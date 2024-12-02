package cz.kudladev.tamziikmp.weather.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import cz.kudladev.tamziikmp.weather.network.NetworkConstants
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenState

@Composable
fun WeatherMainDisplay(
    state: WeatherScreenState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = state.weather?.name ?: "",
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = rememberAsyncImagePainter(
                    NetworkConstants.OPENWEATHER_ICON + (state.weather?.weather?.get(0)?.icon
                    ?: "") + "@4x.png"),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f),
                contentDescription = null,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f),
            contentAlignment = Alignment.BottomStart
        ){
            Text(
                text = state.weather?.main?.temp.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}