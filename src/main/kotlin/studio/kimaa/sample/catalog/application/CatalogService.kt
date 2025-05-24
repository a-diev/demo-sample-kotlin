package studio.kimaa.sample.catalog.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.time.Instant
import java.util.UUID

@Service
class CatalogService(private val repository: CatalogRepository) {
    fun index(page: Int = 0, perPage: Int = 10, name: String? = null, status: Boolean? = null, startDate: Instant? = null, endDate: Instant? = null): Page<CatalogEntity> {
        val pageable: Pageable = PageRequest.of(page, perPage)
        val spec = CatalogSpecificationBuilder<CatalogEntity>()
            .like("name", name)
            .equal("status", status)
            .betweenDates("createdAt", startDate, endDate)
            .build()

        return repository.findAll(spec, pageable)
    }

    fun store(request: CatalogDTORequest): String {
        val catalog = CatalogEntity(
            name = request.name,
            description = request.description,
            status = request.status
        )
        val result = repository.save(catalog)
        return result.catalogId.toString()
    }

    fun update(catalogUID: String, catalogRequest: CatalogDTORequest): CatalogEntity {
        val findExist = repository.findById(UUID.fromString(catalogUID)).orElseThrow {
            CatalogNotFoundException("Catalog dengan UID $catalogUID tidak ditemukan")
        }
        val updated = findExist.copy(
            name = catalogRequest.name,
            description = catalogRequest.description,
            status = catalogRequest.status
        )
        return repository.save<CatalogEntity>(updated)
    }

    fun destroy(catalogUID: String): CatalogEntity {
        val findExist = repository.findById(UUID.fromString(catalogUID)).orElseThrow {
            CatalogNotFoundException("Catalog dengan UID $catalogUID tidak ditemukan")
        }
        repository.delete(findExist)
        return findExist
    }
}