package cz.kudladev.exec01.core.presentation.screens.graphs

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import kotlinx.coroutines.launch

@Composable
fun GraphsScreen(modifier: Modifier = Modifier,navController: NavController, state: GraphsScreenState, onEvent: (GraphsScreenEvents) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            onEvent(GraphsScreenEvents.TogglePermissions)
        } else if (isGranted && !state.permissionsGranted) {
            onEvent(GraphsScreenEvents.TogglePermissions)
        }
    }
    val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
        LaunchedEffect(key1 = Unit) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    NavDrawer(
        navController = navController,
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithDrawer(
                    title = "Graphs",
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (!state.error.isNullOrBlank()){
                    Text(text = state.error)
                }
                Text("longitude: ${state.longitude}, latitude: ${state.latitude}")
            }
        }
    }
}