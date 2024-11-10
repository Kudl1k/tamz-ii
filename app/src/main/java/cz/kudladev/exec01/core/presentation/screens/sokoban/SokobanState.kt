package cz.kudladev.exec01.core.presentation.screens.sokoban

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.level1

data class SokobanState(
    val selectedLevel: Level = Level(0, level1, level1, 10, 10,10, 0,0,false),
    val levels: List<Level> = emptyList(),
    val editorLevel: MutableState<Level> = mutableStateOf(Level(0, level1, level1, 10, 10,10, 0,0,false)),
)
