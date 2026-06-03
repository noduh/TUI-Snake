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

class Snake {
    var length: Int = 0
        private set
    var direction: Direction = Direction.EAST
        private set
    var headLocation: Pair<Int, Int> = Pair(0, 0)
        private set
    private val tail: MutableList<Pair<Int, Int>> = mutableListOf<Pair<Int, Int>>()
    private var growing: Int = 0 // zero if not growing, amount left to grow otherwise

    fun grow(lengthToGrow: Int) {
        this.growing = lengthToGrow
    }
}

class GameBoard(width: Int, height: Int) {
    private val board: Array<Array<Int>> = Array(width) { Array(height) { 0 } }

}