package com.wilder.mvvmnexus.domain.model

/**
 * Envuelve los resultados de las operaciones de autenticación
 */
sealed class ResultadoAuth<out T> {
    
    //operacion exitosa
    data class Exito<T>(val data: T) : ResultadoAuth<T>()
    
    //error en la operacion
    data class Error(val mensaje: String, val excepcion: Exception? = null) : ResultadoAuth<Nothing>()
    
    //operacion en progreso
    data object Cargando : ResultadoAuth<Nothing>()
}

/**
 * 🛠️ EXTENSIONES ÚTILES
 */
fun <T> ResultadoAuth<T>.esExitoso(): Boolean = this is ResultadoAuth.Exito

fun <T> ResultadoAuth<T>.esError(): Boolean = this is ResultadoAuth.Error

fun <T> ResultadoAuth<T>.estaCargando(): Boolean = this is ResultadoAuth.Cargando

fun <T> ResultadoAuth<T>.obtenerDatos(): T? = (this as? ResultadoAuth.Exito)?.data

fun <T> ResultadoAuth<T>.obtenerMensajeError(): String? = (this as? ResultadoAuth.Error)?.mensaje