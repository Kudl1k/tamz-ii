package cz.kudladev.exec01.core.presentation.screens.graphs

import cz.kudladev.exec01.core.domain.dto.weather.Weather

data class GraphsScreenState(
    val permissionsGranted: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val weather: Weather? = null
)
