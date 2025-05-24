package studio.kimaa.sample.catalog

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import studio.kimaa.sample.catalog.application.CatalogDTORequest
import studio.kimaa.sample.catalog.application.CatalogRepository
import studio.kimaa.sample.catalog.application.CatalogService
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.time.Instant
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class CatalogServiceTest {

    @Mock
    private lateinit var repository: CatalogRepository
    @InjectMocks
    private lateinit var service: CatalogService

    @Test
    fun `should return list of catalogs`() {
        val pageable = PageRequest.of(0, 10) // Page 0, size 10

        val initItems = listOf(
            CatalogEntity(
                UUID.fromString("87386e6d-cea9-4c42-9d89-d6d216d0b857"),
                "Pakaian Pria",
                "Kumpulan pakaian pria.",
                false,
                Instant.parse("2025-05-14T02:56:29.203555Z")
            )
        )
        val initPage = PageImpl(initItems, pageable, initItems.size.toLong())

        whenever(repository.findAll(any<Specification<CatalogEntity>>(), any<Pageable>())).thenReturn(initPage)

        val result = service.index(
            page = 0,
            perPage = 10,
            name = "Pakaian Pria",
            status = false,
            startDate = Instant.parse("2025-05-14T02:56:29.203555Z"),
            endDate = Instant.parse("2025-05-17T02:56:29.203555Z"),
        )

        assertEquals(1, result.content.size)
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