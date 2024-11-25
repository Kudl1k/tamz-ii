package cz.kudladev.tamziikmp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(val route: String, val title: String? = null, val icon: ImageVector? = null) {

    data object Home : Routes(route = "home", title = "TAMZ II - KUD0132", icon = Icons.Default.Home)

    data object Root : Routes(route = "root")

    data object InvestmentNav : Routes(route = "investment_nav", title = "Investiční kalkulačka", icon = Icons.Default.Warning)
    data object InvestmentCalc : Routes(route = "investment_calc", title = "Investiční kalkulačka", icon = Icons.Default.Warning)
    data object InvestmentCalcHistory : Routes(route = "investment_calc_history", title = "Historie", icon = Icons.Default.Warning)
    data object Weather: Routes(route = "weather", title = "Počasí", icon = Icons.Default.Star)

    data object Scanner : Routes(route = "scanner", title = "Scanner", icon = Icons.Default.Phone)
    data object API: Routes(route = "api", title = "API", icon = Icons.Default.Build)

    data object Sokoban : Routes(route = "sokoban", title = "Sokoban", icon = Icons.Default.AddCircle)
    data object SokobanMain : Routes(route = "sokoban_main", title = "Level select", icon = Icons.Default.AddCircle)
    data object SokobanGame : Routes(route = "sokoban_game", title = "Game", icon = Icons.Default.AddCircle)
    data object SokobanEdit : Routes(route = "sokoban_edit", title = "Editor", icon = Icons.Default.Edit)

}