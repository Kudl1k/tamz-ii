package cz.kudladev.exec01.core.presentation.screens.sokoban.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import cz.kudladev.exec01.core.presentation.screens.sokoban.SokobanEvent
import cz.kudladev.exec01.core.presentation.screens.sokoban.SokobanState
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

    LaunchedEffect(state.editorLevel.value) {
        Log.d("LevelEditor", "Level changed")
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
            }


        }
    }
}