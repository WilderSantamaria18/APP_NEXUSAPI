package com.wilder.mvvmnexus.domain.repository

import com.wilder.mvvmnexus.domain.model.EstadoAuth
import com.wilder.mvvmnexus.domain.model.ResultadoAuth
import com.wilder.mvvmnexus.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface RepositorioAuth {
    val estadoAuth: Flow<EstadoAuth>
    
    suspend fun login(email: String, pass: String): ResultadoAuth<Usuario>
    suspend fun registro(nombre: String, email: String, pass: String): ResultadoAuth<Usuario>
    suspend fun loginConGoogle(idToken: String): ResultadoAuth<Usuario>
    suspend fun cerrarSesion()
    
    // Obtener usuario actualmente autenticado
    suspend fun obtenerUsuarioActual(): Usuario?
    
    // Métodos para persistencia local del perfil

    suspend fun guardarUsuarioLocal(usuario: Usuario)
    
    // Recuperación de contraseña
    suspend fun enviarCorreoRestablecimiento(email: String): ResultadoAuth<Unit>
}
