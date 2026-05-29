package dev.noduh.snake

import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.textLine

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    session {
        section {
            textLine(App().greeting)
        }.run()
    }
}
