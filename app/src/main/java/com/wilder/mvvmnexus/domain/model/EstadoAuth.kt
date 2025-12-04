package com.wilder.mvvmnexus.domain.model

/**
 * Representa los posibles estados de la autenticación de usuario
 */
sealed class EstadoAuth {
    
    /**
     *  Estado de carga - Verificando autenticación
     */
    data object Cargando : EstadoAuth()
    
    /**
     *  Usuario no autenticado
     */
    data object NoAutenticado : EstadoAuth()
    
    /**
     *  Usuario autenticado correctamente
     */
    data class Autenticado(val usuario: Usuario) : EstadoAuth()
    
    /**
     *  Error en la autenticación
     */
    data class Error(val mensaje: String, val codigo: String = "") : EstadoAuth()
}