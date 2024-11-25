package cz.kudladev.exec01.core.domain.dto.location

data class Region(
    val mapbox_id: String,
    val name: String,
    val region_code: String,
    val region_code_full: String,
    val wikidata_id: String
)