package cz.kudladev.tamziikmp.weather.domain.usecase

import cz.kudladev.tamziikmp.core.util.Resource
import cz.kudladev.tamziikmp.weather.data.dto.current.WeatherCurrentResponse
import cz.kudladev.tamziikmp.weather.domain.WeatherClient

class CurrentWeather(
    private val weatherClient: WeatherClient
) {
    suspend fun execute(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherCurrentResponse>{
        return try {
            val response = weatherClient.getCurrentWeather(latitude, longitude)
            println("WeatherResponse: $response")
            Resource.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}