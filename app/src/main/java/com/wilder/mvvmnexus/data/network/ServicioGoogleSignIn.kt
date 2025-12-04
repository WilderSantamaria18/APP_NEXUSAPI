package com.wilder.mvvmnexus.data.network

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

/**
 * 🔍 SERVICIO DE GOOGLE SIGN-IN
 * Maneja la configuración y autenticación con Google
 */
class ServicioGoogleSignIn(private val context: Context) {
    
    private val googleSignInClient: GoogleSignInClient
    
    init {
        // Configurar Google Sign-In con selección de cuenta forzada
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(obtenerWebClientId())  // 🔑 Client ID del servidor web
            .requestEmail()                        // 📧 Solicitar email
            .requestProfile()                      // 👤 Solicitar perfil completo
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    /**
     * 🔄 Obtener cliente para selección de cuenta (login y registro)
     * Fuerza el selector de cuenta cada vez
     */
    fun obtenerClienteConSeleccionCuenta(): GoogleSignInClient {
        // Cerrar sesión silenciosamente para forzar selección de cuenta
        googleSignInClient.signOut()
        return googleSignInClient
    }
    
    /**
     * 🎯 Obtener cliente de Google Sign-In
     */
    fun obtenerCliente(): GoogleSignInClient {
        return googleSignInClient
    }
    
    /**
     * 🔍 Obtener cuenta actual de Google
     */
    fun obtenerCuentaActual(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
    
    /**
     * 🚪 Cerrar sesión de Google
     */
    fun cerrarSesion(): Task<Void> {
        return googleSignInClient.signOut()
    }
    
    /**
     * 🗑️ Revocar acceso de Google
     */
    fun revocarAcceso(): Task<Void> {
        return googleSignInClient.revokeAccess()
    }
    
    /**
     * 📝 Manejar resultado del sign-in
     */
    fun manejarResultadoSignIn(task: Task<GoogleSignInAccount>): Result<GoogleSignInAccount> {
        return try {
            val cuenta = task.getResult(ApiException::class.java)
            Result.success(cuenta)
        } catch (e: ApiException) {
            val mensajeError = when (e.statusCode) {
                12501 -> "Sign-in cancelado por el usuario"
                12500 -> "Error interno de Google Sign-In"
                7 -> "Sin conexión a internet"
                else -> "Error de Google Sign-In: ${e.message}"
            }
            Result.failure(Exception(mensajeError))
        }
    }
    
    /**
     * 🔑 Obtener Web Client ID de Firebase
     * IMPORTANTE: Debes configurar Google Sign-In en Firebase Console primero
     */
    private fun obtenerWebClientId(): String {
        // Por ahora usamos un placeholder - debes configurar Google Sign-In en Firebase Console
        // y actualizar el string en strings.xml
        return context.getString(com.wilder.mvvmnexus.R.string.default_web_client_id)
    }
}