package com.wilder.mvvmnexus.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioUid: String,
    val productoId: Int,
    val titulo: String,
    val precio: Double,
    val imagen: String,
    val categoria: String,
    val fechaAgregado: Long = System.currentTimeMillis()
)
