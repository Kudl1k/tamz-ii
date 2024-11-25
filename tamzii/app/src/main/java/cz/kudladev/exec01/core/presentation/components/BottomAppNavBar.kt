package cz.kudladev.exec01.core.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cz.kudladev.exec01.core.navigation.Routes

@Composable
fun BottomAppNavBar(modifier: Modifier = Modifier, navController: NavController) {
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = navController.currentDestination?.route == Routes.InvestmentCalc.route,
            onClick = {
                navController.navigate(Routes.InvestmentCalc.route){
                    popUpTo(Routes.Root.route){
                        inclusive = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Routes.InvestmentCalc.icon!!,
                    contentDescription = Routes.InvestmentCalc.title
                )
            },
            label = {
                Routes.InvestmentCalc.title?.let {
                    Text(
                        text = it
                    )
                }
            }
        )
        NavigationBarItem(
            selected = navController.currentDestination?.route == Routes.Scanner.route,
            onClick = {
                navController.navigate(Routes.Scanner.route){
                    popUpTo(Routes.Root.route){
                        inclusive = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Routes.Scanner.icon!!,
                    contentDescription = Routes.Scanner.title
                )
            },
            label = {
                Routes.Scanner.title?.let {
                    Text(
                        text = it
                    )
                }
            }
        )
        NavigationBarItem(
            selected = navController.currentDestination?.route == Routes.API.route,
            onClick = {
                navController.navigate(Routes.API.route){
                    popUpTo(Routes.Root.route){
                        inclusive = true
                    }
                }
            },
            icon = {
                Icon(
                    imageVector = Routes.API.icon!!,
                    contentDescription = Routes.API.title
                )
            },
            label = {
                Routes.API.title?.let {
                    Text(
                        text = it
                    )
                }
            }
        )
    }

}