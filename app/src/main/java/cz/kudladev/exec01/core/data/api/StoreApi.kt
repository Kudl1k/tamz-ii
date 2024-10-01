package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.domain.dto.Product
import retrofit2.http.GET

interface StoreApi {

    @GET("products")
    suspend fun getProducts(): Product

    companion object{
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}