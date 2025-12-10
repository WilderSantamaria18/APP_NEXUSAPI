package com.wilder.mvvmnexus.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.wilder.mvvmnexus.data.network.ServicioGoogleSignIn
import com.wilder.mvvmnexus.data.repository.RepositorioFirebaseAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.presentation.compose.screens.RegisterScreen
import com.wilder.mvvmnexus.presentation.compose.theme.MvvmNexusTheme
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModelFactory

@Suppress("DEPRECATION")
class RegistroActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var servicioGoogle: ServicioGoogleSignIn

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val resultado = servicioGoogle.manejarResultadoSignIn(task)
        
        if (resultado.isSuccess) {
            val cuenta = resultado.getOrNull()
            val idToken = cuenta?.idToken
            if (idToken != null) {
                authViewModel.iniciarSesionConGoogle(idToken)
            } else {
                Toast.makeText(this, "Error: No se pudo obtener token de Google", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Error con Google Sign-In: ${resultado.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configurarViewModel()
        servicioGoogle = ServicioGoogleSignIn(this)

        setContent {
            MvvmNexusTheme {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = {
                        Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                    },
                    onNavigateBack = {
                        finish()
                    },
                    onGoogleSignInClick = {
                        val signInIntent = servicioGoogle.obtenerClienteParaLogin().signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    }
                )
            }
        }
    }

    private fun configurarViewModel() {
        val database = com.wilder.mvvmnexus.data.database.AppDatabase.getDatabase(applicationContext)
        val usuarioDao = database.usuarioDao()
        val carritoDao = database.carritoDao()
        
        val repositorioAuth = RepositorioFirebaseAuth(usuarioDao)
        val casosUsoAuth = CasosUsoAuth(repositorioAuth)
        
        val repositorioCarrito = com.wilder.mvvmnexus.data.repository.RepositorioCarritoImpl(carritoDao)
        val casosUsoCarrito = com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito(repositorioCarrito)
        
        authViewModel = ViewModelProvider(this, AuthViewModelFactory(casosUsoAuth, casosUsoCarrito)).get(AuthViewModel::class.java)
    }
}
