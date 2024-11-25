package cz.kudladev.tamziikmp.weather.domain

import cz.kudladev.tamziikmp.core.util.Resource
import cz.kudladev.tamziikmp.weather.data.dto.WeatherResponse

class WeatherUseCase(
    private val weatherClient: WeatherClient
) {
    suspend fun execute(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherResponse>{
        return try {
            val response = weatherClient.getWeather(latitude, longitude)
            println("WeatherResponse: $response")
            Resource.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}