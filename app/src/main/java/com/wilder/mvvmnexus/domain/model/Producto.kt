package com.wilder.mvvmnexus.domain.model

data class Producto(
    val id: Int,
    val titulo: String,
    val precio: Double,
    val descripcion: String,
    val categoria: String,
    val imagen: String,
    val puntuacion: Double,
    val cantidadVotos: Int
)