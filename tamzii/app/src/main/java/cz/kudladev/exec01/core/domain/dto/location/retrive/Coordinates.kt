package cz.kudladev.exec01.core.domain.dto.location.retrive

data class Coordinates(
    val latitude: Double,
    val longitude: Double,
    val routable_points: List<RoutablePoint>
)