package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.domain.dto.store.Product
import retrofit2.http.GET

interface StoreApi {

    @GET("products")
    suspend fun getProducts(): List<Product>

    companion object{
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}