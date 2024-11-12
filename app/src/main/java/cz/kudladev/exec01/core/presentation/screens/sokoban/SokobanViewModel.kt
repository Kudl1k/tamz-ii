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
                Log.d("SokobanViewModel", "DB levels: $dbLevels")
                val apiLevels = levelsAPI.getLevels()
                Log.d("SokobanViewModel", "API levels: $apiLevels")
                if (apiLevels.size != dbLevels.size) {
                    Log.d("SokobanViewModel", "Inserting levels from API")
                    withContext(Dispatchers.IO) {
                        levelDao.insertLevels(apiLevels.map { it.toLevelDTO() })
                    }
                    _state.value = _state.value.copy(
                        levels = apiLevels,
                        selectedLevel = apiLevels[0],
                    )
                } else {
                    Log.d("SokobanViewModel", "Using levels from DB")
                    _state.value = _state.value.copy(
                        levels = dbLevels,
                        selectedLevel = dbLevels[0],
                    )
                }
            } catch (e: ConnectException) {
                Log.e("SokobanViewModel", "No internet connection")
                val dbLevels = withContext(Dispatchers.IO) {
                    levelDao.getLevels().map { it.toLevel() }
                }
                Log.d("SokobanViewModel", "Using levels from DB")
                _state.value = _state.value.copy(
                    levels = dbLevels,
                    selectedLevel = if (dbLevels.isNotEmpty()) dbLevels[0] else Level(0, IntArray(0), IntArray(0), 0, 0, 0, 0, 0, false, false),
                )
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
                Log.d("SokobanViewModel", "Levels: ${state.value.levels}")
                _state.value = _state.value.copy(
                    selectedLevel = state.value.levels[event.level-1],
                )
                Log.d("SokobanViewModel", "Selected level: ${state.value.selectedLevel}")
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
                    false,
                    event.currentMoves != 0
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
                Log.d("SokobanViewModel", "Saved progress ${event.currentMoves}")
            }
            is SokobanEvent.Win -> {
                viewModelScope.launch {
                    val moves = if (event.currentMoves < state.value.selectedLevel.bestMoves) {
                        Log.d("SokobanViewModel", "New best moves: ${event.currentMoves}")
                        true
                    } else {
                        if (state.value.selectedLevel.bestMoves == 0) {
                            Log.d("SokobanViewModel", "First win")
                            true
                        } else {
                            Log.d("SokobanViewModel", "Best moves: ${state.value.selectedLevel.bestMoves}")
                            false
                        }
                    }
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
                        true,
                        false
                    )
                    withContext(Dispatchers.IO) {
                        levelDao.getLevel(updatedLevel.id).let {
                            if (it.currentMoves < updatedLevel.currentMoves) {
                                levelDao.updateLevel(updatedLevel.toLevelDTO())
                            }
                        }
                    }
                    _state.value = _state.value.copy(
                        selectedLevel = updatedLevel,
                        levels = state.value.levels.map { if (it.id == updatedLevel.id) updatedLevel else it }
                    )
                }



            }

            SokobanEvent.DecreaseHeight -> {
                val currentLevel = state.value.editorLevel.value.level
                val height = state.value.editorLevel.value.height
                val newLevel = IntArray(currentLevel.size - state.value.editorLevel.value.width) { index ->
                    if (index < currentLevel.size - state.value.editorLevel.value.width) {
                        currentLevel[index]
                    } else {
                        0
                    }
                }

                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(
                        state.value.editorLevel.value.copy(
                            height = state.value.editorLevel.value.height - 1,
                            level = newLevel
                        )
                    )
                )
            }
            SokobanEvent.DecreaseWidth -> {
                val currentLevel = state.value.editorLevel.value.level
                val width = state.value.editorLevel.value.width
                val height = state.value.editorLevel.value.height

                // Calculate the new level size
                val newLevelSize = (width - 1) * height

                val newLevel = IntArray(newLevelSize) { index ->
                    val row = index / (width - 1) // Calculate the row index
                    val col = index % (width - 1) // Calculate the column index

                    // Check if the element should be copied from the current level
                    if (col < width) {
                        currentLevel[row * width + col]
                    } else {
                        0 // Insert 0 for new values
                    }
                }

                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(
                        state.value.editorLevel.value.copy(
                            width = state.value.editorLevel.value.width - 1,
                            level = newLevel
                        )
                    )
                )
            }
            SokobanEvent.IncreaseHeight -> {
                val currentLevel = state.value.editorLevel.value.level
                val height = state.value.editorLevel.value.height
                val newLevel = IntArray(currentLevel.size + state.value.editorLevel.value.width) { index ->
                    if (index < currentLevel.size) {
                        currentLevel[index]
                    } else {
                        0
                    }
                }

                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(
                        state.value.editorLevel.value.copy(
                            height = state.value.editorLevel.value.height + 1,
                            level = newLevel
                        )
                    )
                )
            }
            SokobanEvent.IncreaseWidth -> {
                val currentLevel = state.value.editorLevel.value.level
                val width = state.value.editorLevel.value.width
                val height = state.value.editorLevel.value.height

                // Calculate the new level size
                val newLevelSize = (width + 1) * height

                val newLevel = IntArray(newLevelSize) { index ->
                    val row = index / (width + 1) // Calculate the row index
                    val col = index % (width + 1) // Calculate the column index

                    // Check if the element should be copied from the current level
                    if (col < width) {
                        currentLevel[row * width + col]
                    } else {
                        0 // Insert 0 for new values
                    }
                }

                _state.value = _state.value.copy(
                    editorLevel = mutableStateOf(
                        state.value.editorLevel.value.copy(
                            width = state.value.editorLevel.value.width + 1,
                            level = newLevel
                        )
                    )
                )
            }

            is SokobanEvent.UpdateEditLevel -> {
                _state.value.editorLevel.value = _state.value.editorLevel.value.copy(
                    level = event.level
                )
            }

            SokobanEvent.SaveNewLevel -> {
                val newLevel = Level(
                    state.value.levels.size + 1,
                    state.value.editorLevel.value.level,
                    state.value.editorLevel.value.level,
                    state.value.editorLevel.value.boxes,
                    state.value.editorLevel.value.height,
                    state.value.editorLevel.value.width,
                    0,
                    0,
                    false,
                    false
                )
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        levelDao.insertLevel(newLevel.toLevelDTO())
                    }
                    _state.value = _state.value.copy(
                        levels = state.value.levels + newLevel
                    )
                }
            }

            is SokobanEvent.LoadLevels -> {
                Log.d("SokobanViewModel", "Loading levels")
                val allLevels = state.value.levels.toMutableList() // Create a mutable copy
                event.levels.forEach {
                    val level = Level(
                        allLevels.size + 1,
                        it.level,
                        it.level,
                        it.boxes,
                        it.width,
                        it.height,
                        0,
                        0,
                        false,
                        false
                    )
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            levelDao.insertLevel(level.toLevelDTO())
                        }
                    }
                    allLevels.add(level) // Add the new level to the mutable list
                }
                Log.d("SokobanViewModel", "All levels: $allLevels")
                _state.value = _state.value.copy(
                    levels = allLevels
                )
            }
        }
    }
}