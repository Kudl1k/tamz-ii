package cz.kudladev.exec01.core.presentation.screens.sokoban.db.converters

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntArray(value: IntArray): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntArray(value: String): IntArray {
        return value.split(",").map { it.toInt() }.toIntArray()
    }
}