package cz.kudladev.exec01.core.presentation.screens.sokoban

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.reflect.TypeToken
import cz.kudladev.exec01.core.data.api.LevelsAPI
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.dao.LevelDao
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.mappers.toLevel
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.mappers.toLevelDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import java.net.ConnectException

class SokobanViewModel(
    private val levelsAPI: LevelsAPI,
    private val levelDao: LevelDao
): ViewModel() {

    private val _state = MutableStateFlow(SokobanState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val dbLevels = withContext(Dispatchers.IO) {
                    levelDao.getLevels().map { it.toLevel() }
                }
                val apiLevels = levelsAPI.getLevels()
                if (apiLevels.size != dbLevels.size) {
                    withContext(Dispatchers.IO) {
                        levelDao.insertLevels(apiLevels.map { it.toLevelDTO() })
                    }
                    _state.value = _state.value.copy(
                        levels = apiLevels,
                        selectedLevel = apiLevels[0],
                    )
                } else {
                    _state.value = _state.value.copy(
                        levels = dbLevels,
                        selectedLevel = dbLevels[0],
                    )
                }
            } catch (e: ConnectException) {
                Log.e("SokobanViewModel", "No internet connection")
            }
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

            is SokobanEvent.SaveProgress -> {
                Log.d("SokobanViewModel", "Saving progress ${event.currentMoves}")
                val updatedLevel = Level(
                    state.value.selectedLevel.id,
                    state.value.selectedLevel.baseLevel,
                    event.level,
                    state.value.selectedLevel.boxes,
                    state.value.selectedLevel.height,
                    state.value.selectedLevel.width,
                    event.currentMoves,
                    state.value.selectedLevel.bestMoves,
                    true
                )
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        levelDao.updateLevel(updatedLevel.toLevelDTO())
                    }
                }
                _state.value = _state.value.copy(
                    selectedLevel = updatedLevel,
                    levels = state.value.levels.map { if (it.id == updatedLevel.id) updatedLevel else it }
                )
            }
            is SokobanEvent.Win -> {
                viewModelScope.launch {
                    val moves = event.currentMoves < state.value.selectedLevel.bestMoves
                    Log.d("SokobanViewModel", "Moves: ${event.currentMoves}")
                    val updatedLevel = Level(
                        state.value.selectedLevel.id,
                        state.value.selectedLevel.baseLevel,
                        event.level,
                        state.value.selectedLevel.boxes,
                        state.value.selectedLevel.height,
                        state.value.selectedLevel.width,
                        event.currentMoves,
                        if (moves) event.currentMoves else state.value.selectedLevel.bestMoves,
                        false
                    )
                    withContext(Dispatchers.IO) {
                        levelDao.getLevel(updatedLevel.id).let {
                            if (it.currentMoves < updatedLevel.currentMoves) {
                                levelDao.updateLevel(updatedLevel.toLevelDTO())
                            }
                        }
                    }
                }



            }

            SokobanEvent.DecreaseHeight -> {
                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(state.value.editorLevel.value.copy(height = state.value.editorLevel.value.height - 1))
                )
            }
            SokobanEvent.DecreaseWidth -> {
                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(state.value.editorLevel.value.copy(width = state.value.editorLevel.value.width - 1))
                )
            }
            SokobanEvent.IncreaseHeight -> {
                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(state.value.editorLevel.value.copy(height = state.value.editorLevel.value.height + 1))
                )
            }
            SokobanEvent.IncreaseWidth -> {
                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(state.value.editorLevel.value.copy(width = state.value.editorLevel.value.width + 1))
                )
            }
        }
    }
}