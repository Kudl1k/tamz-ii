package cz.kudladev.tamziikmp.weather.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cz.kudladev.tamziikmp.permissions.RequestLocationPermission
import cz.kudladev.tamziikmp.weather.presentation.components.WeatherDescriptionDisplay
import cz.kudladev.tamziikmp.weather.presentation.components.WeatherMainDisplay

@Composable
fun WeatherScreen(
    state: WeatherScreenState,
    onEvent: (WeatherScreenEvent) -> Unit
) {
    RequestLocationPermission(onEvent)

    LaunchedEffect(key1 = state.permissionsGranted){
        if (state.permissionsGranted){
            onEvent(WeatherScreenEvent.getWeather)
        }
    }

    println(state.weather)

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .systemBarsPadding(),
        ){
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            ) {
                WeatherMainDisplay(state = state)
                WeatherDescriptionDisplay(state = state, modifier = Modifier.weight(1f))
            }
        }
    }
}