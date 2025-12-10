package com.wilder.mvvmnexus.domain.model

/**
 * Estados de operaciones de autenticación
 */
sealed class ResultadoAuth<out T> {
    

    data class Exito<T>(val data: T) : ResultadoAuth<T>()
    data class Error(val mensaje: String, val excepcion: Exception? = null) : ResultadoAuth<Nothing>()
    data object Cargando : ResultadoAuth<Nothing>()
}

