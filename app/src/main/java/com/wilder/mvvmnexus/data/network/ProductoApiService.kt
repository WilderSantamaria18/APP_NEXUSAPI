package com.wilder.mvvmnexus.data.network

import com.wilder.mvvmnexus.data.model.ProductoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductoApiService {
    @GET("products")
    suspend fun obtenerProductos(): Response<List<ProductoModel>>
    
    @GET("products/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: Int): Response<ProductoModel>
}