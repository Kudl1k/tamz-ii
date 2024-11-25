package cz.kudladev.tamziikmp.weather.domain

import cz.kudladev.tamziikmp.weather.data.dto.WeatherResponse

interface WeatherClient {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): WeatherResponse
}