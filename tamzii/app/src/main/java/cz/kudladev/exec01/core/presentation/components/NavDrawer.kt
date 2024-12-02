package cz.kudladev.exec01.core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.kudladev.exec01.core.navigation.Routes

@Composable
fun NavDrawer(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState,
    screenContent: @Composable () -> Unit
) {
    val routes = listOf(
        Routes.InvestmentNav,
        Routes.Scanner,
        Routes.Sokoban,
        Routes.Weather,
        Routes.FaceRecognition
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("TAMZ II - KUD0132", modifier = Modifier.padding(16.dp).weight(1f))
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.Root.route) {
                                popUpTo(Routes.Root.route) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Routes.Home.icon!!,
                            contentDescription = Routes.Home.title
                        )
                    }
                }
                HorizontalDivider()
                for (route in routes) {
                    NavigationDrawerItem(
                        modifier = Modifier.padding(6.dp),
                        label = { Text(route.title!!) },
                        selected = navController.currentDestination?.route == route.route,
                        onClick = {
                            navController.navigate(route.route){
                                popUpTo(Routes.Root.route){
                                    inclusive = true
                                }
                            }
                        },
                        icon = {
                            route.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = route.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {
        screenContent()
    }
}