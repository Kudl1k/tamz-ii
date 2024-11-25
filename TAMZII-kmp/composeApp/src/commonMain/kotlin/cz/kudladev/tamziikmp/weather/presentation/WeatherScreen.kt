package cz.kudladev.tamziikmp.weather.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WeatherScreen(
    state: WeatherScreenState,
    onEvent: (WeatherScreenEvent) -> Unit
) {
    Column {
        Text(state.weather.toString())
    }
}