package cz.kudladev.tamziikmp.weather.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WeatherDescriptionCurrentItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.padding(8.dp)
    ){
        Box(modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.Black),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "°C",
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label, fontWeight = FontWeight.Normal)
            Text(value, fontWeight = FontWeight.Bold)
        }
    }
}