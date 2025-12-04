package com.wilder.mvvmnexus.domain.model

data class ProductoFavorito(
    val id: Int,
    val productoId: Int,
    val titulo: String,
    val precio: Double,
    val imagen: String,
    val categoria: String,
    val fechaAgregado: Long
)
