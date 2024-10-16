package cz.kudladev.exec01.core.domain.dto.weather

data class HourlyUnits(
    val relative_humidity_2m: String,
    val temperature_2m: String,
    val time: String,
    val wind_speed_10m: String,
    val weather_code: String

)