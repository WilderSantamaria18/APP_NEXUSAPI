package com.wilder.mvvmnexus.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.wilder.mvvmnexus.data.database.AppDatabase
import com.wilder.mvvmnexus.data.repository.RepositorioCarritoImpl
import com.wilder.mvvmnexus.data.repository.RepositorioFirebaseAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito
import com.wilder.mvvmnexus.presentation.compose.screens.CartScreen
import com.wilder.mvvmnexus.presentation.compose.theme.MvvmNexusTheme
import com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.CartViewModelFactory
import com.wilder.mvvmnexus.utils.ThemeManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class CartActivity : ComponentActivity() {

    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configurarViewModel()

        setContent {
            val isDarkTheme by ThemeManager.isDarkTheme.collectAsState()
            MvvmNexusTheme(darkTheme = isDarkTheme) {
                CartScreen(
                    viewModel = cartViewModel,
                    onNavigateBack = { finish() }
                )
            }
        }
    }

    private fun configurarViewModel() {
        val database = AppDatabase.getDatabase(applicationContext)
        val carritoDao = database.carritoDao()
        val usuarioDao = database.usuarioDao()
        
        val repositorioCarrito = RepositorioCarritoImpl(carritoDao)
        val casosUsoCarrito = CasosUsoCarrito(repositorioCarrito)
        
        val repositorioAuth = RepositorioFirebaseAuth(usuarioDao)
        val casosUsoAuth = CasosUsoAuth(repositorioAuth)
        
        val factory = CartViewModelFactory(casosUsoCarrito, casosUsoAuth)
        cartViewModel = ViewModelProvider(this, factory).get(CartViewModel::class.java)
    }
}
