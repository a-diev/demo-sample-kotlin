package studio.kimaa.sample.catalog.application

object CatalogResponseUtil {

    fun <T> success(data: T, message: String = "success"): CatalogDTOResponse<T> {
        return CatalogDTOResponse(
            success = true,
            message = message,
            data = data
        )
    }

    fun error(message: String, data: Any? = null): CatalogDTOResponse<Any?> {
        return CatalogDTOResponse(
            success = false,
            message = message,
            data = data
        )
    }

}