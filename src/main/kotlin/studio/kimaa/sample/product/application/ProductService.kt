package studio.kimaa.sample.product.application

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import studio.kimaa.sample.product.domain.ProductEntity

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun index(): Page<ProductEntity> {
        return PageImpl(listOf<ProductEntity>())
    }
}