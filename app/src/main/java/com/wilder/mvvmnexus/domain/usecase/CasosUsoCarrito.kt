package com.wilder.mvvmnexus.domain.usecase

import com.wilder.mvvmnexus.domain.model.CarritoItem
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.repository.RepositorioCarrito
import kotlinx.coroutines.flow.Flow

class CasosUsoCarrito(private val repositorio: RepositorioCarrito) {

    fun obtenerCarrito(usuarioId: String): Flow<List<CarritoItem>> {
        return repositorio.obtenerCarrito(usuarioId)
    }

    suspend fun agregarProducto(usuarioId: String, producto: Producto, cantidad: Int) {
        repositorio.agregarProducto(usuarioId, producto, cantidad)
    }

    suspend fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) {
        repositorio.actualizarCantidad(item, nuevaCantidad)
    }

    suspend fun eliminarProducto(item: CarritoItem) {
        repositorio.eliminarProducto(item)
    }

    suspend fun vaciarCarrito(usuarioId: String) {
        repositorio.vaciarCarrito(usuarioId)
    }

    suspend fun sincronizarCarrito(usuarioId: String) {
        repositorio.sincronizarDesdeFirestore(usuarioId)
    }
}
