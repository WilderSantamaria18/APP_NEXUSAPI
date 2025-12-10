package com.wilder.mvvmnexus.domain.model


 // Representa la información de un usuario

data class Usuario(
    val uid: String = "",
    val email: String = "",
    val nombreCompleto: String = "",
    val fotoUrl: String = "",
    val emailVerificado: Boolean = false,
    val proveedor: String = ""
) {
    //obtiene el nombre a mostrar en el perfil
    fun obtenerNombreMostrar(): String {
        return if (nombreCompleto.isNotEmpty()) {
            nombreCompleto
        } else {
            email.substringBefore("@")
        }
    }
    
   //usuario autenticado
    fun estaAutenticado(): Boolean {
        return uid.isNotEmpty() && email.isNotEmpty()
    }
}