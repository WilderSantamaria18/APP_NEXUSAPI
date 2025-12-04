package com.wilder.mvvmnexus.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductoApiClient {
    private const val BASE_URL = "https://fakestoreapi.com/"
    
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }
}