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
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
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
        val listCatalogs = listOf(
            CatalogEntity(UUID.randomUUID(), "Gadget", "Katalok untuk semua produk gadget"),
            CatalogEntity(UUID.randomUUID(), "Laptop", "Katalok untuk semua produk laptop"),
        )
        val pageable: Pageable = PageRequest.of(0, 5, Sort.by("createdAt").ascending())
        val pageResult: Page<CatalogEntity> = PageImpl(listCatalogs)
        val result = CatalogPagedResponse(
            data = pageResult.content,
            page = pageResult.number,
            size = pageResult.size,
            totalElements = pageResult.totalElements,
            totalPages = pageResult.totalPages
        )

        whenever(service.index(name = "Gadget", status = true, startDate = Instant.now(), endDate = Instant.now(), pageable = pageable)).thenReturn(result)

        mockMvc.get("/catalogs") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath("$.success") { value(true) }
            jsonPath("$.message") { value("success") }
            jsonPath("$.data.size") { value(result.size) }
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