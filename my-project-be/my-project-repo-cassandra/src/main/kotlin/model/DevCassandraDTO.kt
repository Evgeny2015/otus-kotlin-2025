package ru.otus.otuskotlin.myproject.backend.repo.cassandra.model

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import ru.otus.otuskotlin.myproject.common.models.*

@Entity
data class DevCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey // можно задать порядок
    var id: String? = null,
    @field:CqlName(COLUMN_NAME)
    var name: String? = null,
    @field:CqlName(COLUMN_OWNER_ID)
    var ownerId: String? = null,
    @field:CqlName(COLUMN_VISIBILITY)
    var visibility: DevVisibility? = null,
    // Нельзя использовать в моделях хранения внутренние модели.
    // При изменении внутренних моделей, БД автоматически не изменится,
    // а потому будет Runtime ошибка, которая вылезет только на продуктовом стенде
    @field:CqlName(COLUMN_DEV_TYPE)
    var devType: DeviceType? = null,
    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {
    constructor(devModel: DevAd) : this(
        ownerId = devModel.ownerId.takeIf { it != DevUserId.NONE }?.asString(),
        id = devModel.id.takeIf { it != DevId.NONE }?.asString(),
        name = devModel.name.takeIf { it.isNotBlank() },
        visibility = devModel.visibility.toTransport(),
        devType = devModel.deviceType.toTransport(),
        lock = devModel.lock.takeIf { it != DevLock.NONE }?.asString()
    )

    fun toDevModel(): DevAd =
        DevAd(
            ownerId = ownerId?.let { DevUserId(it) } ?: DevUserId.NONE,
            id = id?.let { DevId(it) } ?: DevId.NONE,
            name = name ?: "",
            visibility = visibility.fromTransport(),
            deviceType = devType.fromTransport(),
            lock = lock?.let { DevLock(it) } ?: DevLock.NONE
        )

    companion object {
        const val TABLE_NAME = "project_devs"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_OWNER_ID = "owner_id"
        const val COLUMN_VISIBILITY = "visibility"
        const val COLUMN_DEV_TYPE = "dev_type"
        const val COLUMN_LOCK = "lock"

    }
}
