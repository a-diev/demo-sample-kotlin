package studio.kimaa.sample.catalog.application

data class CatalogDTOResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)