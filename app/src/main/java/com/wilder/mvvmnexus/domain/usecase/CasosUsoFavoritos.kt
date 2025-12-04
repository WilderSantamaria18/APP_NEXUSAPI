package com.wilder.mvvmnexus.domain.usecase

import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.model.ProductoFavorito
import com.wilder.mvvmnexus.domain.repository.RepositorioFavoritos
import kotlinx.coroutines.flow.Flow

class CasosUsoFavoritos(
    private val repositorio: RepositorioFavoritos
) {
    
    fun obtenerFavoritos(usuarioUid: String): Flow<List<ProductoFavorito>> {
        return repositorio.obtenerFavoritos(usuarioUid)
    }
    
    fun esFavorito(usuarioUid: String, productoId: Int): Flow<Boolean> {
        return repositorio.esFavorito(usuarioUid, productoId)
    }
    
    suspend fun agregarFavorito(usuarioUid: String, producto: Producto) {
        repositorio.agregarFavorito(usuarioUid, producto)
    }
    
    suspend fun eliminarFavorito(usuarioUid: String, productoId: Int) {
        repositorio.eliminarFavorito(usuarioUid, productoId)
    }
    
    suspend fun toggleFavorito(usuarioUid: String, producto: Producto) {
        repositorio.toggleFavorito(usuarioUid, producto)
    }
}
