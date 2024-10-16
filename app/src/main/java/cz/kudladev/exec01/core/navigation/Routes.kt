package cz.kudladev.exec01.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(val route: String, val title: String? = null, val icon: ImageVector? = null) {

    data object Root : Routes(route = "root")

    data object InvestmentCalc : Routes(route = "investment_calc", title = "Investiční kalkulačka", icon = Icons.Default.Calculate)
    data object Weather: Routes(route = "weather", title = "Počasí", icon = Icons.Default.Cloud)

    data object Scanner : Routes(route = "scanner", title = "Scanner", icon = Icons.Default.QrCodeScanner)
    data object API: Routes(route = "api", title = "API", icon = Icons.Default.Api)

}