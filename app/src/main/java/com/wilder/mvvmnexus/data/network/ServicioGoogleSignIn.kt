package com.wilder.mvvmnexus.data.network

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

@Suppress("DEPRECATION")
class ServicioGoogleSignIn(private val context: Context) {
    
    private val googleSignInClient: GoogleSignInClient
    
    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(obtenerWebClientId())
            .requestEmail()
            .requestProfile()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun obtenerClienteParaLogin(): GoogleSignInClient {
        googleSignInClient.signOut()
        return googleSignInClient
    }

    fun manejarResultadoSignIn(task: Task<GoogleSignInAccount>): Result<GoogleSignInAccount> {
        return try {
            val cuenta = task.getResult(ApiException::class.java)
            Result.success(cuenta)
        } catch (e: ApiException) {
            val mensaje = when (e.statusCode) {
                12501 -> "Cancelado por el usuario"
                12500 -> "Error interno"
                7 -> "Sin conexión"
                else -> "Error: ${e.message}"
            }
            Result.failure(Exception(mensaje))
        }
    }

    private fun obtenerWebClientId(): String {
        return context.getString(com.wilder.mvvmnexus.R.string.default_web_client_id)
    }
}