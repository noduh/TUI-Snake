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
    NORTH, EAST, SOUTH, WEST
}

// work on encapsulation

class Snake {
    var length: Int = 0
    var direction: Direction = Direction.EAST
    var headLocation: Pair<Int, Int> = Pair(0, 0)
    val tail: MutableList<Pair<Int, Int>> = mutableListOf<Pair<Int, Int>>()
}

class GameBoard(val width: Int, val height: Int) {
    val board: Array<Array<Int>> // figure out how to initialize this
}