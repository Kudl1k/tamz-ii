package cz.kudladev.exec01.core.presentation.screens.inputs_screen

sealed class InputScreenEvents {

    data class setLogin(val login: String) : InputScreenEvents()
    data class setPassword(val password: String) : InputScreenEvents()
    object toggleVisibility : InputScreenEvents()
    object submit : InputScreenEvents()
}