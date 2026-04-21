package repo

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.myproject.backend.repo.tests.DevRepositoryMock
import ru.otus.otuskotlin.myproject.bl.DevProcessor
import ru.otus.otuskotlin.myproject.common.DevContext
import ru.otus.otuskotlin.myproject.common.DevCorSettings
import ru.otus.otuskotlin.myproject.common.models.*
import ru.otus.otuskotlin.myproject.common.repo.DbDevResponseOk
import ru.otus.otuskotlin.myproject.common.repo.errorNotFound
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initAd = DevAd(
    id = DevId("123"),
    name = "abc",
    deviceType = DevType.DEVICE,
    visibility = DeviceVisibility.VISIBLE_PUBLIC,
)
private val repo = DevRepositoryMock(
        invokeReadDev = {
            if (it.id == initAd.id) {
                DbDevResponseOk(
                    data = initAd,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = DevCorSettings(repoTest = repo)
private val processor = DevProcessor(settings)

fun repoNotFoundTest(command: DevCommand) = runTest {
    val ctx = DevContext(
        command = command,
        state = DevState.NONE,
        workMode = DevWorkMode.TEST,
        devRequest = DevAd(
            id = DevId("12345"),
            name = "xyz",
            deviceType = DevType.DEVICE,
            visibility = DeviceVisibility.VISIBLE_TO_GROUP,
            lock = DevLock("123"),
        ),
    )
    processor.exec(ctx)
    assertEquals(DevState.FAILING, ctx.state)
    assertEquals(DevAd(), ctx.devResponse)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
