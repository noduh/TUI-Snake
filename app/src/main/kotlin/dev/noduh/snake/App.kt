package dev.noduh.snake

import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.textLine

fun main() {
    session {
        section {
            val terminalWidth = terminalSize.width
            val terminalHeight = terminalSize.height
            val gameMap = GameMap(terminalWidth, terminalHeight)


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

        length++ // I almost forgot to add this part
    }

    fun eat(growAmount: Int) {
        growing += growAmount
    }

    fun getTail(): Array<Triple<Int, Int, Direction>> = tail.toTypedArray() // don't want to pass the mutable data
}

class GameMap(width: Int, height: Int) {
    val growAmount = 1
    private val board: Array<Array<BoardPiece>> = Array(width) { Array(height) { BoardPiece.EMPTY } }
    val dimensions: Pair<Int, Int> = Pair(width, height)
    private val snake: Snake = Snake()

    private fun spawnApple() {
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

    private fun didHeadCrash(): Boolean { // if true, the game should end
        val headLocationX: Int = snake.headLocation.first
        val headLocationY: Int = snake.headLocation.second

        if (headLocationX >= dimensions.first || headLocationX < 0 || headLocationY >= dimensions.second || headLocationY < 0) { // out of bounds check
            return true
        }

        val pieceToCheck: BoardPiece = board[headLocationX][headLocationY]

        return pieceToCheck.isSnake // does it hit itself
    }

    private fun didHeadEat(): Boolean =
        board[snake.headLocation.first][snake.headLocation.second].isApple // is the head where an apple was

    fun getScore(): Int = snake.length // it will be minimum of 2 but that's what I want

    fun updateBoard(): Boolean { // if it's false, game is over
        val snakeTail = snake.getTail()

        snake.move()

        // checking the collisions
        if (didHeadCrash()) {
            return false
        }
        if (didHeadEat()) {
            snake.eat(growAmount)
            spawnApple()
        }

        // clearing board (except the apple)
        for (column in 0..<dimensions.first) {
            for (row in 0..<dimensions.second) {
                if (!board[column][row].isApple) {
                    board[column][row] = BoardPiece.EMPTY
                }
            }
        }

        // redraw the snake
        board[snake.headLocation.first][snake.headLocation.second] = BoardPiece.HEAD
        for (piece in snakeTail) {
            board[piece.first][piece.second] = BoardPiece.TAIL
        }
        board[snakeTail[snakeTail.lastIndex].first][snakeTail[snakeTail.lastIndex].second] = BoardPiece.END

        // hopefully everything worked
        return true
    }

    fun updateBoard(moveDirection: Direction): Boolean {
        snake.turn(moveDirection)

        return updateBoard()
    }

    fun getDrawableBoard(): Array<String> {
        val arrayLength =
            dimensions.first * dimensions.second + (dimensions.second - 1) // must include newline character for every row EXCEPT the last
        val drawableArray: Array<String> = Array(arrayLength) { " " }
        for (i in 0..<dimensions.second) { // at the end of each row (except last), put a newline
            drawableArray[i * dimensions.first] = "\n"
        }

        // fill in the array with the correct characters
        for (j in 0..<dimensions.second) { // for each row
            var arrayLocation = 0
            for (i in 0..<dimensions.first) { // for each character in the row
                val pieceAtLocation = board[i][j]
                if (pieceAtLocation != BoardPiece.EMPTY) { // empty was how the drawable was filled
                    drawableArray[arrayLocation] = when (pieceAtLocation) {
                        BoardPiece.HEAD -> "⬤"
                        BoardPiece.TAIL -> "#"
                        BoardPiece.END -> "+"
                        BoardPiece.APPLE -> "🍎"
                    }
                }
                arrayLocation++ // gotta increment each time
            }
            arrayLocation++ // increment an extra time at the end of each row to skip newline
        }
        
        return drawableArray
    }
}
