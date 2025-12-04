package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilder.mvvmnexus.domain.model.CarritoItem
import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val casosUsoCarrito: CasosUsoCarrito,
    private val casosUsoAuth: CasosUsoAuth
) : ViewModel() {

    private val _carrito = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carrito: StateFlow<List<CarritoItem>> = _carrito.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    private val _cantidadProductos = MutableStateFlow(0)
    val cantidadProductos: StateFlow<Int> = _cantidadProductos.asStateFlow()

    init {
        observarCarrito()
    }

    private fun observarCarrito() {
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            if (usuario != null) {
                casosUsoCarrito.obtenerCarrito(usuario.uid).collect { items ->
                    _carrito.value = items
                    _total.value = items.sumOf { it.total }
                    _cantidadProductos.value = items.sumOf { it.cantidad }
                }
            }
        }
    }

    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            if (usuario != null) {
                casosUsoCarrito.agregarProducto(usuario.uid, producto, cantidad)
            }
        }
    }

    fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) {
        viewModelScope.launch {
            casosUsoCarrito.actualizarCantidad(item, nuevaCantidad)
        }
    }

    fun eliminarProducto(item: CarritoItem) {
        viewModelScope.launch {
            casosUsoCarrito.eliminarProducto(item)
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            val usuario = casosUsoAuth.obtenerUsuarioActual()
            if (usuario != null) {
                casosUsoCarrito.vaciarCarrito(usuario.uid)
            }
        }
    }
}
