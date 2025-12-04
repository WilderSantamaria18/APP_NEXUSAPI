package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wilder.mvvmnexus.data.network.ProductoApiClient
import com.wilder.mvvmnexus.data.repository.ProductoRepositoryImpl
import com.wilder.mvvmnexus.domain.usecase.ObtenerDetalleProductoUseCase

import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito

class DetalleProductoViewModelFactory(
    private val casosUsoCarrito: CasosUsoCarrito,
    private val casosUsoAuth: CasosUsoAuth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleProductoViewModel::class.java)) {
            val repository = ProductoRepositoryImpl()
            val useCase = ObtenerDetalleProductoUseCase(repository)
            @Suppress("UNCHECKED_CAST")
            return DetalleProductoViewModel(useCase, casosUsoCarrito, casosUsoAuth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
