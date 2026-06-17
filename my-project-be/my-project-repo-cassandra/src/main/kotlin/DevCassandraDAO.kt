package ru.otus.otuskotlin.myproject.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import ru.otus.otuskotlin.myproject.backend.repo.cassandra.model.DevCassandraDTO
import ru.otus.otuskotlin.myproject.backend.repo.cassandra.model.DevCassandraDTO.Companion.COLUMN_LOCK
import ru.otus.otuskotlin.myproject.common.repo.DbDevFilterRequest
import java.util.concurrent.CompletionStage

@Dao
interface DevCassandraDAO {
    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: DevCassandraDTO): CompletionStage<DevCassandraDTO>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(id: String): CompletionStage<DevCassandraDTO?>

    @Update(customIfClause = "${COLUMN_LOCK} = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: DevCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "${COLUMN_LOCK} = :prevLock", entityClass = [DevCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @Query("TRUNCATE ${DevCassandraDTO.TABLE_NAME}")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun deleteAll()

    @QueryProvider(providerClass = DevCassandraSearchProvider::class, entityHelpers = [DevCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun search(filter: DbDevFilterRequest): CompletionStage<Collection<DevCassandraDTO>>
}
