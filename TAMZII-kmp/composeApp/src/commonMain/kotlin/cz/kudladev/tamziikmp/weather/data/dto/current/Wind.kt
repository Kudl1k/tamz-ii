package cz.kudladev.tamziikmp.weather.data.dto.current

import kotlinx.serialization.Serializable


@Serializable
data class Wind(
    val deg: Int? = null,
    val gust: Double? = null,
    val speed: Double? = null
)