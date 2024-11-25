package cz.kudladev.exec01.core.domain.dto.location.retrive

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)