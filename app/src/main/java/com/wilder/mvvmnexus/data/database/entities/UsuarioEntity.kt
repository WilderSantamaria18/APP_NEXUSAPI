package com.wilder.mvvmnexus.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String, // Usaremos el UID de Firebase como ID
    val nombre: String,
    val email: String,
    val fotoUrl: String? = null
)
