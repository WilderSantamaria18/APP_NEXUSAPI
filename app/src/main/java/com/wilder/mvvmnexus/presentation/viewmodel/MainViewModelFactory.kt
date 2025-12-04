package com.wilder.mvvmnexus.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wilder.mvvmnexus.data.repository.ProductoRepositoryImpl
import com.wilder.mvvmnexus.domain.usecase.ObtenerProductosUseCase

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val repository = ProductoRepositoryImpl()
            val useCase = ObtenerProductosUseCase(repository)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}