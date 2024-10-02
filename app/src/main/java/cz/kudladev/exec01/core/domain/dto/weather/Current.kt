package cz.kudladev.exec01.core.domain.dto.weather

data class Current(
    val interval: Int,
    val temperature_2m: Double,
    val time: String,
    val wind_speed_10m: Double
)