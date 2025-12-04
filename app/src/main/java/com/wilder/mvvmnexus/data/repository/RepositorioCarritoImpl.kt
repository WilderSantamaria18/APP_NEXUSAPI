package com.wilder.mvvmnexus.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.wilder.mvvmnexus.data.database.dao.CarritoDao
import com.wilder.mvvmnexus.data.database.entities.CarritoEntity
import com.wilder.mvvmnexus.domain.model.CarritoItem
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.repository.RepositorioCarrito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class RepositorioCarritoImpl(
    private val carritoDao: CarritoDao
) : RepositorioCarrito {

    private val firestore = FirebaseFirestore.getInstance()
    private val carritoCollection = firestore.collection("carritos")

    override fun obtenerCarrito(usuarioId: String): Flow<List<CarritoItem>> {
        return carritoDao.obtenerCarritoPorUsuario(usuarioId).map { entities ->
            entities.map { it.aDominio() }
        }
    }

    override suspend fun agregarProducto(usuarioId: String, producto: Producto, cantidad: Int) {
        val itemExistente = carritoDao.obtenerProductoEnCarrito(usuarioId, producto.id)
        
        if (itemExistente != null) {
            val nuevaCantidad = itemExistente.cantidad + cantidad
            carritoDao.actualizarCantidad(itemExistente.id, nuevaCantidad)
            actualizarEnFirestore(usuarioId, itemExistente.copy(cantidad = nuevaCantidad))
        } else {
            val nuevoItem = CarritoEntity(
                usuarioId = usuarioId,
                productoId = producto.id,
                titulo = producto.titulo,
                precio = producto.precio,
                imagen = producto.imagen,
                cantidad = cantidad
            )
            carritoDao.insertarProducto(nuevoItem)
            
            val itemConId = carritoDao.obtenerProductoEnCarrito(usuarioId, producto.id)
            itemConId?.let { actualizarEnFirestore(usuarioId, it) }
        }
    }

    override suspend fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) {
        if (nuevaCantidad > 0) {
            carritoDao.actualizarCantidad(item.id, nuevaCantidad)
            
            val itemActualizado = carritoDao.obtenerProductoEnCarritoPorId(item.id)
            itemActualizado?.let { 
                actualizarEnFirestore(it.usuarioId, it) 
            }
        } else {
            eliminarProducto(item)
        }
    }

    override suspend fun eliminarProducto(item: CarritoItem) {
        val entity = carritoDao.obtenerProductoEnCarritoPorId(item.id)
        entity?.let {
            carritoDao.eliminarProductoPorId(item.id)
            eliminarDeFirestore(it.usuarioId, it.productoId.toString())
        }
    }

    override suspend fun vaciarCarrito(usuarioId: String) {
        carritoDao.vaciarCarrito(usuarioId)
        vaciarFirestore(usuarioId)
    }

    override suspend fun sincronizarDesdeFirestore(usuarioId: String) {
        try {
            val snapshot = carritoCollection
                .document(usuarioId)
                .collection("items")
                .get()
                .await()

            carritoDao.vaciarCarrito(usuarioId)

            snapshot.documents.forEach { doc ->
                val item = CarritoEntity(
                    id = 0,
                    usuarioId = usuarioId,
                    productoId = doc.getLong("productoId")?.toInt() ?: 0,
                    titulo = doc.getString("titulo") ?: "",
                    precio = doc.getDouble("precio") ?: 0.0,
                    imagen = doc.getString("imagen") ?: "",
                    cantidad = doc.getLong("cantidad")?.toInt() ?: 1
                )
                carritoDao.insertarProducto(item)
            }
        } catch (e: Exception) {
            // Silenciosamente fallar si no hay conexion
        }
    }

    private suspend fun actualizarEnFirestore(usuarioId: String, item: CarritoEntity) {
        try {
            val itemData = hashMapOf(
                "productoId" to item.productoId,
                "titulo" to item.titulo,
                "precio" to item.precio,
                "imagen" to item.imagen,
                "cantidad" to item.cantidad
            )

            carritoCollection
                .document(usuarioId)
                .collection("items")
                .document(item.productoId.toString())
                .set(itemData)
                .await()
        } catch (e: Exception) {
            // Silenciosamente fallar si no hay conexion
        }
    }

    private suspend fun eliminarDeFirestore(usuarioId: String, productoId: String) {
        try {
            carritoCollection
                .document(usuarioId)
                .collection("items")
                .document(productoId)
                .delete()
                .await()
        } catch (e: Exception) {
            // Silenciosamente fallar si no hay conexion
        }
    }

    private suspend fun vaciarFirestore(usuarioId: String) {
        try {
            val snapshot = carritoCollection
                .document(usuarioId)
                .collection("items")
                .get()
                .await()

            snapshot.documents.forEach { doc ->
                doc.reference.delete().await()
            }
        } catch (e: Exception) {
            // Silenciosamente fallar si no hay conexion
        }
    }

    private fun CarritoEntity.aDominio(): CarritoItem {
        return CarritoItem(
            id = id,
            productoId = productoId,
            titulo = titulo,
            precio = precio,
            imagen = imagen,
            cantidad = cantidad
        )
    }
}
