package com.wilder.mvvmnexus.domain.repository

import com.wilder.mvvmnexus.domain.model.Producto

interface ProductoRepository {
    suspend fun obtenerProductos(): List<Producto>
    suspend fun obtenerProductoPorId(id: Int): Producto?
}