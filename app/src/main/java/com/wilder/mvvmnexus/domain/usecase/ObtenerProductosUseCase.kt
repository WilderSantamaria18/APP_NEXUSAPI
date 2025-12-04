package com.wilder.mvvmnexus.domain.usecase

import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.repository.ProductoRepository

class ObtenerProductosUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(): List<Producto> {
        return repository.obtenerProductos()
    }
}