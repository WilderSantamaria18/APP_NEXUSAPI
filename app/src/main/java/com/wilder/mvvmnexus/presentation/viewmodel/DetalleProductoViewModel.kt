package com.wilder.mvvmnexus.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.usecase.ObtenerDetalleProductoUseCase
import kotlinx.coroutines.launch

import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito

class DetalleProductoViewModel(
    private val obtenerDetalleProductoUseCase: ObtenerDetalleProductoUseCase,
    private val casosUsoCarrito: CasosUsoCarrito,
    private val casosUsoAuth: CasosUsoAuth
) : ViewModel() {

    private val _mensajeCarrito = MutableLiveData<String?>()
    val mensajeCarrito: LiveData<String?> = _mensajeCarrito

    private val _producto = MutableLiveData<Producto?>()
    val producto: LiveData<Producto?> = _producto

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun cargarProducto(id: Int) {
        Log.d("DetalleProductoVM", "Cargando producto con ID: $id")
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val producto = obtenerDetalleProductoUseCase(id)
                if (producto != null) {
                    _producto.value = producto
                    Log.d("DetalleProductoVM", "Producto cargado: ${producto.titulo}")
                } else {
                    _error.value = "No se pudo cargar el producto"
                    Log.e("DetalleProductoVM", "Producto null")
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("DetalleProductoVM", "Error cargando producto", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun agregarAlCarrito(producto: Producto) {
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            if (usuario != null) {
                try {
                    casosUsoCarrito.agregarProducto(usuario.uid, producto, 1)
                    _mensajeCarrito.value = "Producto agregado al carrito"
                } catch (e: Exception) {
                    _mensajeCarrito.value = "Error al agregar al carrito: ${e.message}"
                }
            } else {
                _mensajeCarrito.value = "Debes iniciar sesión para agregar al carrito"
            }
        }
    }
    
    fun limpiarMensajeCarrito() {
        _mensajeCarrito.value = null
    }
}
