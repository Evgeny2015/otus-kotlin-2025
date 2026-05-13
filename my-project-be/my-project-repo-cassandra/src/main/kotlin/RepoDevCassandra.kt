package ru.otus.otuskotlin.myproject.backend.repo.cassandra

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.myproject.backend.repo.cassandra.model.DevCassandraDTO
import ru.otus.otuskotlin.myproject.backend.repo.cassandra.model.DeviceType
import ru.otus.otuskotlin.myproject.backend.repo.cassandra.model.DevVisibility
import ru.otus.otuskotlin.myproject.common.models.DevAd
import ru.otus.otuskotlin.myproject.common.models.DevId
import ru.otus.otuskotlin.myproject.common.models.DevLock
import ru.otus.otuskotlin.myproject.common.repo.*
import ru.otus.otuskotlin.myproject.repo.common.IRepoDevInitializable
import java.net.InetAddress
import java.net.InetSocketAddress

class RepoDevCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val dc: String = "dc1",
    private val randomUuid: () -> String = { uuid4().toString() },
) : DevRepoBase(), IRepoDev, IRepoDevInitializable {
    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(DevVisibility::class.java))
            register(EnumNameCodec(DeviceType::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter(dc)
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .withKeyspace(keyspaceName)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private val dao by lazy {
        mapper.devDao(keyspaceName, DevCassandraDTO.TABLE_NAME)
    }

    fun clear() = dao.deleteAll()

    override fun save(devs: Collection<DevAd>): Collection<DevAd> = runBlocking {
        devs.onEach {
            dao.create(DevCassandraDTO(it)).await()
        }
    }

    override suspend fun createDev(rq: DbDevRequest): IDbDevResponse = tryDevMethod {
        val new = rq.dev.copy(id = DevId(randomUuid()), lock = DevLock(randomUuid()))
        dao.create(DevCassandraDTO(new)).await()
        DbDevResponseOk(new)
    }

    override suspend fun readDev(rq: DbDevIdRequest): IDbDevResponse = tryDevMethod {
        if (rq.id == DevId.NONE) return@tryDevMethod errorEmptyId
        val res = dao.read(rq.id.asString()).await() ?: return@tryDevMethod errorNotFound(rq.id)
        DbDevResponseOk(res.toDevModel())
    }

    override suspend fun updateDev(rq: DbDevRequest): IDbDevResponse = tryDevMethod {
        val idStr = rq.dev.id.asString()
        val prevLock = rq.dev.lock.asString()
        val new = rq.dev.copy(lock = DevLock(randomUuid()))
        val dto = DevCassandraDTO(new)

        val res: AsyncResultSet = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(DevCassandraDTO.COLUMN_LOCK) }
            ?.getString(DevCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbDevResponseOk(new)
            // res.wasApplied() -> DbAdResponse.success(dao.read(idStr).await()?.toAdModel())
            resultField == null -> errorNotFound(rq.dev.id)
            else -> errorRepoConcurrency(
                oldDev = dao.read(idStr).await()?.toDevModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was denied for update but the same object was not found in db at further request"
                ),
                expectedLock = rq.dev.lock
            )
        }
    }

    override suspend fun deleteDev(rq: DbDevIdRequest): IDbDevResponse = tryDevMethod {
        val idStr = rq.id.asString()
        val prevLock = rq.lock.asString()
        val oldDev = dao.read(idStr).await()?.toDevModel() ?: return@tryDevMethod errorNotFound(rq.id)
        val res = dao.delete(idStr, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(DevCassandraDTO.COLUMN_LOCK) }
            ?.getString(DevCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbDevResponseOk(oldDev)
            resultField == null -> errorNotFound(rq.id)
            else -> errorRepoConcurrency(
                dao.read(idStr).await()?.toDevModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was successfully read but was denied for delete"
                ),
                rq.lock
            )
        }
    }

    override suspend fun searchDev(rq: DbDevFilterRequest): IDbDevsResponse = tryDevsMethod {
        val found = dao.search(rq).await()
        DbDevsResponseOk(found.map { it.toDevModel() })
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}
