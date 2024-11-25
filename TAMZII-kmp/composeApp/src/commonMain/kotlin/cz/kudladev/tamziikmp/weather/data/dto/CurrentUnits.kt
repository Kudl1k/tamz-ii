package cz.kudladev.tamziikmp.weather.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class CurrentUnits(
    val interval: String,
    val is_day: String,
    val time: String
)