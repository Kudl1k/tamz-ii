package cz.kudladev.exec01.core.domain.dto.location

data class GeocodeReverseResponse(
    val attribution: String,
    val features: List<Feature>,
    val type: String
)