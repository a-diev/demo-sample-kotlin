package studio.kimaa.sample.catalog.application

data class CatalogPagedResponse<T>(
    val data: List<T>,
    val page: Int,
    val size: Int,
    val numberOfElements: Int,
    val totalElements: Long,
    val totalPages: Int
)
