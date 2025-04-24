package studio.kimaa.sample.product.web

import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import studio.kimaa.sample.product.application.ProductService
import studio.kimaa.sample.product.domain.ProductEntity

@RestController
class ProductController(private val productService: ProductService) {
    @GetMapping("products")
    fun index(
        @RequestParam(name = "page", required = false, defaultValue = "1") page: String,
        @RequestParam(name = "perPage", required = false, defaultValue = "9") perPage: String,
        @RequestParam(name = "search", required = false, defaultValue = "") search: String
    ): ResponseEntity<Page<ProductEntity>> {
        return ResponseEntity(
            productService.index(),
            HttpStatusCode.valueOf(HttpStatus.OK.value())
        )
    }
}