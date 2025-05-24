package studio.kimaa.sample.catalog.web

import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import studio.kimaa.sample.catalog.application.CatalogDTOResponse
import studio.kimaa.sample.catalog.application.CatalogDTORequest
import studio.kimaa.sample.catalog.application.CatalogPagedResponse
import studio.kimaa.sample.catalog.application.CatalogResponseUtil
import studio.kimaa.sample.catalog.application.CatalogService
import studio.kimaa.sample.catalog.domain.CatalogEntity
import java.time.Instant

@RestController
@RequestMapping("/catalogs")
class CatalogController(private val service: CatalogService) {

    @GetMapping
    fun index(
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") perPage: Int,
        @RequestParam(name = "name", required = false) name: String?,
        @RequestParam(name = "status", required = false) status: Boolean?,
        @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: Instant?,
        @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: Instant?,
    ): ResponseEntity<CatalogDTOResponse<CatalogPagedResponse<CatalogEntity>>> {
        val result = service.index(page, perPage, name, status, startDate, endDate)
        val response = CatalogPagedResponse(
            result.content,
            result.number,
            result.size,
            result.numberOfElements,
            result.totalElements,
            result.totalPages,
        )
        return ResponseEntity.status(200).body(
            CatalogResponseUtil.success(response, "success")
        )
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun store(
        @Valid @RequestBody catalogRequest: CatalogDTORequest
    ): ResponseEntity<CatalogDTOResponse<String>> {
        val result = service.store(request = catalogRequest)
        return ResponseEntity.status(201).body(
            CatalogResponseUtil.success(result, "success")
        )
    }

    @PutMapping("/{uid}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @PathVariable uid: String,
        @Valid @RequestBody request: CatalogDTORequest
    ): ResponseEntity<CatalogDTOResponse<CatalogEntity>> {
        val result = service.update(catalogUID = uid, catalogRequest = request)
        return ResponseEntity.status(200).body(
            CatalogResponseUtil.success(result, "success")
        )
    }

    @DeleteMapping("/{uid}")
    fun destroy(@PathVariable uid: String): ResponseEntity<CatalogDTOResponse<CatalogEntity>> {
        val result = service.destroy(catalogUID = uid)
        return ResponseEntity.status(200).body(
            CatalogResponseUtil.success(result, "success")
        )
    }
}