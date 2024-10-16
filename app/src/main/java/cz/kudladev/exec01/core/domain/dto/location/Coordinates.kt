package cz.kudladev.exec01.core.domain.dto.location

data class Coordinates(
    val accuracy: String,
    val latitude: Double,
    val longitude: Double,
    val routable_points: List<RoutablePoint>
)