package dev.noduh.snake

import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.textLine

fun main() {
    session {
        section {
            val terminalWidth = terminalSize.width
            val terminalHeight = terminalSize.height
            textLine("your terminal is ${terminalWidth}x${terminalHeight}")
        }.run()
    }
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    val opposite: Direction
        get() = when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
        }

    val xDirection: Int
        get() = when (this) {
            EAST -> 1
            WEST -> -1
            else -> 0
        }

    val yDirection: Int
        get() = when (this) {
            SOUTH -> 1
            NORTH -> -1
            else -> 0
        }
}

class Snake {
    var length: Int = 0
        private set
    var direction: Direction = Direction.EAST
        private set
    var headLocation: Pair<Int, Int> = Pair(0, 0)
        private set
    private val tail: ArrayDeque<Pair<Int, Int>> = ArrayDeque<Pair<Int, Int>>()
    private var growing: Int = 0 // zero if not growing, amount left to grow otherwise

    fun grow(lengthToGrow: Int) {
        this.growing = lengthToGrow
    }

    fun turn(tryDirection: Direction) {
        direction = if (tryDirection == direction.opposite) direction else tryDirection
    }

    fun move() {
        tail.addFirst(headLocation) // must happen *before* updating the headLocation
        headLocation = Pair(
            headLocation.first + direction.xDirection,
            headLocation.second + direction.yDirection
        ) // get the direction it's moving and add it to the current coordinates

        if (growing != 0) {
            growing--
        } else {
            tail.removeLastOrNull()
        }
    }

    fun eat(growAmount: Int) {
        growing += growAmount
    }
}

class GameMap(width: Int, height: Int) {
    private val board: Array<Array<Int>> = Array(width) { Array(height) { 0 } }

}

class Game() {
    //
}