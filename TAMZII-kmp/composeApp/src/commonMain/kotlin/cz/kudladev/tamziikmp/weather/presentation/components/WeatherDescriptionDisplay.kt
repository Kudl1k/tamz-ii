package cz.kudladev.tamziikmp.weather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenState

@Composable
fun WeatherDescriptionDisplay(
    state: WeatherScreenState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        WeatherDescriptionCurrentDisplay(state)
    }
}