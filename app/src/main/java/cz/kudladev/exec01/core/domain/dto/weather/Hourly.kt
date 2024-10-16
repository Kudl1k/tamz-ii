package cz.kudladev.exec01.core.domain.dto.weather

data class Hourly(
    val apparent_temperature: List<Double>,
    val dew_point_2m: List<Double>,
    val precipitation: List<Double>,
    val precipitation_probability: List<Int>,
    val relative_humidity_2m: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weather_code: List<Int>,
    val wind_speed_10m: List<Double>
)