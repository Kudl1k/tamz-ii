package cz.kudladev.exec01.core.domain.dto.location

data class Address(
    val address_number: String,
    val mapbox_id: String,
    val name: String,
    val street_name: String
)