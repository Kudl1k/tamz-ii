package cz.kudladev.exec01.core.presentation.screens.weather

import cz.kudladev.exec01.core.domain.dto.location.GeocodeReverseResponse
import cz.kudladev.exec01.core.domain.dto.location.searchgeo.GeocodeSearchResponse
import cz.kudladev.exec01.core.domain.dto.weather.Weather
import java.util.UUID

data class WeatherScreenState(
    val permissionsGranted: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val weather: Weather? = null,
    val place: GeocodeReverseResponse? = null,
    val searchQuery: String = "",
    val showSearchBox: Boolean = false,
    val sessionId: String = UUID.randomUUID().toString(),
    val searchResults: GeocodeSearchResponse? = null
)
