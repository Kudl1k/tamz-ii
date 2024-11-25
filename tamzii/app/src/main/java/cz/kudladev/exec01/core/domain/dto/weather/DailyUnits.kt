package cz.kudladev.exec01.core.domain.dto.weather

data class DailyUnits(
    val time: String,
    val uv_index_max: String,
    val weather_code: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
)