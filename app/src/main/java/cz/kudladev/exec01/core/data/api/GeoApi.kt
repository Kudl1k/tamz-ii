package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.domain.dto.location.GeocodeReverseResponse
import cz.kudladev.exec01.core.domain.dto.location.retrive.RetrieveResponse
import cz.kudladev.exec01.core.domain.dto.location.searchgeo.GeocodeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface GeoApi {


    @GET("search/geocode/v6/reverse")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") accessToken: String = TOKEN
    ) : GeocodeReverseResponse


    @GET("search/searchbox/v1/suggest")
    suspend fun searchSuggestions(
        @Query("q") query: String,
        @Query("language") language: String = "en", // Default to English
        @Query("limit") limit: Int = 4, // Default to 10 results
        @Query("session_token") sessionToken: String,
        @Query("proximity") proximity: String? = null, // Optional proximity
        @Query("country") country: String? = null, // Optional country code
        @Query("access_token") accessToken: String = TOKEN
    ): GeocodeSearchResponse

    @GET("search/searchbox/v1/retrieve/{mapbox_id}")
    suspend fun retrieveFeatureDetails(
        @Path("mapbox_id") mapboxId: String,
        @Query("session_token") sessionToken: String,
        @Query("access_token") accessToken: String = TOKEN
    ): RetrieveResponse


    companion object {
        const val TOKEN = "pk.eyJ1Ijoia3VkbDFrIiwiYSI6ImNtMmJ1bHJoMTBweGYya3F3MXZzaWY4MjQifQ.H6srO2K-LkW6A53Ba37duw"
        const val BASE_URL = "https://api.mapbox.com/"
    }
}