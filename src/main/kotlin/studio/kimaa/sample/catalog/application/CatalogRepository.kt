package studio.kimaa.sample.catalog.application

import org.springframework.data.jpa.repository.JpaRepository
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.util.UUID

interface CatalogRepository : JpaRepository<CatalogEntity, UUID> {
}