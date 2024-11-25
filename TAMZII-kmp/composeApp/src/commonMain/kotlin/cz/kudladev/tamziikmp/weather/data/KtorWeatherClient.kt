package cz.kudladev.tamziikmp.weather.data

import cz.kudladev.tamziikmp.weather.data.dto.WeatherResponse
import cz.kudladev.tamziikmp.weather.domain.WeatherClient
import cz.kudladev.tamziikmp.weather.network.NetworkConstants
import cz.kudladev.tamziikmp.weather.network.NetworkError
import cz.kudladev.tamziikmp.weather.network.NetworkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException

class KtorWeatherClient(
    private val httpClient: HttpClient
) : WeatherClient {
    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        val result = try {
            httpClient.get {
                url(NetworkConstants.BASE_URL + "v1/forecast")
                contentType(ContentType.Application.Json)
                parameter("latitude", latitude)
                parameter("longitude", longitude)
                parameter("current", "is_day")
                parameter("hourly", "temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,precipitation_probability,precipitation,weather_code,wind_speed_10m")
                parameter("daily", "uv_index_max,weather_code,temperature_2m_max,temperature_2m_min")
                parameter("timezone", "auto")
            }
        } catch (e: IOException) {
            throw NetworkException(NetworkError.SERVICE_UNAVAILABLE)
        }

        when (result.status.value) {
            in 200..299 -> Unit
            500 -> throw NetworkException(NetworkError.SERVER_ERROR)
            in 400..499 -> throw NetworkException(NetworkError.CLIENT_ERROR)
            else -> throw NetworkException(NetworkError.UNKNOWN_ERROR)
        }

        return try {
            result.body<WeatherResponse>()
        } catch (e: Exception) {
            e.printStackTrace()
            throw NetworkException(NetworkError.UNKNOWN_ERROR)
        }
    }
}