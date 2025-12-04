package com.wilder.mvvmnexus.presentation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.wilder.mvvmnexus.presentation.compose.screens.ProductDetailScreen
import com.wilder.mvvmnexus.presentation.compose.theme.MvvmNexusTheme
import com.wilder.mvvmnexus.presentation.viewmodel.DetalleProductoViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.DetalleProductoViewModelFactory
import com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModelFactory
import com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel
import com.wilder.mvvmnexus.utils.ThemeManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class DetalleProductoActivity : ComponentActivity() {

    companion object {
        const val EXTRA_PRODUCTO_ID = "producto_id"
    }

    private lateinit var viewModel: DetalleProductoViewModel
    private lateinit var favoritosViewModel: FavoritosViewModel
    private lateinit var cartViewModel: com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productoId = intent.getIntExtra(EXTRA_PRODUCTO_ID, -1)
        
        if (productoId == -1) {
            Toast.makeText(this, "Error: ID de producto inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        configurarViewModel()

        viewModel.mensajeCarrito.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.limpiarMensajeCarrito()
            }
        }

        setContent {
            val isDarkTheme by ThemeManager.isDarkTheme.collectAsState()
            MvvmNexusTheme(darkTheme = isDarkTheme) {
                ProductDetailScreen(
                    viewModel = viewModel,
                    favoritosViewModel = favoritosViewModel,
                    cartViewModel = cartViewModel,
                    productoId = productoId,
                    onBackClick = { finish() },
                    onNavigateToCart = {
                        val intent = android.content.Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    },
                    onAddToCart = { producto ->
                        viewModel.agregarAlCarrito(producto)
                    }
                )
            }
        }
    }

    private fun configurarViewModel() {
        val database = com.wilder.mvvmnexus.data.database.AppDatabase.getDatabase(applicationContext)
        val usuarioDao = database.usuarioDao()
        val carritoDao = database.carritoDao()
        val favoritoDao = database.favoritoDao()
        
        val repositorioCarrito = com.wilder.mvvmnexus.data.repository.RepositorioCarritoImpl(carritoDao)
        val casosUsoCarrito = com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito(repositorioCarrito)
        
        val repositorioAuth = com.wilder.mvvmnexus.data.repository.RepositorioFirebaseAuth(usuarioDao)
        val casosUsoAuth = com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth(repositorioAuth)
        
        val repositorioFavoritos = com.wilder.mvvmnexus.data.repository.RepositorioFavoritosImpl(favoritoDao)
        val casosUsoFavoritos = com.wilder.mvvmnexus.domain.usecase.CasosUsoFavoritos(repositorioFavoritos)
        
        val factory = DetalleProductoViewModelFactory(casosUsoCarrito, casosUsoAuth)
        viewModel = androidx.lifecycle.ViewModelProvider(this, factory).get(DetalleProductoViewModel::class.java)
        
        val favoritosFactory = FavoritosViewModelFactory(casosUsoFavoritos, casosUsoAuth)
        favoritosViewModel = androidx.lifecycle.ViewModelProvider(this, favoritosFactory).get(FavoritosViewModel::class.java)

        val cartFactory = com.wilder.mvvmnexus.presentation.viewmodel.CartViewModelFactory(casosUsoCarrito, casosUsoAuth)
        cartViewModel = androidx.lifecycle.ViewModelProvider(this, cartFactory).get(com.wilder.mvvmnexus.presentation.viewmodel.CartViewModel::class.java)
    }
}
