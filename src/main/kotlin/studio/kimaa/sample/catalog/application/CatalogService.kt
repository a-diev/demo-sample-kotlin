package studio.kimaa.sample.catalog.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import studio.kimaa.sample.catalog.domain.CatalogEntity

@Service
class CatalogService(private val catalogRepository: CatalogRepository) {
    fun index(): Page<CatalogEntity> {
        return PageImpl(listOf<CatalogEntity>())
    }
}