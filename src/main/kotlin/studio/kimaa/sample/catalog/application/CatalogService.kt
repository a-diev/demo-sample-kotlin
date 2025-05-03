package studio.kimaa.sample.catalog.application

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.time.Instant
import java.util.UUID

@Service
class CatalogService(private val repository: CatalogRepository) {
    fun index(name: String?, status: Boolean?, startDate: Instant?, endDate: Instant?, pageable: Pageable): CatalogPagedResponse<CatalogEntity> {
        val specifications = CatalogSpecificationBuilder<CatalogEntity>()
            .like("name", name)
            .equal("status", status)
            .betweenDates("createdAt", startDate, endDate)
            .build()
        val pageResult = repository.findAll(specifications, pageable)
        return CatalogPagedResponse(
            data = pageResult.content,
            page = pageResult.number,
            size = pageResult.size,
            totalElements = pageResult.totalElements,
            totalPages = pageResult.totalPages
        )
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