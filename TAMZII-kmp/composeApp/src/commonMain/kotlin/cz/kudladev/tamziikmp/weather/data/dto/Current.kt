package cz.kudladev.tamziikmp.weather.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class Current(
    val interval: Int,
    val is_day: Int,
    val time: String
)