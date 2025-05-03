package studio.kimaa.sample.catalog.application

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.util.UUID

interface CatalogRepository : JpaRepository<CatalogEntity, UUID>, JpaSpecificationExecutor<CatalogEntity> {
}