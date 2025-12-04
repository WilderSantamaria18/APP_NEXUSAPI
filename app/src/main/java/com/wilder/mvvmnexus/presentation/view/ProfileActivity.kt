package com.wilder.mvvmnexus.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wilder.mvvmnexus.data.database.AppDatabase
import com.wilder.mvvmnexus.data.repository.RepositorioFirebaseAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.presentation.compose.screens.ProfileScreen
import com.wilder.mvvmnexus.presentation.compose.theme.MvvmNexusTheme
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModelFactory
import com.wilder.mvvmnexus.utils.ThemeManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class ProfileActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val usuarioDao = database.usuarioDao()
        val carritoDao = database.carritoDao()
        
        val repositorioAuth = RepositorioFirebaseAuth(usuarioDao)
        val casosUsoAuth = CasosUsoAuth(repositorioAuth)
        
        val repositorioCarrito = com.wilder.mvvmnexus.data.repository.RepositorioCarritoImpl(carritoDao)
        val casosUsoCarrito = com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito(repositorioCarrito)
        
        authViewModel = androidx.lifecycle.ViewModelProvider(this, AuthViewModelFactory(casosUsoAuth, casosUsoCarrito)).get(AuthViewModel::class.java)

        setContent {
            val isDarkTheme by ThemeManager.isDarkTheme.collectAsState()
            MvvmNexusTheme(darkTheme = isDarkTheme) {
                ProfileScreen(
                    viewModel = authViewModel,
                    onBackClick = { finish() }
                )
            }
        }
    }
}
