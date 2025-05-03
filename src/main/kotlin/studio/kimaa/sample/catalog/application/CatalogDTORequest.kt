package studio.kimaa.sample.catalog.application

import jakarta.validation.constraints.NotBlank

data class CatalogDTORequest(
    @field:NotBlank(message = "Nama tidak boleh kosong")
    val name: String,

    @field:NotBlank(message = "Deskripsi tidak boleh kosong")
    val description: String,

    val status: Boolean
)
