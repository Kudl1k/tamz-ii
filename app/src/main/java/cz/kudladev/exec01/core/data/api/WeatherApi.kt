package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.domain.dto.weather.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("current") currentParameters: String,
        @Query("hourly") hourlyParameters: String,
        @Query("timezone") timezone: String = "auto",
    ): Weather


    companion object {
        const val BASE_URL = "https://api.open-meteo.com/"
    }

}