package com.wilder.mvvmnexus.data.repository

import com.wilder.mvvmnexus.data.model.ProductoModel
import com.wilder.mvvmnexus.data.network.ProductoApiClient
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.repository.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepositoryImpl : ProductoRepository {
    private val api = ProductoApiClient.service

    override suspend fun obtenerProductos(): List<Producto> = withContext(Dispatchers.IO) {
        val response = api.obtenerProductos()
        if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun obtenerProductoPorId(id: Int): Producto? = withContext(Dispatchers.IO) {
        try {
            val response = api.obtenerProductoPorId(id)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

// Mapper
private fun ProductoModel.toDomain() = Producto(
    id = id,
    titulo = titulo,
    precio = precio,
    descripcion = descripcion,
    categoria = categoria,
    imagen = imagen,
    puntuacion = calificacion.puntuacion,
    cantidadVotos = calificacion.cantidadVotos
)