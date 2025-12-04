package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.model.ProductoFavorito
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoFavoritos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val casosUsoFavoritos: CasosUsoFavoritos,
    private val casosUsoAuth: CasosUsoAuth
) : ViewModel() {
    
    private val _favoritos = MutableStateFlow<List<ProductoFavorito>>(emptyList())
    val favoritos: StateFlow<List<ProductoFavorito>> = _favoritos.asStateFlow()
    
    init {
        cargarFavoritos()
    }
    
    private fun cargarFavoritos() {
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            if (usuario != null) {
                casosUsoFavoritos.obtenerFavoritos(usuario.uid).collect { lista ->
                    _favoritos.value = lista
                }
            }
        }
    }
    
    fun toggleFavorito(producto: Producto) {
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            if (usuario != null) {
                casosUsoFavoritos.toggleFavorito(usuario.uid, producto)
            }
        }
    }
    
    fun esFavorito(productoId: Int): StateFlow<Boolean> {
        val flow = MutableStateFlow(false)
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            if (usuario != null) {
                casosUsoFavoritos.esFavorito(usuario.uid, productoId).collect { esFav ->
                    flow.value = esFav
                }
            }
        }
        return flow.asStateFlow()
    }
}
