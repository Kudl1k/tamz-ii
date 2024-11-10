package cz.kudladev.exec01.core.presentation.screens.scanner_screen.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level
import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Game(private val level: Level) {

    var levelWidth: Int = 10
    var levelHeight: Int = 10
    val currentLevel = mutableStateOf(level.level)
    var currentMoves = mutableStateOf(level.currentMoves)
    val baseLevel = level.baseLevel
    var points = mutableStateOf(0)
    var maxPoints = mutableStateOf(0)
    var player: Player? = null
    var win = mutableStateOf(false)

    init {
        var startingLocation = getPlayerStartingLocation(currentLevel.value)
        player = Player(startingLocation.first, startingLocation.second)
        maxPoints.value = getMaxPoints()
        points.value = getPoints()
        Log.d("Game", "Game initialized")
    }



    private fun getPlayerStartingLocation(level: IntArray): Pair<Int, Int> {
        var x = 0
        var y = 0
        for (i in level.indices) {
            if (level[i] == 4) {
                x = i % 10
                y = i / 10
                break
            }
        }
        return Pair(x,y)
    }

    fun checkMovement(direction: Int) {
        if (player == null || win.value) return

        when (direction) {
            0 -> {
                if (player!!.x + 1 == levelWidth) return
                movePlayer(1, 0)
            }
            1 -> {
                if (player!!.x - 1 < 0) return
                movePlayer(-1, 0)
            }
            2 -> {
                if (player!!.y - 1 < 0) return
                movePlayer(0, -1)
            }
            3 -> {
                if (player!!.y + 1 == levelHeight) return
                movePlayer(0, 1)
            }
        }
    }

    fun restart(){
        val startingLocation = getPlayerStartingLocation(baseLevel)
        player = Player(startingLocation.first, startingLocation.second)
        currentLevel.value = baseLevel
        points.value = 0
        win.value = false
        currentMoves.value = 0
        CoroutineScope(Dispatchers.Main).launch {
            maxPoints.value = getMaxPoints()
        }
    }

    private fun movePlayer(dx: Int, dy: Int) {
        val nextX = player!!.x + dx
        val nextY = player!!.y + dy
        val nextPositionIndex = nextY * levelWidth + nextX

        when (currentLevel.value[nextPositionIndex]) {
            0 -> {
                currentMoves.value += 1
                updateLevel(nextX, nextY)
                Log.d("Game", "Moved to empty space")
            }
            2, 5 -> {
                val nextBoxX = player!!.x + 2 * dx
                val nextBoxY = player!!.y + 2 * dy
                val nextBoxPositionIndex = nextBoxY * levelWidth + nextBoxX

                if (nextBoxX in 0 until levelWidth && nextBoxY in 0 until levelHeight) {
                    when (currentLevel.value[nextBoxPositionIndex]) {
                        0 -> {
                            currentMoves.value += 1
                            updateLevel(nextX, nextY, nextBoxX, nextBoxY, currentLevel.value[nextPositionIndex])
                            Log.d("Game", "Moved box to empty space")
                        }
                        3 -> {
                            currentMoves.value += 1
                            updateLevel(nextX, nextY, nextBoxX, nextBoxY, 5)
                            Log.d("Game", "Moved box to goal")
                        }
                        else -> return
                    }
                } else {
                    return
                }
            }
            1 -> return
            else -> {
                updateLevel(nextX, nextY)
                Log.d("Game", "Moved")
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            win.value = checkWin()
            points.value = checkPoints()
        }
    }

    private fun updateLevel(nextX: Int, nextY: Int, nextBoxX: Int = -1, nextBoxY: Int = -1, boxType: Int = -1) {
        //6 player_on_goal
        //5 box_ok
        //4 player
        //3 goal
        //2 box
        //1 wall
        //0 empty

        //0 right
        //1 left
        //2 up
        //3 down

        val updatedLevel = currentLevel.value.clone()
        val currentPlayerPositionIndex = player!!.y * levelWidth + player!!.x
        val nextPlayerPositionIndex = nextY * levelWidth + nextX

        val isCurrentPlayerPositionPlayerOnGoal = currentLevel.value[currentPlayerPositionIndex] == 6

        val isNextPlayerPositionGoal = currentLevel.value[nextPlayerPositionIndex] == 3

        updatedLevel[currentPlayerPositionIndex] = if (isCurrentPlayerPositionPlayerOnGoal) 3 else 0
        updatedLevel[nextPlayerPositionIndex] = if (isNextPlayerPositionGoal) 6 else 4

        if (nextBoxX != -1 && nextBoxY != -1) {
            val currentBoxPositionIndex = nextPlayerPositionIndex
            val nextBoxPositionIndex = nextBoxY * levelWidth + nextBoxX

            val isGoalAtTargetBoxPosition = currentLevel.value[nextBoxPositionIndex] == 3

            updatedLevel[currentPlayerPositionIndex] = if (isCurrentPlayerPositionPlayerOnGoal) 3 else 0
            updatedLevel[currentBoxPositionIndex] = 4

            updatedLevel[nextBoxPositionIndex] = if (isGoalAtTargetBoxPosition) 5 else 2

            if (currentLevel.value[currentBoxPositionIndex] == 5) {
                updatedLevel[currentBoxPositionIndex] = 6
            }
        }

        currentLevel.value = updatedLevel
        player!!.x = nextX
        player!!.y = nextY
    }

    private fun checkWin(): Boolean {
        for (i in currentLevel.value.indices) {
            if (currentLevel.value[i] == 3) {
                return false
            }
        }
        return true
    }

    private fun checkPoints(): Int {
        var points = 0
        for (i in currentLevel.value.indices) {
            if (currentLevel.value[i] == 5) {
                points++
            }
        }
        return points
    }

    private fun getMaxPoints(): Int {
        var points = 0
        for (i in baseLevel.indices) {
            if (baseLevel[i] == 3) {
                points++
            }
        }
        return points
    }

    private fun getPoints(): Int {
        var points = 0
        for (i in currentLevel.value.indices) {
            if (currentLevel.value[i] == 5) {
                points++
            }
        }
        return points
    }
}