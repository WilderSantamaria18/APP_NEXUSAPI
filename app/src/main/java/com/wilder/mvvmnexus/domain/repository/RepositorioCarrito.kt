package com.wilder.mvvmnexus.domain.repository

import com.wilder.mvvmnexus.domain.model.CarritoItem
import com.wilder.mvvmnexus.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface RepositorioCarrito {
    fun obtenerCarrito(usuarioId: String): Flow<List<CarritoItem>>
    suspend fun agregarProducto(usuarioId: String, producto: Producto, cantidad: Int)
    suspend fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int)
    suspend fun eliminarProducto(item: CarritoItem)
    suspend fun vaciarCarrito(usuarioId: String)
    suspend fun sincronizarDesdeFirestore(usuarioId: String)
}
