package com.wilder.mvvmnexus.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: String,
    val productoId: Int,
    val titulo: String,
    val precio: Double,
    val imagen: String,
    val cantidad: Int
)
