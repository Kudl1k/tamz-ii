package cz.kudladev.tamziikmp.weather.domain

import cz.kudladev.tamziikmp.weather.data.dto.current.WeatherCurrentResponse

interface WeatherClient {

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): WeatherCurrentResponse
}