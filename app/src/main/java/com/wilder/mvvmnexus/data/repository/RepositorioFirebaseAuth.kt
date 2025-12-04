package com.wilder.mvvmnexus.data.repository

import com.google.firebase.auth.*
import com.wilder.mvvmnexus.data.database.dao.UsuarioDao
import com.wilder.mvvmnexus.data.database.entities.UsuarioEntity
import com.wilder.mvvmnexus.domain.model.EstadoAuth
import com.wilder.mvvmnexus.domain.model.ResultadoAuth
import com.wilder.mvvmnexus.domain.model.Usuario
import com.wilder.mvvmnexus.domain.repository.RepositorioAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

/**
 * IMPLEMENTACIÓN FIREBASE DEL REPOSITORIO
 * Maneja toda la lógica de autenticación con Firebase y persistencia local con Room
 */
class RepositorioFirebaseAuth(
    private val usuarioDao: UsuarioDao
) : RepositorioAuth {
    
    // Instancia de Firebase Auth
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    
    // Implementación de estadoAuth
    override val estadoAuth: Flow<EstadoAuth> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val usuario = auth.currentUser?.aUsuario()
            if (usuario != null) {
                trySend(EstadoAuth.Autenticado(usuario))
            } else {
                trySend(EstadoAuth.NoAutenticado)
            }
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }
    
    /**
     *  Iniciar sesión con email y contraseña
     */
    override suspend fun login(email: String, pass: String): ResultadoAuth<Usuario> {
        return try {
            val resultado = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            val usuarioFirebase = resultado.user
            
            if (usuarioFirebase != null) {
                ResultadoAuth.Exito(usuarioFirebase.aUsuario())
            } else {
                ResultadoAuth.Error("No se pudo iniciar sesión")
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            ResultadoAuth.Error("El usuario no existe")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            ResultadoAuth.Error("Credenciales inválidas")
        } catch (e: FirebaseAuthException) {
            ResultadoAuth.Error("Error de autenticación: ${e.message}")
        } catch (e: Exception) {
            ResultadoAuth.Error("Error inesperado: ${e.message}")
        }
    }
    
    /**
     *  Registrarse con email y contraseña
     */
    override suspend fun registro(nombre: String, email: String, pass: String): ResultadoAuth<Usuario> {
        return try {
            val resultado = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val usuarioFirebase = resultado.user
            
            if (usuarioFirebase != null) {
                // Actualizar nombre de usuario
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(nombre)
                    .build()
                usuarioFirebase.updateProfile(profileUpdates).await()
                
                ResultadoAuth.Exito(usuarioFirebase.aUsuario())
            } else {
                ResultadoAuth.Error("No se pudo crear la cuenta")
            }
        } catch (e: FirebaseAuthWeakPasswordException) {
            ResultadoAuth.Error("La contraseña es muy débil")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            ResultadoAuth.Error("Email inválido")
        } catch (e: FirebaseAuthUserCollisionException) {
            ResultadoAuth.Error("Ya existe una cuenta con este email")
        } catch (e: FirebaseAuthException) {
            ResultadoAuth.Error("Error al crear cuenta: ${e.message}")
        } catch (e: Exception) {
            ResultadoAuth.Error("Error inesperado: ${e.message}")
        }
    }
    
    /**
     *  Iniciar sesión con Google
     */
    override suspend fun loginConGoogle(idToken: String): ResultadoAuth<Usuario> {
        return try {
            val credencial = GoogleAuthProvider.getCredential(idToken, null)
            val resultado = firebaseAuth.signInWithCredential(credencial).await()
            val usuarioFirebase = resultado.user
            
            if (usuarioFirebase != null) {
                ResultadoAuth.Exito(usuarioFirebase.aUsuario())
            } else {
                ResultadoAuth.Error("No se pudo iniciar sesión con Google")
            }
        } catch (e: FirebaseAuthException) {
            ResultadoAuth.Error("Error con Google Sign-In: ${e.message}")
        } catch (e: Exception) {
            ResultadoAuth.Error("Error inesperado con Google: ${e.message}")
        }
    }
    
    /**
     * 🚪 Cerrar sesión
     */
    override suspend fun cerrarSesion(): Unit {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {

        }
    }
    
    /**
     *  Obtener usuario actualmente autenticado
     */
    override suspend fun obtenerUsuarioActual(): Usuario? {
        return firebaseAuth.currentUser?.aUsuario()
    }
    
    /**
     *  Obtener usuario local (Room)
     */
    override fun obtenerUsuarioLocal(uid: String): Flow<Usuario?> {
        return usuarioDao.obtenerUsuarioPorId(uid).map { entity ->
            entity?.let {
                Usuario(
                    uid = it.id,
                    nombreCompleto = it.nombre,
                    email = it.email,
                    fotoUrl = it.fotoUrl ?: "",
                    emailVerificado = true, // Asumimos verificado si está en local
                    proveedor = "local"
                )
            }
        }
    }

    /**
     * 💾 Guardar usuario local (Room)
     */
    override suspend fun guardarUsuarioLocal(usuario: Usuario) {
        val entity = UsuarioEntity(
            id = usuario.uid,
            nombre = usuario.nombreCompleto,
            email = usuario.email,
            fotoUrl = usuario.fotoUrl
        )
        usuarioDao.insertarUsuario(entity)
    }
    
    /**
     * 🔄 FUNCIÓN DE EXTENSIÓN
     * Convierte FirebaseUser a Usuario del dominio
     */
    private fun FirebaseUser.aUsuario(): Usuario {
        return Usuario(
            uid = this.uid,
            email = this.email ?: "",
            nombreCompleto = this.displayName ?: "",
            fotoUrl = this.photoUrl?.toString() ?: "",
            emailVerificado = this.isEmailVerified,
            proveedor = when {
                this.providerData.any { it.providerId == "google.com" } -> "google"
                else -> "firebase"
            }
        )
    }
}