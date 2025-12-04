package com.wilder.mvvmnexus.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wilder.mvvmnexus.data.database.dao.CarritoDao
import com.wilder.mvvmnexus.data.database.dao.FavoritoDao
import com.wilder.mvvmnexus.data.database.dao.UsuarioDao
import com.wilder.mvvmnexus.data.database.entities.CarritoEntity
import com.wilder.mvvmnexus.data.database.entities.FavoritoEntity
import com.wilder.mvvmnexus.data.database.entities.UsuarioEntity

@Database(entities = [UsuarioEntity::class, CarritoEntity::class, FavoritoEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun carritoDao(): CarritoDao
    abstract fun favoritoDao(): FavoritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nexus_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
