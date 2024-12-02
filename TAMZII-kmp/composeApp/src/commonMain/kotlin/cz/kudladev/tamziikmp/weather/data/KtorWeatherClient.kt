package cz.kudladev.tamziikmp.weather.data

import cz.kudladev.tamziikmp.weather.data.dto.current.WeatherCurrentResponse
import cz.kudladev.tamziikmp.weather.domain.WeatherClient
import cz.kudladev.tamziikmp.weather.network.NetworkConstants
import cz.kudladev.tamziikmp.weather.network.NetworkError
import cz.kudladev.tamziikmp.weather.network.NetworkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException

class KtorWeatherClient(
    private val httpClient: HttpClient
) : WeatherClient {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherCurrentResponse {
        val result = try {
            println("Sending request to OpenWeather API with lat: $latitude, lon: $longitude")
            httpClient.get {
                url(NetworkConstants.OPENWEATHER + "weather")
                contentType(ContentType.Application.Json)
                parameter("lat", latitude)
                parameter("lon", longitude)
                parameter("appid", NetworkConstants.OPENWEATHER_API)
                parameter("units", "metric")
            }
        } catch (e: IOException) {
            println("IOException occurred: ${e.message}")
            throw NetworkException(NetworkError.SERVICE_UNAVAILABLE)
        }

        println("Received response with status: ${result.status.value}")

        when (result.status.value) {
            in 200..299 -> Unit
            500 -> {
                println("Server error occurred")
                throw NetworkException(NetworkError.SERVER_ERROR)
            }
            in 400..499 -> {
                val responseBody = result.bodyAsText() // Get the response body as text
                println("Client error occurred: Status code = ${result.status.value}, Response body = $responseBody")
                throw NetworkException(NetworkError.CLIENT_ERROR)
            }
            else -> {
                println("Unknown error occurred")
                throw NetworkException(NetworkError.UNKNOWN_ERROR)
            }
        }

        return try {
            result.body<WeatherCurrentResponse>()
        } catch (e: Exception) {
            println("Exception occurred while parsing response: ${e.message}")
            e.printStackTrace()
            throw NetworkException(NetworkError.UNKNOWN_ERROR)
        }
    }
}