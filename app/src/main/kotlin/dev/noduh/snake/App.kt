package dev.noduh.snake

import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.textLine

fun main() {
    session {
        section {
            val terminalWidth = terminalSize.width
            val terminalHeight = terminalSize.height

            textLine("your terminal is ${terminalWidth}x${terminalHeight}")
        }.run {

        }
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

enum class BoardPiece { // defines what needs to be drawn on the board
    EMPTY, APPLE, HEAD, TAIL, END;

    val isSnake: Boolean
        get() = when (this) {
            HEAD -> true
            TAIL -> true
            END -> true
            else -> false
        }

    val isHead: Boolean
        get() = this == HEAD

    val isTail: Boolean
        get() = this == TAIL

    val isEnd: Boolean
        get() = this == END

    val isApple: Boolean
        get() = this == APPLE

    val isEmpty: Boolean
        get() = this == EMPTY
}

class Snake {
    var length: Int = 2 // this includes head and end
        private set
    var direction: Direction = Direction.EAST
        private set
    var headLocation: Pair<Int, Int> = Pair(0, 0)
        private set
    private val tail: ArrayDeque<Triple<Int, Int, Direction>> = ArrayDeque<Triple<Int, Int, Direction>>()
    private var growing: Int = 0 // zero if not growing, amount left to grow otherwise

    fun grow(lengthToGrow: Int) {
        this.growing = lengthToGrow
    }

    fun turn(tryDirection: Direction) {
        direction = if (tryDirection == direction.opposite) direction else tryDirection
    }

    fun move() {
        tail.addFirst(
            Triple(
                headLocation.first,
                headLocation.second,
                direction
            )
        ) // must happen *before* updating the headLocation
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

    fun getTail(): Array<Triple<Int, Int, Direction>> = tail.toTypedArray() // don't want to pass the mutable data
}

class GameMap(width: Int, height: Int) {
    private val board: Array<Array<BoardPiece>> = Array(width) { Array(height) { BoardPiece.EMPTY } }
    val dimensions: Pair<Int, Int> = Pair(width, height)
    private val snake: Snake = Snake()

    fun spawnApple() {
        val maxX: Int = dimensions.first - 1
        val maxY: Int = dimensions.second - 1
        var tryLocation: Pair<Int, Int> = Pair(-1, -1)
        var pieceAtLocation: BoardPiece = BoardPiece.EMPTY
        do {
            tryLocation = Pair(((0..maxX).random()), ((0..maxY).random()))
            pieceAtLocation = board[tryLocation.first][tryLocation.second]
        } while (pieceAtLocation.isSnake)

        board[tryLocation.first][tryLocation.second] = BoardPiece.APPLE
    }
}

class Game() {
    //
}