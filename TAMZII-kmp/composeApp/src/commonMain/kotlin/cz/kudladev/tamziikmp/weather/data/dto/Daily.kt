package cz.kudladev.tamziikmp.weather.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class Daily(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>
)