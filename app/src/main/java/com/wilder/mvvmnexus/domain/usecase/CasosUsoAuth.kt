package com.wilder.mvvmnexus.domain.usecase

import com.wilder.mvvmnexus.domain.repository.RepositorioAuth
import com.wilder.mvvmnexus.domain.model.Usuario
import com.wilder.mvvmnexus.domain.model.ResultadoAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

 //casos de uso
class CasosUsoAuth(private val repositorio: RepositorioAuth) {
    


    //Valida los datos antes de proceder

    suspend fun iniciarSesionEmail(email: String, password: String): ResultadoAuth<Usuario> {

        when {
            email.isBlank() -> return ResultadoAuth.Error("El email es requerido")
            !esEmailValido(email) -> return ResultadoAuth.Error("El formato del email es inválido")
            password.isBlank() -> return ResultadoAuth.Error("La contraseña es requerida")
            password.length < 6 -> return ResultadoAuth.Error("La contraseña debe tener al menos 6 caracteres")
        }
        
        return repositorio.login(email.trim(), password)
    }
    


     // Valida los datos y crea la cuenta

    suspend fun registrarUsuario(email: String, password: String, confirmarPassword: String): ResultadoAuth<Usuario> {

        when {
            email.isBlank() -> return ResultadoAuth.Error("El email es requerido")
            !esEmailValido(email) -> return ResultadoAuth.Error("El formato del email es inválido")
            password.isBlank() -> return ResultadoAuth.Error("La contraseña es requerida")
            password.length < 6 -> return ResultadoAuth.Error("La contraseña debe tener al menos 6 caracteres")
            password != confirmarPassword -> return ResultadoAuth.Error("Las contraseñas no coinciden")
            !esPasswordSegura(password) -> return ResultadoAuth.Error("La contraseña debe tener al menos una mayúscula, una minúscula y un número")
        }
        
        return repositorio.registro(nombre = "", email = email.trim(), pass = password) // Nombre temporal vacío
    }

    suspend fun iniciarSesionGoogle(idToken: String): ResultadoAuth<Usuario> {
        if (idToken.isBlank()) {
            return ResultadoAuth.Error("Token de Google inválido")
        }
        return repositorio.loginConGoogle(idToken)
    }

    suspend fun cerrarSesion(): ResultadoAuth<Unit> {
        repositorio.cerrarSesion()
        return ResultadoAuth.Exito(Unit)
    }

    suspend fun obtenerUsuarioActual(): Usuario? {
        return repositorio.obtenerUsuarioActual()
    }

    fun observarEstadoAuth(): Flow<Usuario?> {

        return repositorio.estadoAuth.map { estado ->
            if (estado is com.wilder.mvvmnexus.domain.model.EstadoAuth.Autenticado) estado.usuario else null
        }
    }
    
    
    suspend fun restablecerPassword(email: String): ResultadoAuth<Unit> {
        if (email.isBlank()) return ResultadoAuth.Error("El email es requerido")
        if (!esEmailValido(email)) return ResultadoAuth.Error("El formato del email es inválido")
        
        return repositorio.enviarCorreoRestablecimiento(email)
    }
    



    suspend fun guardarUsuarioLocal(usuario: Usuario) {
        repositorio.guardarUsuarioLocal(usuario)
    }



    private fun esEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    private fun esPasswordSegura(password: String): Boolean {
        val tieneMinuscula = password.any { it.isLowerCase() }
        val tieneMayuscula = password.any { it.isUpperCase() }
        val tieneNumero = password.any { it.isDigit() }
        
        return tieneMinuscula && tieneMayuscula && tieneNumero
    }
}