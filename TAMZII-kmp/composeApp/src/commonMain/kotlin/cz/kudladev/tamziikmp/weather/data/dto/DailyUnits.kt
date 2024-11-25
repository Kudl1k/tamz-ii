package cz.kudladev.tamziikmp.weather.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DailyUnits(
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val uv_index_max: String,
    val weather_code: String
)