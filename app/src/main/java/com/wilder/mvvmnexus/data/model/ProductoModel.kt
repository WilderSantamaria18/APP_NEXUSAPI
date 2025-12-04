package com.wilder.mvvmnexus.data.model

import com.google.gson.annotations.SerializedName

data class ProductoModel(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("price") val precio: Double,
    @SerializedName("description") val descripcion: String,
    @SerializedName("category") val categoria: String,
    @SerializedName("image") val imagen: String,
    @SerializedName("rating") val calificacion: RatingModel
)

data class RatingModel(
    @SerializedName("rate") val puntuacion: Double,
    @SerializedName("count") val cantidadVotos: Int
)