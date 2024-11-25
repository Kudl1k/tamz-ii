package cz.kudladev.exec01.core.presentation.screens.sokoban.domain

import android.content.Context
import android.net.Uri
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.LevelLoader

fun openFileFromUri(context: Context, uri: Uri): String? {
    return try {
        val contentResolver = context.contentResolver
        contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun loadLevelsFromFile(context: Context, fileName: Uri): List<LevelLoader> {
    val fileContent = openFileFromUri(context, fileName)

    val levels = mutableListOf<LevelLoader>()
    val levelBlocks = fileContent?.split(Regex("(?m)^Level \\d+"))?.drop(1) // Split by "Level [number]"

    levelBlocks?.forEach { levelBlock ->
        val levelData = levelBlock.trim().split("\n").filter { it.isNotBlank() }

        val width = levelData.maxOfOrNull { it.length } ?: 0
        val height = levelData.size

        val level = IntArray(width * height) { 0 } // Initialize with empty spaces
        var boxes = 0
        levelData.forEachIndexed { rowIndex, row ->
            row.padEnd(width, ' ').forEachIndexed { columnIndex, char -> // Pad rows to match width
                val index = rowIndex * width + columnIndex
                level[index] = when (char) {
                    '#' -> 1 // Wall
                    '$' -> {
                        boxes++
                        2 // Box
                    }
                    '.' -> 3 // Goal
                    '@' -> 4 // Player
                    '*' -> 5 // Player on Goal
                    '+' -> 6 // Box on Goal
                    else -> 0 // Empty space
                }
            }
        }
        printLevel(LevelLoader(level, width, height, boxes))
        levels.add(LevelLoader(level, width, height, boxes))
    }

    return levels
}

fun printLevel(level: LevelLoader) {
    for (y in 0 until level.height) {
        for (x in 0 until level.width) {
            val currentObject = level.level[y * level.width + x]
            print(when (currentObject) {
                0 -> "!" // Empty space
                1 -> "#" // Wall
                2 -> "$" // Box
                3 -> "." // Goal
                4 -> "@" // Player
                5 -> "*" // Player on Goal
                6 -> "+" // Box on Goal
                else -> "?" // Unknown object
            })
        }
        println() // New line after each row
    }
    println("width:${level.width} x height:${level.height}") // New line after each row
    println("n") // New line after each row

}