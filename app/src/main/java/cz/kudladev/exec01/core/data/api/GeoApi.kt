package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.domain.dto.location.GeocodeReverseResponse
import cz.kudladev.exec01.core.domain.dto.location.searchgeo.GeocodeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.UUID

interface GeoApi {


    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") accessToken: String = TOKEN
    ) : GeocodeReverseResponse


    @GET("search/geocode/v6/forward")
    suspend fun searchSuggestions(
        @Query("q") query: String,
        @Query("language") language: String = "cs", // Default language to Czech
        @Query("proximity") proximity: String? = "-73.990593,40.740121", // Optional proximity
        @Query("session_token") sessionToken: String = UUID.randomUUID().toString(), // Generate a unique session token
        @Query("access_token") accessToken: String = TOKEN
    ): GeocodeSearchResponse

    companion object {
        const val TOKEN = "pk.eyJ1Ijoia3VkbDFrIiwiYSI6ImNtMmJ1bHJoMTBweGYya3F3MXZzaWY4MjQifQ.H6srO2K-LkW6A53Ba37duw"
        const val BASE_URL = "https://api.mapbox.com/"
    }
}