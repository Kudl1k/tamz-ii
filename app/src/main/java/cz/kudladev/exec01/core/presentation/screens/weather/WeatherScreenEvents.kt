package cz.kudladev.exec01.core.presentation.screens.weather

sealed class WeatherScreenEvents {

    data object TogglePermissions : WeatherScreenEvents()

    data object ToggleSearchBox : WeatherScreenEvents()

    data class setSearchQuery(val query: String) : WeatherScreenEvents()

    data object Search : WeatherScreenEvents()

}