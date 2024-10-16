package cz.kudladev.exec01.core.presentation.screens.weather

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import kotlinx.coroutines.launch
import cz.kudladev.exec01.R
import cz.kudladev.exec01.core.navigation.Routes

@Composable
fun WeatherScreen(modifier: Modifier = Modifier, navController: NavController, state: WeatherScreenState, onEvent: (WeatherScreenEvents) -> Unit) {
    val route = Routes.Weather

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val focus = remember {
        FocusRequester()
    }

    val searchBoxHeight = animateDpAsState( // Direct assignment
        targetValue = if (state.showSearchBox) 75.dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    ).value


    val searchBoxOffset = animateDpAsState(
        targetValue = if (state.showSearchBox) 0.dp else (-48).dp,
        animationSpec = tween(durationMillis = 300)
    ).value

    val searchVisibility = animateFloatAsState(
        targetValue = if (state.showSearchBox) 0f else 1f,
        animationSpec = tween(durationMillis = 300)
    ).value


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
                    title = route.title!!,
                    icon = Icons.Default.Menu,
                    onIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    actions = {
                        if (!state.permissionsGranted){
                            Icon(
                                imageVector = Icons.Default.LocationOff,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            IconButton(
                                onClick = {
                                    onEvent(WeatherScreenEvents.ToggleSearchBox)
                                },
                                enabled = !state.showSearchBox
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp).alpha(searchVisibility)
                                )
                            }
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(searchBoxHeight) // Use searchBoxHeight directly
                        .offset(y = searchBoxOffset) // Use searchBoxOffset directly
                ) {
                    if (state.showSearchBox){
                        SearchBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            value = state.searchQuery,
                            onValueChange = { query ->
                                onEvent(WeatherScreenEvents.setSearchQuery(query))
                            },
                            onSearch = {
                                if (state.searchQuery.isNotBlank()) {
                                    onEvent(WeatherScreenEvents.Search)
                                } else {
                                    onEvent(WeatherScreenEvents.ToggleSearchBox)
                                }
                            },
                            focus = focus,
                            shouldShowHint = true
                        )
                    }
                }

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
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.wind),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "${state.weather?.current?.wind_speed_10m} ${state.weather?.current_units?.wind_speed_10m}",
                                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                            fontWeight = MaterialTheme.typography.titleSmall.fontWeight
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier.weight(1f),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "${String.format("%.2f", state.weather?.latitude)}° ${String.format("%.2f", state.weather?.longitude)}°",
                                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                            fontWeight = MaterialTheme.typography.titleSmall.fontWeight
                                        )
                                    }

                                    state.weather?.current?.weather_code?.let { weather_code ->
                                        Image(
                                            painter = painterResource(id = getIcon(weather_code)),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(128.dp)
                                                .padding(top = 8.dp)
                                                .offset(x = 20.dp, y = 20.dp)
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
}