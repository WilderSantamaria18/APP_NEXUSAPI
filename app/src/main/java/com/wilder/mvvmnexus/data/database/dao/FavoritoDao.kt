package com.wilder.mvvmnexus.data.database.dao

import androidx.room.*
import com.wilder.mvvmnexus.data.database.entities.FavoritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {
    
    @Query("SELECT * FROM favoritos WHERE usuarioUid = :usuarioUid ORDER BY fechaAgregado DESC")
    fun obtenerFavoritos(usuarioUid: String): Flow<List<FavoritoEntity>>
    
    @Query("SELECT * FROM favoritos WHERE usuarioUid = :usuarioUid AND productoId = :productoId LIMIT 1")
    suspend fun obtenerFavorito(usuarioUid: String, productoId: Int): FavoritoEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE usuarioUid = :usuarioUid AND productoId = :productoId)")
    fun esFavorito(usuarioUid: String, productoId: Int): Flow<Boolean>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarFavorito(favorito: FavoritoEntity)
    
    @Query("DELETE FROM favoritos WHERE usuarioUid = :usuarioUid AND productoId = :productoId")
    suspend fun eliminarFavorito(usuarioUid: String, productoId: Int)
    
    @Query("DELETE FROM favoritos WHERE usuarioUid = :usuarioUid")
    suspend fun eliminarTodosFavoritos(usuarioUid: String)
}
