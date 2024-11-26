package cz.kudladev.tamziikmp.weather.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.kudladev.tamziikmp.permissions.RequestLocationPermission

@Composable
fun WeatherScreen(
    state: WeatherScreenState,
    onEvent: (WeatherScreenEvent) -> Unit
) {
    RequestLocationPermission(onEvent)


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Weather Screen")
        Text(text = "Weather: ${state.weather}")
    }
}