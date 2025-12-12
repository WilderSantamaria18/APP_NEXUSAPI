package com.wilder.mvvmnexus.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.wilder.mvvmnexus.presentation.compose.screens.ProductListScreen
import com.wilder.mvvmnexus.presentation.compose.screens.SettingsScreen
import com.wilder.mvvmnexus.presentation.compose.theme.MvvmNexusTheme
import com.wilder.mvvmnexus.presentation.viewmodel.MainViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.MainViewModelFactory
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.wilder.mvvmnexus.data.database.AppDatabase
import com.wilder.mvvmnexus.data.repository.RepositorioFirebaseAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.AuthViewModelFactory
import com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.CartViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }
    private val themeViewModel: com.wilder.mvvmnexus.presentation.viewmodel.ThemeViewModel by viewModels()
    private lateinit var authViewModel: AuthViewModel
    private lateinit var cartViewModel: CartViewModel

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
        cartViewModel = androidx.lifecycle.ViewModelProvider(this, CartViewModelFactory(casosUsoCarrito, casosUsoAuth)).get(CartViewModel::class.java)
        
        val favoritoDao = database.favoritoDao()
        val repositorioFavoritos = com.wilder.mvvmnexus.data.repository.RepositorioFavoritosImpl(favoritoDao)
        val casosUsoFavoritos = com.wilder.mvvmnexus.domain.usecase.CasosUsoFavoritos(repositorioFavoritos)
        val favoritosViewModel = androidx.lifecycle.ViewModelProvider(this, com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModelFactory(casosUsoFavoritos, casosUsoAuth)).get(com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModel::class.java)
        
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = true)
            MvvmNexusTheme(darkTheme = isDarkTheme) {
                var currentScreen by remember { mutableStateOf("home") }

                if (currentScreen == "home") {
                    ProductListScreen(
                        viewModel = viewModel,
                        authViewModel = authViewModel,
                        cartViewModel = cartViewModel,
                        favoritosViewModel = favoritosViewModel,
                        themeViewModel = themeViewModel,
                        onProductClick = { productoId ->
                            val intent = Intent(this, DetalleProductoActivity::class.java).apply {
                                putExtra(DetalleProductoActivity.EXTRA_PRODUCTO_ID, productoId)
                            }
                            startActivity(intent)
                        },
                        onNavigateToProfile = {
                            startActivity(Intent(this, ProfileActivity::class.java))
                        },
                        onNavigateToFavorites = {
                            startActivity(Intent(this, FavoritosActivity::class.java))
                        },
                        onNavigateToCart = {
                            startActivity(Intent(this, CartActivity::class.java))
                        },
                        onLogout = {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },
                        onNavigateToSettings = {
                            currentScreen = "settings"
                        }
                    )
                } else {
                    SettingsScreen(
                        themeViewModel = themeViewModel,
                        onBack = { currentScreen = "home" }
                    )
                }
            }
        }
    }
}
