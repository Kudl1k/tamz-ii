package cz.kudladev.exec01.core.domain.dto.location.retrive

data class RetrieveResponse(
    val attribution: String,
    val features: List<Feature>,
    val type: String
)