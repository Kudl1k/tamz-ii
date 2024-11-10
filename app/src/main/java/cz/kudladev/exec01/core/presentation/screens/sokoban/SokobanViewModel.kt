package cz.kudladev.exec01.core.presentation.screens.sokoban

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kudladev.exec01.core.data.api.LevelsAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SokobanViewModel(
    private val levelsAPI: LevelsAPI
): ViewModel() {

    private val _state = MutableStateFlow(SokobanState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            levelsAPI.getLevels().let { levels ->
                _state.value = state.value.copy(
                    levels = levels
                )
            }
            println("Levels loaded: ${state.value.levels}")
        }
    }

    fun onEvent(event: SokobanEvent) {
        when(event) {
            SokobanEvent.Exit -> {
                _state.value = _state.value.copy(
                    selectedLevel = state.value.levels[0],
                )
            }
            is SokobanEvent.SelectLevel -> {
                _state.value = _state.value.copy(
                    selectedLevel = state.value.levels[event.level-1],
                )
            }
        }
    }
}