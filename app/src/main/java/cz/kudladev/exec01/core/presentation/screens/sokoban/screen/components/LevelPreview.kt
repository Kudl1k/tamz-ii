package cz.kudladev.exec01.core.presentation.screens.sokoban.screen.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import cz.kudladev.exec01.R
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level

@Composable
fun LevelPreview(
    modifier: Modifier = Modifier,
    level: Level
) {
    val context = LocalContext.current

    val bmp = remember {
        arrayOf(
            ContextCompat.getDrawable(context, R.drawable.empty)?.toBitmap()?.asImageBitmap(),
            ContextCompat.getDrawable(context, R.drawable.wall)?.toBitmap()?.asImageBitmap(),
            ContextCompat.getDrawable(context, R.drawable.box)?.toBitmap()?.asImageBitmap(),
            ContextCompat.getDrawable(context, R.drawable.goal)?.toBitmap()?.asImageBitmap(),
            ContextCompat.getDrawable(context, R.drawable.hero)?.toBitmap()?.asImageBitmap(),
            ContextCompat.getDrawable(context, R.drawable.boxok)?.toBitmap()?.asImageBitmap(),
            ContextCompat.getDrawable(context, R.drawable.hero_goal)?.toBitmap()?.asImageBitmap()
        )
    }

    Canvas(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth()
            .aspectRatio(level.width.toFloat() / level.height) // Maintain aspect ratio of the level
    ) {
        val cellWidth = size.width / level.width
        val cellHeight = size.height / level.height

        for (y in 0 until level.height) {
            for (x in 0 until level.width) {
                val currentObject = level.level[y * level.width + x]
                if (currentObject in bmp.indices) {
                    bmp[currentObject]?.let { bitmap ->
                        drawIntoCanvas { canvas ->
                            val dstOffset = Offset(x * cellWidth, y * cellHeight)
                            val scaledBitmap = bitmap.asAndroidBitmap().scale(
                                cellWidth.toInt(),
                                cellHeight.toInt(),
                                true
                            ).asImageBitmap()

                            canvas.nativeCanvas.drawBitmap(
                                scaledBitmap.asAndroidBitmap(),
                                dstOffset.x,
                                dstOffset.y,
                                null
                            )
                        }
                    }
                }
            }
        }
    }
}