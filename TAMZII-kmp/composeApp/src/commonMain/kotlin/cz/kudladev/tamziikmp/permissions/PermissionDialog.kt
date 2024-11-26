package cz.kudladev.tamziikmp.permissions

import androidx.compose.runtime.Composable
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenEvent

@Composable
expect fun RequestLocationPermission(onEvent: (WeatherScreenEvent) -> Unit)