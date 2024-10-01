package cz.kudladev.exec01.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
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
        Routes.Inputs,
        Routes.Graphs,
        Routes.API,
        Routes.Scanner,
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("TAMZ II - KUD0132", modifier = Modifier.padding(16.dp))
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