package cz.kudladev.exec01.core.presentation.screens.sokoban.data


data class Level(
    val id: Int,
    var level: IntArray,
    val boxes: Int,
    val width: Int,
    val height: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Level

        if (id != other.id) return false
        if (!level.contentEquals(other.level)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + level.contentHashCode()
        return result
    }
}
