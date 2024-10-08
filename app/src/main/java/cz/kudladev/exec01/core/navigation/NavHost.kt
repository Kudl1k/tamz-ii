package cz.kudladev.exec01.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import cz.kudladev.exec01.core.presentation.screens.scanner_screen.ScannerScreen
import cz.kudladev.exec01.core.presentation.screens.api_screen.APIScreen
import cz.kudladev.exec01.core.presentation.screens.api_screen.APIScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.graphs.GraphsScreen
import cz.kudladev.exec01.core.presentation.screens.graphs.GraphsScreenViewModel
import cz.kudladev.exec01.core.presentation.screens.inputs_screen.InputScreenViewmodel
import cz.kudladev.exec01.core.presentation.screens.inputs_screen.InputsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.Root.route
    ) {
        navigation(
            startDestination = Routes.Inputs.route,
            route = Routes.Root.route
        ){
            composable(route = Routes.Inputs.route){
                val viewModel: InputScreenViewmodel = koinViewModel()
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent
                InputsScreen(navController = navHostController, state = state, onEvent = onEvent )
            }
            composable(route = Routes.Scanner.route){
                ScannerScreen(navController = navHostController)
            }
            composable(route = Routes.API.route){
                val viewModel: APIScreenViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent
                APIScreen(navController = navHostController, state = state, onEvent = onEvent)
            }
            composable(route = Routes.Graphs.route){
                val viewModel: GraphsScreenViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                val onEvent = viewModel::onEvent
                GraphsScreen(navController = navHostController, state = state, onEvent = onEvent)
            }
        }

    }

}