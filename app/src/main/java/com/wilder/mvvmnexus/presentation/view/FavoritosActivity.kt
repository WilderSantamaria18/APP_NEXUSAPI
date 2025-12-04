package com.wilder.mvvmnexus.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wilder.mvvmnexus.data.database.AppDatabase
import com.wilder.mvvmnexus.data.repository.RepositorioFirebaseAuth
import com.wilder.mvvmnexus.data.repository.RepositorioFavoritosImpl
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoFavoritos
import com.wilder.mvvmnexus.presentation.compose.screens.FavoritosScreen
import com.wilder.mvvmnexus.presentation.compose.theme.MvvmNexusTheme
import com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModel
import com.wilder.mvvmnexus.presentation.viewmodel.FavoritosViewModelFactory
import com.wilder.mvvmnexus.utils.ThemeManager
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class FavoritosActivity : ComponentActivity() {

    private lateinit var favoritosViewModel: FavoritosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val usuarioDao = database.usuarioDao()
        val favoritoDao = database.favoritoDao()
        
        val repositorioAuth = RepositorioFirebaseAuth(usuarioDao)
        val casosUsoAuth = CasosUsoAuth(repositorioAuth)
        
        val repositorioFavoritos = RepositorioFavoritosImpl(favoritoDao)
        val casosUsoFavoritos = CasosUsoFavoritos(repositorioFavoritos)
        
        favoritosViewModel = androidx.lifecycle.ViewModelProvider(
            this, 
            FavoritosViewModelFactory(casosUsoFavoritos, casosUsoAuth)
        ).get(FavoritosViewModel::class.java)

        setContent {
            val isDarkTheme by ThemeManager.isDarkTheme.collectAsState()
            MvvmNexusTheme(darkTheme = isDarkTheme) {
                FavoritosScreen(
                    viewModel = favoritosViewModel,
                    onBackClick = { finish() },
                    onProductClick = { productoId ->
                        val intent = Intent(this, DetalleProductoActivity::class.java).apply {
                            putExtra(DetalleProductoActivity.EXTRA_PRODUCTO_ID, productoId)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
