package studio.kimaa.sample.product.application

import org.springframework.data.jpa.repository.JpaRepository
import studio.kimaa.sample.product.domain.ProductEntity
import java.util.UUID

interface ProductRepository : JpaRepository<ProductEntity, UUID> {
}