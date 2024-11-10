package cz.kudladev.exec01.core.presentation.screens.sokoban.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.kudladev.exec01.core.navigation.Routes
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import cz.kudladev.exec01.core.presentation.screens.sokoban.SokobanEvent
import cz.kudladev.exec01.core.presentation.screens.sokoban.SokobanState
import cz.kudladev.exec01.core.presentation.screens.sokoban.screen.components.LevelLazyItem
import cz.kudladev.exec01.core.presentation.screens.sokoban.screen.components.LevelPreview
import kotlinx.coroutines.launch

@Composable
fun SokobanMainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: SokobanState,
    onEvent: (SokobanEvent) -> Unit
) {

    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    },
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Select a level",
                    fontSize = 35.sp
                )
                if (state.levels.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No levels found",
                            fontSize = 20.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.levels) {
                            LevelLazyItem(level = it) { levelId ->
                                onEvent(SokobanEvent.SelectLevel(levelId))
                                navController.navigate(Routes.SokobanGame.route)
                            }
                        }
                    }


                }
                Button(
                    modifier = Modifier.padding(vertical = 10.dp),
                    onClick = {
                        navController.navigate(Routes.SokobanEdit.route)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Text(
                            "Add a new level"
                        )
                    }
                }
            }
        }
    }
}