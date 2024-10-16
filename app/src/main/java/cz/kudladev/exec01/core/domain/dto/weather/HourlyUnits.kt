package cz.kudladev.exec01.core.domain.dto.weather

data class HourlyUnits(
    val apparent_temperature: String,
    val dew_point_2m: String,
    val precipitation: String,
    val precipitation_probability: String,
    val relative_humidity_2m: String,
    val temperature_2m: String,
    val time: String,
    val weather_code: String,
    val wind_speed_10m: String
)