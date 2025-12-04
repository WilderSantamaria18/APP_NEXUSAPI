package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoCarrito

class CartViewModelFactory(
    private val casosUsoCarrito: CasosUsoCarrito,
    private val casosUsoAuth: CasosUsoAuth
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(casosUsoCarrito, casosUsoAuth) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
