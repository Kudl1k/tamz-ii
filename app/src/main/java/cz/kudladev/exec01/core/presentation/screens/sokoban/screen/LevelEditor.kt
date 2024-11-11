package cz.kudladev.exec01.core.presentation.screens.sokoban.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.navigation.NavController
import cz.kudladev.exec01.R
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import cz.kudladev.exec01.core.presentation.screens.sokoban.SokobanEvent
import cz.kudladev.exec01.core.presentation.screens.sokoban.SokobanState
import cz.kudladev.exec01.core.presentation.screens.sokoban.detectSwipe
import kotlinx.coroutines.launch

@Composable
fun LevelEditor(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: SokobanState,
    onEvent: (SokobanEvent) -> Unit
) {

    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var size by remember { mutableStateOf(IntSize.Zero) }
    var selectedObject by remember { mutableStateOf(0) }
    var isTherePlayer by remember { mutableStateOf(state.editorLevel.value.level.contains(4)) }
    var sameCountOfBoxesAsGoals by remember { mutableStateOf(sameAmountOfBoxesAndGoals(state.editorLevel.value.level)) }

    LaunchedEffect(state.editorLevel.value) {
        Log.d("LevelEditor", "Level changed")
        isTherePlayer = state.editorLevel.value.level.contains(4)
        sameCountOfBoxesAsGoals = sameAmountOfBoxesAndGoals(state.editorLevel.value.level)
    }

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

    NavDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithDrawer(
                    title = "Level Editor",
                    icon = Icons.Default.Menu,
                    onIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(it)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Width",
                            fontSize = 20.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clickable {
                                        onEvent(SokobanEvent.DecreaseWidth)
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Add"
                                )
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = state.editorLevel.value.width.toString()
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clickable {
                                        onEvent(SokobanEvent.IncreaseWidth)
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add"
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Height",
                                fontSize = 20.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(100))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .clickable {
                                            onEvent(SokobanEvent.DecreaseHeight)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Add"
                                    )
                                }
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = state.editorLevel.value.height.toString()
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(100))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .clickable {
                                            onEvent(SokobanEvent.IncreaseHeight)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add"
                                    )
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in 0 .. 4) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedObject == i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                                .clickable { selectedObject = i },
                            contentAlignment = Alignment.Center
                        ) {
                            // Display the object image or a placeholder
                            if (bmp[i] != null) {
                                Image(
                                    bitmap = bmp[i]!!,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            } else {
                                Text(text = i.toString())
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Assign weight to prioritize space allocation
                    contentAlignment = Alignment.Center // Center the Canvas within the Box
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .aspectRatio(state.editorLevel.value.width.toFloat() / state.editorLevel.value.height.toFloat())
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    // Calculate tile size using the minimum of width/height per tile.
                                    val tileSize = minOf(
                                        size.width.toFloat() / state.editorLevel.value.width,
                                        size.height.toFloat() / state.editorLevel.value.height
                                    )

                                    Log.d("LevelEditor", "Tile size: $tileSize")

                                    // Compute the offset to center grid.
                                    val offsetX = (size.width - (tileSize * state.editorLevel.value.width)) / 2
                                    val offsetY = (size.height - (tileSize * state.editorLevel.value.height)) / 2

                                    // Convert tap coordinates into grid coordinates.
                                    val x = ((offset.x - offsetX) / tileSize).toInt()
                                    val y = ((offset.y - offsetY) / tileSize).toInt()

                                    // Check if within valid bounds.
                                    if (x in 0 until state.editorLevel.value.width && y in 0 until state.editorLevel.value.height) {
                                        val index = y * state.editorLevel.value.width + x
                                        // Ensure index is in bounds.
                                        if (index in state.editorLevel.value.level.indices) {
                                            // Update the level array with new object value.
                                            if (selectedObject == 4) {
                                                if (state.editorLevel.value.level.contains(4)) {
                                                    val indexPlayer = state.editorLevel.value.level.indexOf(4)
                                                    val updatedLevel = state.editorLevel.value.level.copyOf()
                                                    updatedLevel[indexPlayer] = 0
                                                    updatedLevel[index] = selectedObject
                                                    onEvent(SokobanEvent.UpdateEditLevel(updatedLevel))
                                                } else {
                                                    val updatedLevel = state.editorLevel.value.level.copyOf()
                                                    updatedLevel[index] = selectedObject
                                                    onEvent(SokobanEvent.UpdateEditLevel(updatedLevel))
                                                }
                                            } else {
                                                val updatedLevel = state.editorLevel.value.level.copyOf()
                                                updatedLevel[index] = selectedObject
                                                onEvent(SokobanEvent.UpdateEditLevel(updatedLevel))
                                            }
                                        }
                                    }
                                }
                            }
                    ) {
                        // Set `size` based on actual canvas size to avoid layout inconsistencies
                        size = IntSize(this.size.width.toInt(), this.size.height.toInt())

                        val tileWidth = size.width.toFloat() / state.editorLevel.value.width
                        val tileHeight = size.height.toFloat() / state.editorLevel.value.height

                        for (y in 0 until state.editorLevel.value.height) {
                            for (x in 0 until state.editorLevel.value.width) {
                                val currentObject = state.editorLevel.value.level[y * state.editorLevel.value.width + x]
                                if (currentObject in bmp.indices) {
                                    bmp[currentObject]?.let { bitmap ->
                                        drawIntoCanvas { canvas ->
                                            val dstOffset = Offset(x * tileWidth, y * tileHeight)
                                            val dstSize = Size(tileWidth, tileHeight)

                                            // Scale and draw the bitmap
                                            val scaledBitmap = bitmap.asAndroidBitmap().scale(
                                                tileWidth.toInt(),
                                                tileHeight.toInt(),
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
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isTherePlayer) {
                        Text(
                            "Player must be placed on the level",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (!sameCountOfBoxesAsGoals) {
                        Text(
                            "There is not the same amount of boxes and goals ${state.editorLevel.value.level.count { it == 2 }}/${state.editorLevel.value.level.count { it == 3 }}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onEvent(SokobanEvent.SaveNewLevel)
                            navController.popBackStack()
                        },
                        enabled = isTherePlayer || !sameCountOfBoxesAsGoals
                    ) {
                        Text(text = "Save Level")
                    }
                }
            }
        }
    }
}

private fun sameAmountOfBoxesAndGoals(level: IntArray): Boolean {
    var boxes = 0
    var goals = 0
    for (i in level) {
        if (i == 2) boxes++
        if (i == 3) goals++
    }
    return boxes == goals
}