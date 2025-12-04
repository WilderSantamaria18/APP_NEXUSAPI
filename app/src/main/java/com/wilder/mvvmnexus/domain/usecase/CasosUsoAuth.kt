package com.wilder.mvvmnexus.domain.usecase

import com.wilder.mvvmnexus.domain.repository.RepositorioAuth
import com.wilder.mvvmnexus.domain.model.Usuario
import com.wilder.mvvmnexus.domain.model.ResultadoAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 🎯 CASOS DE USO DE AUTENTICACIÓN
 * Contiene la lógica de negocio para todas las operaciones de auth
 */
class CasosUsoAuth(private val repositorio: RepositorioAuth) {
    
    /**
     * 📧 Iniciar sesión con email
     * Valida los datos antes de proceder
     */
    suspend fun iniciarSesionEmail(email: String, password: String): ResultadoAuth<Usuario> {
        // Validaciones de negocio
        when {
            email.isBlank() -> return ResultadoAuth.Error("El email es requerido")
            !esEmailValido(email) -> return ResultadoAuth.Error("El formato del email es inválido")
            password.isBlank() -> return ResultadoAuth.Error("La contraseña es requerida")
            password.length < 6 -> return ResultadoAuth.Error("La contraseña debe tener al menos 6 caracteres")
        }
        
        return repositorio.login(email.trim(), password)
    }
    
    /**
     * 📝 Registrar nuevo usuario
     * Valida los datos y crea la cuenta
     */
    suspend fun registrarUsuario(email: String, password: String, confirmarPassword: String): ResultadoAuth<Usuario> {
        // Validaciones de negocio
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
    
    /**
     * 🔍 Iniciar sesión con Google
     */
    suspend fun iniciarSesionGoogle(idToken: String): ResultadoAuth<Usuario> {
        if (idToken.isBlank()) {
            return ResultadoAuth.Error("Token de Google inválido")
        }
        return repositorio.loginConGoogle(idToken)
    }
    
    /**
     * 🚪 Cerrar sesión
     */
    suspend fun cerrarSesion(): ResultadoAuth<Unit> {
        repositorio.cerrarSesion()
        return ResultadoAuth.Exito(Unit)
    }
    
    /**
     * 👤 Obtener usuario actual
     */
    suspend fun obtenerUsuarioActual(): Usuario? {
        return repositorio.obtenerUsuarioActual()
    }
    
    /**
     * 🔄 Observar cambios en el estado de autenticación
     */
    fun observarEstadoAuth(): Flow<Usuario?> {
        // Transformar EstadoAuth a Usuario?
        return repositorio.estadoAuth.map { estado ->
            if (estado is com.wilder.mvvmnexus.domain.model.EstadoAuth.Autenticado) estado.usuario else null
        }
    }
    
    /**
     * 🔑 Restablecer contraseña
     */
    suspend fun restablecerPassword(email: String): ResultadoAuth<Unit> {
        when {
            email.isBlank() -> return ResultadoAuth.Error("El email es requerido")
            !esEmailValido(email) -> return ResultadoAuth.Error("El formato del email es inválido")
        }
        
        // Asumiendo que existe un método para esto, si no, se puede omitir o agregar al repo
        // Por ahora retornamos error no implementado o éxito simulado
        return ResultadoAuth.Error("Funcionalidad no disponible temporalmente")
    }
    
    /**
     * ✉️ Reenviar verificación de email
     */
    suspend fun reenviarVerificacionEmail(): ResultadoAuth<Unit> {
        // Asumiendo que existe un método para esto
        return ResultadoAuth.Error("Funcionalidad no disponible temporalmente")
    }

    /**
     * 💾 Guardar usuario localmente
     */
    suspend fun guardarUsuarioLocal(usuario: Usuario) {
        repositorio.guardarUsuarioLocal(usuario)
    }

    /**
     * 💾 Obtener usuario local
     */
    fun obtenerUsuarioLocal(uid: String): Flow<Usuario?> {
        return repositorio.obtenerUsuarioLocal(uid)
    }
    
    /**
     * 🛠️ FUNCIONES DE VALIDACIÓN PRIVADAS
     */
    
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