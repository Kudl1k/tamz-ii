package cz.kudladev.exec01.core.domain.dto.location.searchgeo

data class Context(
    val address: Address,
    val country: Country,
    val district: District,
    val neighborhood: Neighborhood,
    val place: Place,
    val postcode: Postcode,
    val region: Region,
    val street: Street
)