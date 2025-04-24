package studio.kimaa.sample.catalog.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "catalogs")
data class CatalogEntity(
    @Id @Column(name = "catalog_id", columnDefinition = "UUID")
    val catalogId: UUID = UUID.randomUUID(),

    val name: String,
    val description: String,
    val status: Boolean = true,

    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
)