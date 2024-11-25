package cz.kudladev.tamziikmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreen
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val weatherViewmodel: WeatherScreenViewModel = koinViewModel()
    val weatherState by weatherViewmodel.state.collectAsState()
    val weatherOnEvent = weatherViewmodel::onEvent

    NavHost(
        navController = navController,
        startDestination = Routes.Weather.route
    ){
        composable(Routes.Weather.route){
            WeatherScreen(
                state = weatherState,
                onEvent = weatherOnEvent
            )
        }
    }
}