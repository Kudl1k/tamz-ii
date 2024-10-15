package cz.kudladev.exec01.core.presentation.screens.inputs_screen

sealed class InputScreenEvents {

    data class setBalance(val balance: Double): InputScreenEvents()
    data class setInterest(val interest: Double): InputScreenEvents()
    data class setLength(val length: Int): InputScreenEvents()

    object ToggleChartType: InputScreenEvents()
}