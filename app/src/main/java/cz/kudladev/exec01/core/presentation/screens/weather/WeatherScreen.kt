package cz.kudladev.exec01.core.presentation.screens.weather

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

    val keyboardController = LocalSoftwareKeyboardController.current

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

    val dropdownVisibility = animateFloatAsState(
        targetValue = if (state.showSearchBox && state.searchResults != null) 1f else 0f,
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
                                    modifier = Modifier
                                        .size(24.dp)
                                        .alpha(searchVisibility)
                                )
                            }
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
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
                                    onEvent(WeatherScreenEvents.ToggleSearchBox)
                                },
                                onSearchTriggered = {
                                    onEvent(WeatherScreenEvents.Search)
                                },
                                focus = focus,
                                shouldShowHint = true
                            )
                        }
                    }
                    if (!state.error.isNullOrBlank()){
                        Text(text = state.error)
                    } else if (state.place != null){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = state.place.features[0].properties.context.place.name,
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                                )
                                Text(
                                    text = "${state.place.features[0].properties.context.region.name}, ${state.place.features[0].properties.context.country.name}",
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                val time = state.weather?.current?.time?.split("T")
                                Text(
                                    text = "${time?.get(0)} ${time?.get(1)}",
                                )
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                        ) {
                            Box(
                                modifier = Modifier.weight(1f)
                            ){
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(id = getIcon(state.weather?.hourly?.weather_code?.first() ?: 0)),
                                        contentDescription = null,
                                        modifier = Modifier.size(256.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.BottomStart
                                ){
                                    Text(
                                        text = "${state.currentWeather?.temperature_2m}°",
                                        fontSize = MaterialTheme.typography.displayLarge.fontSize,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 48.dp, start = 24.dp, bottom = 24.dp)
                                ) {
                                    Text(
                                        text = "Právě teď",
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column {
                                        Row(
                                            horizontalArrangement = Arrangement.Center
                                        ){
                                            Box(modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(MaterialTheme.colorScheme.primary),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Text(
                                                    text = "°C",
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("Pocitově", fontWeight = FontWeight.Thin)
                                                Text("${state.currentWeather?.apparent_temperature}°", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.Center
                                        ){
                                            Box(modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(MaterialTheme.colorScheme.primary),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Icon(
                                                    painter = painterResource(R.drawable.umbrella),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("Srážky", fontWeight = FontWeight.Thin)
                                                Text("${state.currentWeather?.precipitation_probability} %", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.Center
                                        ){
                                            Box(modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(MaterialTheme.colorScheme.primary),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Icon(
                                                    painter = painterResource(R.drawable.rain_icon),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("Déšť", fontWeight = FontWeight.Thin)
                                                Text("${state.currentWeather?.precipitation} mm", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    Column {
                                        Row(
                                            horizontalArrangement = Arrangement.Center

                                        ){
                                            Box(modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(MaterialTheme.colorScheme.primary),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Icon(
                                                    painter = painterResource(R.drawable.wind),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.size(32.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("Vítr", fontWeight = FontWeight.Thin)
                                                Text("${state.currentWeather?.wind_speed_10m} ${state.weather?.hourly_units?.wind_speed_10m}", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.Center

                                        ){
                                            Box(modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(MaterialTheme.colorScheme.primary),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Icon(
                                                    painter = painterResource(R.drawable.drop),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("Vlhokost", fontWeight = FontWeight.Thin)
                                                Text("${state.currentWeather?.relative_humidity_2m} %", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.Center

                                        ){
                                            Box(modifier = Modifier
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(MaterialTheme.colorScheme.primary),
                                                contentAlignment = Alignment.Center
                                            ){
                                                Icon(
                                                    painter = painterResource(R.drawable.sun_icon),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text("UV Index", fontWeight = FontWeight.Thin)
                                                Text("${state.weather?.daily?.uv_index_max?.first()?.toInt()} / 10", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (state.searchResults != null && state.showSearchBox){
                    AnimatedVisibility(
                        visible = state.showSearchBox && state.searchResults != null,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 300))
                    ){
                        LazyColumn(
                            modifier = Modifier.padding(top = 70.dp).alpha(dropdownVisibility)
                        ) {
                            itemsIndexed(state.searchResults.suggestions){ index, suggestion ->
                                if (index == state.searchResults.suggestions.lastIndex){
                                    DropDownSearchItem(
                                        suggestion = suggestion,
                                        modifier = Modifier.clip(
                                            RoundedCornerShape(
                                                bottomStart = 15.dp,
                                                bottomEnd = 15.dp
                                            )
                                        ),
                                        onClick = {
                                            onEvent(WeatherScreenEvents.setPlace(suggestion.mapbox_id))
                                            onEvent(WeatherScreenEvents.ToggleSearchBox)
                                        }
                                    )
                                } else {
                                    DropDownSearchItem(
                                        suggestion = suggestion,
                                        onClick = {
                                            onEvent(WeatherScreenEvents.setPlace(suggestion.mapbox_id))
                                            onEvent(WeatherScreenEvents.ToggleSearchBox)
                                        }
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