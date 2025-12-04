package com.wilder.mvvmnexus.domain.model


 // Representa la información de un usuario en la aplicación

data class Usuario(
    val uid: String = "",                    // ID único del usuario en Firebase
    val email: String = "",                  // Correo electrónico
    val nombreCompleto: String = "",         // Nombre completo del usuario
    val fotoUrl: String = "",                // URL de la foto de perfil
    val emailVerificado: Boolean = false,    // Si el email está verificado
    val proveedor: String = ""               // "firebase"
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