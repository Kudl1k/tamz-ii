package cz.kudladev.exec01.core.domain.dto.location

data class Geometry(
    val coordinates: List<Double>,
    val type: String
)