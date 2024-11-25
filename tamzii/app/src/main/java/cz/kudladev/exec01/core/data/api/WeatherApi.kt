package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.domain.dto.weather.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("current") currentParameters: String = "is_day", // Default value for current
        @Query("hourly") hourlyParameters: String = "temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,weather_code,wind_speed_10m", // Default value for hourly
        @Query("daily") dailyParameters: String = "uv_index_max,weather_code,temperature_2m_max,temperature_2m_min", // Added daily parameter
        @Query("timezone") timezone: String = "auto"
    ): Weather


    companion object {
        const val BASE_URL = "https://api.open-meteo.com/"
    }

}