import ru.otus.otuskotlin.myproject.backend.repo.tests.*
import ru.otus.otuskotlin.myproject.repo.common.DevRepoInitialized
import ru.otus.otuskotlin.myproject.repo.inmemory.DevRepoInMemory

class DevRepoInMemoryCreateTest : RepoDevCreateTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class DevRepoInMemoryDeleteTest : RepoDevDeleteTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(),
        initObjects = initObjects,
    )
}

class DevRepoInMemoryReadTest : RepoDevReadTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(),
        initObjects = initObjects,
    )
}

class DevRepoInMemorySearchTest : RepoDevSearchTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(),
        initObjects = initObjects,
    )
}

class DevRepoInMemoryUpdateTest : RepoDevUpdateTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory( randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
