package studio.kimaa.sample.catalog.application

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.time.Instant

class CatalogSpecificationBuilder<T> {

    private val predicates = mutableListOf<(root: Root<T>, cb: CriteriaBuilder) -> Predicate>()

    fun like(field: String, value: String?): CatalogSpecificationBuilder<T> {
        if (!value.isNullOrBlank()) {
            predicates.add { root, cb ->
                cb.like(cb.lower(root.get(field)), "%${value.lowercase()}%")
            }
        }
        return this
    }

    fun equal(field: String, value: Any?): CatalogSpecificationBuilder<T> {
        if (value != null) {
            predicates.add { root, cb ->
                cb.equal(root.get<Any>(field), value)
            }
        }
        return this
    }

    fun betweenDates(field: String, start: Instant?, end: Instant?): CatalogSpecificationBuilder<T> {
        if (start != null && end != null) {
            predicates.add { root, cb ->
                cb.between(root.get(field), start, end)
            }
        } else if (start != null) {
            predicates.add { root, cb ->
                cb.greaterThanOrEqualTo(root.get(field), start)
            }
        } else if (end != null) {
            predicates.add { root, cb ->
                cb.lessThanOrEqualTo(root.get(field), end)
            }
        }
        return this
    }


    fun build(): Specification<T> {
        return Specification { root, _, cb ->
            cb.and(*predicates.map { it(root, cb) }.toTypedArray())
        }
    }
}