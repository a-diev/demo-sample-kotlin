package studio.kimaa.sample.product.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id @Column(name = "product_id", columnDefinition = "UUID")
    val productId: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    val catalog: CatalogEntity,

    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val sku: String,

    @Column(name = "image_url")
    val imageUrl: String,

    val status: Boolean = true,

    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
)
