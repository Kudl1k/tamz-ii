package cz.kudladev.tamziikmp.weather.data.dto.current

import kotlinx.serialization.Serializable

@Serializable
data class Clouds(
    val all: Int
)