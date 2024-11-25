package cz.kudladev.exec01.core.domain.dto.location.retrive

data class Properties(
    val address: String,
    val context: Context,
    val coordinates: Coordinates,
    val external_ids: ExternalIds,
    val feature_type: String,
    val full_address: String,
    val language: String,
    val maki: String,
    val mapbox_id: String,
    val metadata: Metadata,
    val name: String,
    val operational_status: String,
    val place_formatted: String,
    val poi_category: List<String>,
    val poi_category_ids: List<String>
)