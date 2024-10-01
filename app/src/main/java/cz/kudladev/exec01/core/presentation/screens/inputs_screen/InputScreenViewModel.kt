package cz.kudladev.exec01.core.presentation.screens.inputs_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InputScreenViewmodel(
): ViewModel() {

    private val _state = MutableStateFlow(InputScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: InputScreenEvents) {
        when (event) {
            is InputScreenEvents.setLogin -> {
                _state.value = _state.value.copy(
                    login = event.login
                )
            }
            is InputScreenEvents.setPassword -> {
                _state.value = _state.value.copy(
                    password = event.password
                )
            }
            InputScreenEvents.submit -> {
                _state.value = _state.value.copy(
                    login = "",
                    password = ""
                )
            }

            InputScreenEvents.toggleVisibility -> {
                _state.value = _state.value.copy(
                    passwordVisible = !_state.value.passwordVisible
                )

            }
        }
    }


}