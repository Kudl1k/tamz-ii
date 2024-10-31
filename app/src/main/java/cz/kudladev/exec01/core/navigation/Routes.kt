package cz.kudladev.exec01.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(val route: String, val title: String? = null, val icon: ImageVector? = null) {

    data object Home : Routes(route = "home", title = "TAMZ II - KUD0132", icon = Icons.Default.Home)

    data object Root : Routes(route = "root")

    data object InvestmentNav : Routes(route = "investment_nav", title = "Investiční kalkulačka", icon = Icons.Default.Calculate)
    data object InvestmentCalc : Routes(route = "investment_calc", title = "Investiční kalkulačka", icon = Icons.Default.Calculate)
    data object InvestmentCalcHistory : Routes(route = "investment_calc_history", title = "Historie", icon = Icons.Default.History)
    data object Weather: Routes(route = "weather", title = "Počasí", icon = Icons.Default.Cloud)

    data object Scanner : Routes(route = "scanner", title = "Scanner", icon = Icons.Default.QrCodeScanner)
    data object API: Routes(route = "api", title = "API", icon = Icons.Default.Api)

    data object Sokoban : Routes(route = "sokoban", title = "Sokoban", icon = Icons.Default.Gamepad)

}