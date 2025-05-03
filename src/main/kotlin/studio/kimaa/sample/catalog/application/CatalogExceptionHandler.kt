package studio.kimaa.sample.catalog.application

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CatalogExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<CatalogDTOResponse<Any?>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return ResponseEntity.badRequest().body(CatalogResponseUtil.error("failed", errors))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<CatalogDTOResponse<Any?>> {
        return ResponseEntity.status(500).body(CatalogResponseUtil.error("unexpected error", ex.localizedMessage))
    }
}