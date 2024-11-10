package cz.kudladev.exec01.core.presentation.screens.sokoban

sealed class SokobanEvent {

    data class SelectLevel(val level: Int) : SokobanEvent()
    data object Exit : SokobanEvent()

}