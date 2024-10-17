package cz.kudladev.exec01.core.domain.dto.weather

data class CurrentWeather(
    val apparent_temperature: Double,
    val dew_point_2m: Double,
    val precipitation: Double,
    val precipitation_probability: Int,
    val relative_humidity_2m: Int,
    val temperature_2m: Double,
    val time: String,
    val weather_code: Int,
    val wind_speed_10m: Double
)
