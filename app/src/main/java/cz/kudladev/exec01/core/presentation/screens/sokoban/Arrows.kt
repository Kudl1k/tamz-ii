package cz.kudladev.exec01.core.presentation.screens.sokoban

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import cz.kudladev.exec01.R

@Composable
fun Arrows(
    modifier: Modifier = Modifier,
    tilt: Float,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(40))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ){
        Icon(
            painter = painterResource(R.drawable.arrow),
            contentDescription = "Arrow",
            modifier = Modifier.rotate(tilt),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}