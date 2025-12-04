package com.wilder.mvvmnexus.data.database.dao

import androidx.room.*
import com.wilder.mvvmnexus.data.database.entities.CarritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Query("SELECT * FROM carrito WHERE usuarioId = :usuarioId")
    fun obtenerCarritoPorUsuario(usuarioId: String): Flow<List<CarritoEntity>>

    @Query("SELECT * FROM carrito WHERE usuarioId = :usuarioId AND productoId = :productoId LIMIT 1")
    suspend fun obtenerProductoEnCarrito(usuarioId: String, productoId: Int): CarritoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(carritoItem: CarritoEntity)

    @Update
    suspend fun actualizarProducto(carritoItem: CarritoEntity)

    @Query("UPDATE carrito SET cantidad = :cantidad WHERE id = :id")
    suspend fun actualizarCantidad(id: Int, cantidad: Int)

    @Delete
    suspend fun eliminarProducto(carritoItem: CarritoEntity)

    @Query("DELETE FROM carrito WHERE id = :id")
    suspend fun eliminarProductoPorId(id: Int)

    @Query("SELECT * FROM carrito WHERE id = :id LIMIT 1")
    suspend fun obtenerProductoEnCarritoPorId(id: Int): CarritoEntity?

    @Query("DELETE FROM carrito WHERE usuarioId = :usuarioId")
    suspend fun vaciarCarrito(usuarioId: String)
}
