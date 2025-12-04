package com.wilder.mvvmnexus.domain.usecase

import com.wilder.mvvmnexus.domain.model.Producto
import com.wilder.mvvmnexus.domain.repository.ProductoRepository

class ObtenerDetalleProductoUseCase(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int): Producto? {
        return repository.obtenerProductoPorId(id)
    }
}
