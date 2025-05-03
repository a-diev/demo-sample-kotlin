package studio.kimaa.sample.catalog

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.mockito.kotlin.mock
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import studio.kimaa.sample.catalog.application.CatalogDTORequest
import studio.kimaa.sample.catalog.application.CatalogRepository
import studio.kimaa.sample.catalog.application.CatalogService
import studio.kimaa.sample.catalog.application.CatalogSpecificationBuilder
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.time.Instant
import java.util.Optional
import java.util.UUID

class CatalogServiceTest {

    private val repository: CatalogRepository = mock()
    private val service: CatalogService = CatalogService(repository)

    @Test
    fun `should return list of catalogs`() {
        // Arrange
        val listCatalogs: List<CatalogEntity> = listOf(
            CatalogEntity(UUID.randomUUID(), "Gadget", "Katalok untuk semua produk gadget", true, Instant.now()),
            CatalogEntity(UUID.randomUUID(), "Laptop", "Katalok untuk semua produk Laptop", true, Instant.now()),
            CatalogEntity(UUID.randomUUID(), "Standing Desk", "Katalog untuk semua produk standing desk", true, Instant.now()),
        )
        val page: Page<CatalogEntity> = PageImpl(listCatalogs)
        val nowDate: Instant = Instant.now()
        val startDate: Instant = nowDate.minusSeconds(7 * 24 * 60 * 60)
        val endDate: Instant = nowDate
        val specifications = CatalogSpecificationBuilder<CatalogEntity>()
            .like("name", "Gadget")
            .equal("status", true)
            .betweenDates("createdAt", startDate, endDate)
            .build()
        val pageable: Pageable = PageRequest.of(0, 5)

        whenever(repository.findAll(specifications, pageable)).thenReturn(page)

        // Act
        val result = service.index(
            name = "Gadget",
            status = true,
            startDate = startDate,
            endDate = endDate,
            pageable = pageable
        )

        // Assert
        assertEquals(3, result.size)
    }

    @Test
    fun `should return created catalog`() {
        // Arrange
        val request = CatalogDTORequest("Gadget", "Katalog untuk semua produk gadget", true)
        val catalogUID = UUID.randomUUID()
        val created = CatalogEntity(
            catalogId = catalogUID,
            name = request.name,
            description = request.description,
            status = request.status,
            createdAt = Instant.now()
        )

        whenever(repository.save(any<CatalogEntity>())).thenReturn(created)

        // Act
        val result = service.store(request)

        // Assert
        assertEquals(catalogUID.toString(), result)
    }

    @Test
    fun `should return updated catalog`() {
        // Arrange
        val request = CatalogDTORequest("New Laptop", "New Katalok untuk semua produk laptop", true)
        val catalogUID = UUID.randomUUID()
        val existings = CatalogEntity(
            catalogId = catalogUID,
            name = "Old Laptop",
            description = "Old Katalok untuk semua produk laptop",
            status = false,
            createdAt = Instant.now()
        )
        val updated = CatalogEntity(
            catalogId = catalogUID,
            name = request.name,
            description = request.description,
            status = request.status,
            createdAt = Instant.now()
        )

        whenever(repository.findById(catalogUID)).thenReturn(Optional.of(existings))
        whenever(repository.save(any<CatalogEntity>())).thenReturn(updated)

        // Act
        val result = service.update(catalogUID = catalogUID.toString(), catalogRequest = request)

        // Assert
        assertEquals(updated, result)
    }

    @Test
    fun `should return destroy by uid catalog`() {
        // Arrange
        val catalogUID = UUID.randomUUID()
        val existings = CatalogEntity(
            catalogId = catalogUID,
            name = "Laptop",
            description = "Katalok untuk semua produk laptop",
            status = false,
            createdAt = Instant.now()
        )

        whenever(repository.findById(catalogUID)).thenReturn(Optional.of(existings))
        doNothing().whenever(repository).delete(existings)

        // Act
        val result = service.destroy(catalogUID = catalogUID.toString())

        // Assert
        assertEquals(existings, result)
    }

}