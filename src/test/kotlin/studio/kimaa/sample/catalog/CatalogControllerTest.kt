package studio.kimaa.sample.catalog

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.util.LinkedMultiValueMap
import studio.kimaa.sample.catalog.application.CatalogDTORequest
import studio.kimaa.sample.catalog.application.CatalogPagedResponse
import studio.kimaa.sample.catalog.application.CatalogService
import studio.kimaa.sample.catalog.domain.CatalogEntity
import studio.kimaa.sample.catalog.web.CatalogController
import java.time.Instant
import java.util.UUID

@WebMvcTest(CatalogController::class)
class CatalogControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var service: CatalogService

    @Test
    fun `should return list of catalogs`() {
        val pageable: Pageable = PageRequest.of(0, 10) // Page 0, size 10

        val initItems = listOf(
            CatalogEntity(
                UUID.fromString("87386e6d-cea9-4c42-9d89-d6d216d0b857"),
                "Pakaian Pria",
                "Kumpulan pakaian pria.",
                false,
                Instant.parse("2025-05-14T02:56:29.203555Z")
            )
        )
        val initPage: Page<CatalogEntity> = PageImpl(initItems, pageable, initItems.size.toLong())

        whenever(service.index(
            // page = 0,
            // perPage = 10,
            // name = "Pakaian Pria",
            // status = false,
            // startDate = Instant.parse("2025-05-14T02:56:29.203555Z"),
            // endDate = Instant.parse("2025-05-16T02:56:29.203555Z"),
        )).thenReturn(initPage)

        mockMvc.get("/catalogs") {
            contentType = MediaType.APPLICATION_JSON
            params = LinkedMultiValueMap<String, String>().apply {
                add("page", "0")
                add("perPage", "10")
            }
        }.andExpect {
            status { isOk() }
            jsonPath("$.success") { value(true) }
            jsonPath("$.message") { value("success") }
            jsonPath("$.data.numberOfElements") { value(initPage.numberOfElements) }
        }
    }

    @Test
    fun `should return created catalog`() {
        val request = CatalogDTORequest("Gadget", "Katalog untuk semua produk gadget", true)
        val catalogUID = UUID.randomUUID()

        whenever(service.store(request)).thenReturn(catalogUID.toString())

        mockMvc.post("/catalogs") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.success") { value(true) }
            jsonPath("$.message") { value("success") }
            jsonPath("$.data") { value(catalogUID.toString())}
        }
    }

    @Test
    fun `should return updated catalog`() {
        val request = CatalogDTORequest("New Laptop", "New Katalok untuk semua produk laptop", true)
        val updated = CatalogEntity(
            catalogId = UUID.randomUUID(),
            name = "New Laptop",
            description = "New Katalok untuk semua produk laptop",
            status = true,
            createdAt = Instant.now()
        )

        whenever(service.update(updated.catalogId.toString(), request)).thenReturn(updated)

        mockMvc.put("/catalogs/${updated.catalogId}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.success") {value(true)}
            jsonPath("$.message") {value("success")}
            jsonPath("$.data.catalogId") { value(updated.catalogId.toString()) }
            jsonPath("$.data.name") { value(updated.name) }
            jsonPath("$.data.description") { value(updated.description) }
            jsonPath("$.data.status") { value(updated.status) }
            jsonPath("$.data.createdAt") { value(updated.createdAt.toString()) }
        }
    }

    @Test
    fun `should return deleted by uid catalog`() {
        val deleted = CatalogEntity(
            catalogId = UUID.randomUUID(),
            name = "Delete Laptop",
            description = "Delete Katalog untuk semua produk laptop",
            status = true,
            createdAt = Instant.now()
        )

        whenever(service.destroy(deleted.catalogId.toString())).thenReturn(deleted)

        mockMvc.delete("/catalogs/${deleted.catalogId}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.success") { value(true) }
            jsonPath("$.message") { value("success") }
            jsonPath("$.data.catalogId") { value(deleted.catalogId.toString()) }
            jsonPath("$.data.name") { value(deleted.name) }
            jsonPath("$.data.description") { value(deleted.description) }
            jsonPath("$.data.status") { value(deleted.status) }
            jsonPath("$.data.createdAt") { value(deleted.createdAt.toString()) }
        }
    }

}