package ru.otus.otuskotlin.myproject.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import ru.otus.otuskotlin.myproject.backend.repo.cassandra.model.DevCassandraDTO
import ru.otus.otuskotlin.myproject.backend.repo.cassandra.model.toTransport
import ru.otus.otuskotlin.myproject.common.models.DevType
import ru.otus.otuskotlin.myproject.common.models.DevUserId
import ru.otus.otuskotlin.myproject.common.repo.DbDevFilterRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class DevCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<DevCassandraDTO>
) {
    fun search(filter: DbDevFilterRequest): CompletionStage<Collection<DevCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.nameFilter.isNotBlank()) {
            // Внимание! При использовании LIKE необходимо использовать SASI индексы.
            // При использовании SASI индекса типа StandardAnalyzer происходит токенизация текста по пробелам.
            // Оператор LIKE в этом случае должен быть НЕ LIKE '%<токен>%' а LIKE '<токен>%'
            select = select
                .whereColumn(DevCassandraDTO.COLUMN_NAME)
                .like(QueryBuilder.literal("${filter.nameFilter}%"))
        }
        if (filter.ownerId != DevUserId.NONE) {
            select = select
                .whereColumn(DevCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }
        if (filter.devType != DevType.NONE) {
            select = select
                .whereColumn(DevCassandraDTO.COLUMN_DEV_TYPE)
                .isEqualTo(QueryBuilder.literal(filter.devType.toTransport(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<DevCassandraDTO>()
        private val future = CompletableFuture<Collection<DevCassandraDTO>>()
        val stage: CompletionStage<Collection<DevCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}