package cz.kudladev.exec01.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import cz.kudladev.exec01.core.presentation.screens.HomeScreen
import cz.kudladev.exec01.core.presentation.screens.scanner_screen.ScannerScreen
import cz.kudladev.exec01.core.presentation.screens.api_screen.APIScreen
import cz.kudladev.exec01.core.presentation.screens.api_screen.APIScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.investment_calculator.InvestmentCalcHistory
import cz.kudladev.exec01.core.presentation.screens.weather.WeatherScreen
import cz.kudladev.exec01.core.presentation.screens.weather.WeatherScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.investment_calculator.InvestmentCalculatorViewModel
import cz.kudladev.exec01.core.presentation.screens.investment_calculator.InvestmentCalculator
import cz.kudladev.exec01.core.presentation.screens.scanner_screen.ScannerScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationHost(
    navHostController: NavHostController
) {
    val investmentViewmodel: InvestmentCalculatorViewModel = koinViewModel()
    val investmentState by investmentViewmodel.state.collectAsState()
    val investmentOnEvent = investmentViewmodel::onEvent
    NavHost(
        navController = navHostController,
        startDestination = Routes.Root.route
    ) {
        navigation(
            startDestination = Routes.Home.route,
            route = Routes.Root.route
        ){
            composable(
                route = Routes.Home.route
            ){
                HomeScreen(
                    navController = navHostController
                )
            }
            navigation(
                startDestination = Routes.InvestmentCalc.route,
                route = Routes.InvestmentNav.route
            ) {
                composable(route = Routes.InvestmentCalc.route) {

                    InvestmentCalculator(
                        navController = navHostController,
                        state = investmentState,
                        onEvent = investmentOnEvent
                    )
                }
                composable(route = Routes.InvestmentCalcHistory.route){


                    InvestmentCalcHistory(
                        navController = navHostController,
                        state = investmentState,
                        onEvent = investmentOnEvent
                    )
                }
            }
            composable(route = Routes.Scanner.route){
                val viewModel = koinViewModel<ScannerScreenViewModel>()
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent

                ScannerScreen(
                    navController = navHostController,
                    state = state,
                    onEvent = onEvent
                )
            }
            composable(route = Routes.API.route){
                val viewModel: APIScreenViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent
                APIScreen(navController = navHostController, state = state, onEvent = onEvent)
            }
            composable(route = Routes.Weather.route){
                val viewModel: WeatherScreenViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent
                WeatherScreen(navController = navHostController, state = state, onEvent = onEvent)
            }
        }

    }

}