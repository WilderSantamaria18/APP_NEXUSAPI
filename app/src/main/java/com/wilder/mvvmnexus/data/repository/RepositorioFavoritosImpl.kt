package com.wilder.mvvmnexus.data.repository

import com.wilder.mvvmnexus.data.database.dao.FavoritoDao
import com.wilder.mvvmnexus.data.database.entities.FavoritoEntity
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.model.ProductoFavorito
import com.wilder.mvvmnexus.domain.repository.RepositorioFavoritos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositorioFavoritosImpl(
    private val favoritoDao: FavoritoDao
) : RepositorioFavoritos {
    
    override fun obtenerFavoritos(usuarioUid: String): Flow<List<ProductoFavorito>> {
        return favoritoDao.obtenerFavoritos(usuarioUid).map { entities ->
            entities.map { entity ->
                ProductoFavorito(
                    id = entity.id,
                    productoId = entity.productoId,
                    titulo = entity.titulo,
                    precio = entity.precio,
                    imagen = entity.imagen,
                    categoria = entity.categoria,
                    fechaAgregado = entity.fechaAgregado
                )
            }
        }
    }
    
    override fun esFavorito(usuarioUid: String, productoId: Int): Flow<Boolean> {
        return favoritoDao.esFavorito(usuarioUid, productoId)
    }
    
    override suspend fun agregarFavorito(usuarioUid: String, producto: Producto) {
        val favorito = FavoritoEntity(
            usuarioUid = usuarioUid,
            productoId = producto.id,
            titulo = producto.titulo,
            precio = producto.precio,
            imagen = producto.imagen,
            categoria = producto.categoria
        )
        favoritoDao.insertarFavorito(favorito)
    }
    
    override suspend fun eliminarFavorito(usuarioUid: String, productoId: Int) {
        favoritoDao.eliminarFavorito(usuarioUid, productoId)
    }
    
    override suspend fun toggleFavorito(usuarioUid: String, producto: Producto) {
        val favoritoExistente = favoritoDao.obtenerFavorito(usuarioUid, producto.id)
        if (favoritoExistente != null) {
            eliminarFavorito(usuarioUid, producto.id)
        } else {
            agregarFavorito(usuarioUid, producto)
        }
    }
}
