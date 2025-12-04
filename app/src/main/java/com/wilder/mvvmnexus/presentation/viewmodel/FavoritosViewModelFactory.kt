package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wilder.mvvmnexus.domain.usecase.CasosUsoAuth
import com.wilder.mvvmnexus.domain.usecase.CasosUsoFavoritos

class FavoritosViewModelFactory(
    private val casosUsoFavoritos: CasosUsoFavoritos,
    private val casosUsoAuth: CasosUsoAuth
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritosViewModel(casosUsoFavoritos, casosUsoAuth) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
