package cz.kudladev.exec01.core.presentation.screens.sokoban.db.mappers

import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.entity.LevelDTO


fun Level.toLevelDTO(): LevelDTO {
    return LevelDTO(
        id = id,
        boxes = boxes,
        level = level,
        baseLevel = baseLevel,
        width = width,
        height = height,
        currentMoves = currentMoves,
        bestMoves = bestMoves,
        inProgress = inProgress
    )
}

fun LevelDTO.toLevel(): Level {
    return Level(
        id = id,
        boxes = boxes,
        level = level,
        baseLevel = baseLevel,
        width = width,
        height = height,
        currentMoves = currentMoves,
        bestMoves = bestMoves,
        inProgress = inProgress
    )
}

