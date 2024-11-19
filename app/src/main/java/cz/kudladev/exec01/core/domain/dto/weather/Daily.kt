package cz.kudladev.exec01.core.domain.dto.weather

data class Daily(
    val time: List<String>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
)