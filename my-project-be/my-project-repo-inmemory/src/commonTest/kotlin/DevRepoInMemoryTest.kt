import ru.otus.otuskotlin.myproject.backend.repo.tests.*
import ru.otus.otuskotlin.myproject.repo.common.DevRepoInitialized
import ru.otus.otuskotlin.myproject.repo.inmemory.DevRepoInMemory

class AdRepoInMemoryCreateTest : RepoDevCreateTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryDeleteTest : RepoDevDeleteTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryReadTest : RepoDevReadTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemorySearchTest : RepoDevSearchTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(),
        initObjects = initObjects,
    )
}

class AdRepoInMemoryUpdateTest : RepoDevUpdateTest() {
    override val repo = DevRepoInitialized(
        DevRepoInMemory(),
        initObjects = initObjects,
    )
}
