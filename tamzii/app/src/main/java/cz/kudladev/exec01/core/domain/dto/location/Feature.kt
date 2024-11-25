package cz.kudladev.exec01.core.domain.dto.location

data class Feature(
    val geometry: Geometry,
    val id: String,
    val properties: Properties,
    val type: String
)