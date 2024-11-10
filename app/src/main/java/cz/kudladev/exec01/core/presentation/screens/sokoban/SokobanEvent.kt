package cz.kudladev.exec01.core.presentation.screens.sokoban

sealed class SokobanEvent {

    data class SelectLevel(val level: Int) : SokobanEvent()
    data object Exit : SokobanEvent()

    data class SaveProgress(val level: IntArray, val currentMoves: Int) : SokobanEvent()
    data class Win(val level: IntArray, val currentMoves: Int) : SokobanEvent()

    data object DecreaseWidth: SokobanEvent()
    data object IncreaseWidth: SokobanEvent()

    data object DecreaseHeight: SokobanEvent()
    data object IncreaseHeight: SokobanEvent()

}