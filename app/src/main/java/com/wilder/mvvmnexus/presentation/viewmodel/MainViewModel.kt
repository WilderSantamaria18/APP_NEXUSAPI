package com.wilder.mvvmnexus.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.usecase.ObtenerProductosUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val obtenerProductosUseCase: ObtenerProductosUseCase
) : ViewModel() {

    private val _productos = MutableLiveData<List<Producto>>()
    val productos: LiveData<List<Producto>> = _productos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun cargarProductos() {
        Log.d("MainViewModel", "Iniciando carga de productos...")
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("MainViewModel", "Llamando al use case...")
                val productos = obtenerProductosUseCase()
                Log.d("MainViewModel", "Productos obtenidos: ${productos.size}")
                _productos.value = productos
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error al cargar productos", e)
                _error.value = "Error al cargar los productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}