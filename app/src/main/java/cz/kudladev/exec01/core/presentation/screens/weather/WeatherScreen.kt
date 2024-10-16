package cz.kudladev.exec01.core.presentation.screens.weather

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen(modifier: Modifier = Modifier, navController: NavController, state: WeatherScreenState, onEvent: (WeatherScreenEvents) -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            onEvent(WeatherScreenEvents.TogglePermissions)
        } else if (isGranted && !state.permissionsGranted) {
            onEvent(WeatherScreenEvents.TogglePermissions)
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
            ) {
                if (!state.error.isNullOrBlank()){
                    Text(text = state.error)
                } else {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "${state.weather?.current?.temperature_2m} ${state.weather?.current_units?.temperature_2m}",
                                        fontSize = MaterialTheme.typography.displayMedium.fontSize,
                                        fontWeight = MaterialTheme.typography.displayMedium.fontWeight
                                    )
                                    Text(
                                        text = "${state.weather?.current?.wind_speed_10m} ${state.weather?.current_units?.wind_speed_10m}",
                                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                        fontWeight = MaterialTheme.typography.titleSmall.fontWeight
                                    )

                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "${String.format("%.2f", state.weather?.latitude)}° ${String.format("%.2f", state.weather?.longitude)}°",
                                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                        fontWeight = MaterialTheme.typography.titleSmall.fontWeight
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