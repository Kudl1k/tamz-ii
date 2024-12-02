package cz.kudladev.tamziikmp.weather.data.dto.current

import kotlinx.serialization.Serializable


@Serializable
data class Sys(
    val country: String? = null,
    val id: Int? = null,
    val sunrise: Int,
    val sunset: Int,
    val type: Int? = null
)