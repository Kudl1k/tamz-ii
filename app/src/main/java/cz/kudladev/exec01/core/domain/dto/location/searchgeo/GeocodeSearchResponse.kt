package cz.kudladev.exec01.core.domain.dto.location.searchgeo

data class GeocodeSearchResponse(
    val attribution: String,
    val response_id: String,
    val suggestions: List<Suggestion>
)