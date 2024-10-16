package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.domain.dto.location.GeocodeReverseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApi {


    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") accessToken: String = TOKEN
    ) : GeocodeReverseResponse


    companion object {
        const val TOKEN = "pk.eyJ1Ijoia3VkbDFrIiwiYSI6ImNtMmJ1bHJoMTBweGYya3F3MXZzaWY4MjQifQ.H6srO2K-LkW6A53Ba37duw"
        const val BASE_URL = "https://api.mapbox.com/"
    }
}