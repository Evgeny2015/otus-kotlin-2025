package ru.otus.otuskotlin.myproject.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import ru.otus.otuskotlin.myproject.app.spring.config.DevConfig
import ru.otus.otuskotlin.myproject.app.spring.controllers.DevControllerV2Fine
import ru.otus.otuskotlin.myproject.common.models.DevType
import ru.otus.otuskotlin.myproject.common.repo.DbDevFilterRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevIdRequest
import ru.otus.otuskotlin.myproject.common.repo.DbDevRequest
import ru.otus.otuskotlin.myproject.common.repo.IRepoDev
import ru.otus.otuskotlin.myproject.repo.common.DevRepoInitialized
import ru.otus.otuskotlin.myproject.repo.inmemory.DevRepoInMemory
import ru.otus.otuskotlin.myproject.stubs.DevStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(DevControllerV2Fine::class, DevConfig::class)
internal class DevRepoInMemoryV2Test : DevRepoBaseV2Test() {
    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoDev

    @BeforeEach
    fun tearUp() {
        val slotAd = slot<DbDevRequest>()
        val slotId = slot<DbDevIdRequest>()
        val slotFl = slot<DbDevFilterRequest>()
        val repo = DevRepoInitialized(
            repo = DevRepoInMemory(randomUuid = { uuidNew }),
            initObjects = DevStub.prepareSearchList("xx", DevType.DEVICE) + DevStub.get()
        )
        coEvery { testTestRepo.createDev(capture(slotAd)) } coAnswers { repo.createDev(slotAd.captured) }
        coEvery { testTestRepo.readDev(capture(slotId)) } coAnswers { repo.readDev(slotId.captured) }
        coEvery { testTestRepo.updateDev(capture(slotAd)) } coAnswers { repo.updateDev(slotAd.captured) }
        coEvery { testTestRepo.deleteDev(capture(slotId)) } coAnswers { repo.deleteDev(slotId.captured) }
        coEvery { testTestRepo.searchDev(capture(slotFl)) } coAnswers { repo.searchDev(slotFl.captured) }
    }

    @Test
    override fun createDev() = super.createDev()

    @Test
    override fun readDev() = super.readDev()

    @Test
    override fun updateDev() = super.updateDev()

    @Test
    override fun deleteDev() = super.deleteDev()

    @Test
    override fun searchDev() = super.searchDev()

}
