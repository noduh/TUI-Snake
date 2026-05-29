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
