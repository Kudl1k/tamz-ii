package cz.kudladev.tamziikmp.weather.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenState

@Composable
fun WeatherDescriptionCurrentDisplay(
    state: WeatherScreenState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = modifier.fillMaxWidth(0.7f)
        ) {
            Text(
                "Current weather",
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    WeatherDescriptionCurrentItem(
                        label = "Feels like",
                        value = state.weather?.main?.feels_like.toString() + "°C"
                    )
                    WeatherDescriptionCurrentItem(
                        label = "Pressure",
                        value = state.weather?.main?.pressure.toString() + "hPa"
                    )
                    WeatherDescriptionCurrentItem(
                        label = "Visibility",
                        value = state.weather?.visibility.toString() + "m"
                    )
                    WeatherDescriptionCurrentItem(
                        label = "Sunrise",
                        value = state.weather?.sys?.sunrise.toString()
                    )
                }
                Column {
                    WeatherDescriptionCurrentItem(
                        label = "Humidity",
                        value = state.weather?.main?.humidity.toString() + "%"
                    )
                    WeatherDescriptionCurrentItem(
                        label = "Wind",
                        value = state.weather?.wind?.speed.toString() + "m/s"
                    )
                    WeatherDescriptionCurrentItem(
                        label = "Cloudiness",
                        value = state.weather?.clouds?.all.toString() + "%"
                    )
                    WeatherDescriptionCurrentItem(
                        label = "Sunset",
                        value = state.weather?.sys?.sunset.toString()
                    )
                }
            }
        }
    }

}