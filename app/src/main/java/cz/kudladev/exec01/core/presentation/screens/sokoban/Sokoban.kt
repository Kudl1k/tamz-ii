package cz.kudladev.exec01.core.presentation.screens.sokoban

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.scale
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
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
import cz.kudladev.exec01.core.presentation.screens.scanner_screen.data.Game
import cz.kudladev.exec01.core.presentation.screens.scanner_screen.data.level1
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SokoView(navController: NavController) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

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

    val game by remember { mutableStateOf(Game()) }



    NavDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithDrawer(
                    title = "Sokoban",
                    icon = Icons.Default.Menu,
                    onIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(it).verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .pointerInput(Unit) {
                                this.detectSwipe(
                                    onSwipeRight = {
                                        Log.d("Swipe", "Swipe Right")
                                        game.checkMovement(0)
                                    },
                                    onSwipeLeft = {
                                        Log.d("Swipe", "Swipe Left")
                                        game.checkMovement(1)
                                    },
                                    onSwipeUp = {
                                        Log.d("Swipe", "Swipe Up")
                                        game.checkMovement(2)
                                    },
                                    onSwipeDown = {
                                        Log.d("Swipe", "Swipe Down")
                                        game.checkMovement(3)
                                    }
                                )
                            }


                    ) {
                        size = IntSize(this.size.width.toInt(), this.size.height.toInt())
                        val width = size.width.toFloat() / game.levelWidth
                        val height = size.height.toFloat() / game.levelHeight

                        val scaleFactor = if (size.width > size.height) {
                            height / bmp[0]?.height?.toFloat()!!
                        } else {
                            minOf(width, height) / bmp[0]?.width?.toFloat()!!
                        }

                        for (y in 0 until game.levelHeight) {
                            for (x in 0 until game.levelWidth) {
                                val currentObject = game.currentLevel.value[y * game.levelWidth + x]
                                if (currentObject in bmp.indices) {
                                    bmp[currentObject]?.let { bitmap ->
                                        drawIntoCanvas { canvas ->
                                            val dstOffset = Offset(x * width, y * height)
                                            val dstSize = Size(width, height)

                                            // Scale the bitmap using the scaleFactor
                                            val scaledBitmap = bitmap.asAndroidBitmap().scale(
                                                (bitmap.width * scaleFactor).toInt(),
                                                (bitmap.height * scaleFactor).toInt(),
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
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Points: ${game.points.value}/${game.maxPoints.value}",
                        modifier = Modifier.wrapContentSize()
                    )
                    Button(
                        onClick = {
                            game.restart()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Restart",
                            modifier = Modifier.wrapContentSize(),
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    if(game.win.value){
                        Text(
                            text = "You win!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 48.sp
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Arrows(
                                tilt = 0f,
                                onClick = {
                                    game.checkMovement(2)
                                },
                                modifier = Modifier.size(75.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Arrows(
                                    tilt = -90f,
                                    onClick = {
                                        game.checkMovement(1)
                                    },
                                    modifier = Modifier.size(75.dp)
                                )
                                Spacer(modifier = Modifier.size(75.dp))
                                Arrows(
                                    tilt = 90f,
                                    onClick = {
                                        game.checkMovement(0)
                                    },
                                    modifier = Modifier.size(75.dp)
                                )
                            }
                            Arrows(
                                tilt = 180f,
                                onClick = {
                                    game.checkMovement(3)
                                },
                                modifier = Modifier.size(75.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


suspend fun PointerInputScope.detectSwipe(
    swipeState: MutableIntState = mutableIntStateOf(-1),
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {},
) = detectDragGestures(
    onDrag = { change, dragAmount ->
        change.consume()
        val (x, y) = dragAmount
        if (abs(x) > abs(y)) {
            when {
                x > 0 -> swipeState.intValue = 0
                x < 0 -> swipeState.intValue = 1
            }
        } else {
            when {
                y > 0 -> swipeState.intValue = 2
                y < 0 -> swipeState.intValue = 3
            }
        }
    },
    onDragEnd = {
        when (swipeState.intValue) {
            0 -> onSwipeRight()
            1 -> onSwipeLeft()
            2 -> onSwipeDown()
            3 -> onSwipeUp()
        }
    }
)