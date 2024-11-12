package cz.kudladev.exec01.core.presentation.screens.sokoban.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class LevelDTO(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "base_level") val baseLevel: IntArray,
    @ColumnInfo(name = "level") val level: IntArray,
    @ColumnInfo(name = "boxes") val boxes: Int,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "current_moves") val currentMoves: Int,
    @ColumnInfo(name = "best_moves") val bestMoves: Int,
    @ColumnInfo(name = "completed") val completed: Boolean,
    @ColumnInfo(name = "in_progress") val inProgress: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LevelDTO

        if (id != other.id) return false
        if (!baseLevel.contentEquals(other.baseLevel)) return false
        if (!level.contentEquals(other.level)) return false
        if (boxes != other.boxes) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (currentMoves != other.currentMoves) return false
        if (bestMoves != other.bestMoves) return false
        if (completed != other.completed) return false
        if (inProgress != other.inProgress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + baseLevel.contentHashCode()
        result = 31 * result + level.contentHashCode()
        result = 31 * result + boxes
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + currentMoves
        result = 31 * result + bestMoves
        result = 31 * result + completed.hashCode()
        result = 31 * result + inProgress.hashCode()
        return result
    }
}
