package cz.kudladev.tamziikmp.weather.data.dto.current

import kotlinx.serialization.Serializable


@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)