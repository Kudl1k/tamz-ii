package cz.kudladev.tamziikmp.weather.presentation

import cz.kudladev.tamziikmp.weather.data.dto.WeatherResponse
import cz.kudladev.tamziikmp.weather.network.NetworkError

data class WeatherScreenState(
    val permissionsGranted: Boolean = true,
    val isLoading: Boolean = true,
    val error: NetworkError? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val weather: WeatherResponse? = null,
//    val currentWeather: CurrentWeather? = null,
//    val place: GeocodeReverseResponse? = null,
    val searchQuery: String = "",
    val showSearchBox: Boolean = false,
//    val sessionId: String = UUID.randomUUID().toString(),
//    val searchResults: GeocodeSearchResponse? = null
)