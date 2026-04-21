package ru.otus.otuskotlin.myproject.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.*
import ru.otus.otuskotlin.myproject.repo.common.IRepoDevInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class DevRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : DevRepoBase(), IRepoDev, IRepoDevInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, DevEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(ads: Collection<DevAd>) = ads.map { ad ->
        val entity = DevEntity(ad)
        require(entity.id != null)
        cache.put(entity.id, entity)
        ad
    }

    override suspend fun createDev(rq: DbDevRequest): IDbDevResponse = tryAdMethod {
        val key = randomUuid()
        val ad = rq.dev.copy(id = DevId(key))
        val entity = DevEntity(ad)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbDevResponseOk(ad)
    }

    override suspend fun readDev(rq: DbDevIdRequest): IDbDevResponse = tryAdMethod {
        val key = rq.id.takeIf { it != DevId.NONE }?.asString() ?: return@tryAdMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbDevResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateDev(rq: DbDevRequest): IDbDevResponse = tryAdMethod {
        val rqAd = rq.dev
        val id = rqAd.id.takeIf { it != DevId.NONE } ?: return@tryAdMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                else -> {
                    val newAd = rqAd.copy()
                    val entity = DevEntity(newAd)
                    cache.put(key, entity)
                    DbDevResponseOk(newAd)
                }
            }
        }
    }

    override suspend fun deleteDev(rq: DbDevIdRequest): IDbDevResponse = tryAdMethod {
        val id = rq.id.takeIf { it != DevId.NONE } ?: return@tryAdMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldAd = cache.get(key)?.toInternal()
            when {
                oldAd == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(key)
                    DbDevResponseOk(oldAd)
                }
            }
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchDev(rq: DbDevFilterRequest): IDbDevsResponse = tryAdsMethod {
        val result: List<DevAd> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != DevUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.devType.takeIf { it != DevType.NONE }?.let {
                    it.name == entry.value.devType
                } ?: true
            }
            .filter { entry ->
                rq.nameFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.name?.contains(it) ?: false
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbDevsResponseOk(result)
    }
}
