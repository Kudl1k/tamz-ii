package cz.kudladev.exec01.core.domain.dto.weather

data class Current(
    val interval: Int,
    val is_day: Int,
    val time: String
)