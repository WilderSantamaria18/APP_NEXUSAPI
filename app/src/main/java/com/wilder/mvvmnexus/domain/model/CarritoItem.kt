package com.wilder.mvvmnexus.domain.model

data class CarritoItem(
    val id: Int,
    val productoId: Int,
    val titulo: String,
    val precio: Double,
    val imagen: String,
    val cantidad: Int
) {
    val total: Double
        get() = precio * cantidad
}
