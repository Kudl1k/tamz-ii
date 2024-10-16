package cz.kudladev.exec01.core.domain.dto.weather

data class Daily(
    val time: List<String>,
    val uv_index_max: List<Double>
)