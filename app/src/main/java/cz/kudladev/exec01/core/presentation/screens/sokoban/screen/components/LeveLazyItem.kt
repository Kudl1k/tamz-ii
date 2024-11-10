package cz.kudladev.exec01.core.presentation.screens.sokoban.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level

@Composable
fun LevelLazyItem(
    modifier: Modifier = Modifier,
    level: Level,
    onSelect: (Int) -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 2.dp, horizontal = 20.dp),
        onClick = { onSelect(level.id) }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LevelPreview(
                level = level
            )
            Text(
                text = "Level ${level.id}",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp
            )
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Boxes: ${level.boxes}",
                    fontSize = 16.sp
                )
                Text(
                    text = "Size: ${level.width}x${level.height}",
                    fontSize = 16.sp
                )
            }
        }
    }


}