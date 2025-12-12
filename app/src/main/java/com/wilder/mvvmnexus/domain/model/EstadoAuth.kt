package com.wilder.mvvmnexus.domain.model


//Modelo de estados de la autenticación de usuario

sealed class EstadoAuth {
    data object Cargando : EstadoAuth()

    data object NoAutenticado : EstadoAuth()
    data class Autenticado(val usuario: Usuario) : EstadoAuth()

    data class Error(val mensaje: String, val codigo: String = "") : EstadoAuth()
}