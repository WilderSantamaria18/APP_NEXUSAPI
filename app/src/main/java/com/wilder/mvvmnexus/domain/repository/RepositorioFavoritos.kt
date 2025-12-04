package com.wilder.mvvmnexus.domain.repository

import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.model.ProductoFavorito
import kotlinx.coroutines.flow.Flow

interface RepositorioFavoritos {
    fun obtenerFavoritos(usuarioUid: String): Flow<List<ProductoFavorito>>
    fun esFavorito(usuarioUid: String, productoId: Int): Flow<Boolean>
    suspend fun agregarFavorito(usuarioUid: String, producto: Producto)
    suspend fun eliminarFavorito(usuarioUid: String, productoId: Int)
    suspend fun toggleFavorito(usuarioUid: String, producto: Producto)
}
