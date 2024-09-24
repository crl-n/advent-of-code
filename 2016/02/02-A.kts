import java.io.File

val input = File("./02/02.input").readLines()

val keypad = (1..9).toList()
var keypadPointer = keypad.indexOf(5)

fun moveUp(keypadPointer: Int): Int {
    val newPos = keypadPointer - 3
    if (newPos >= 0) return newPos
    return keypadPointer
}

fun moveDown(keypadPointer: Int): Int {
    val newPos = keypadPointer + 3
    if (newPos <= 8) return newPos
    return keypadPointer
}

fun moveRight(keypadPointer: Int): Int {
    if ((keypadPointer + 1) % 3 == 0) return keypadPointer
    if (keypadPointer == 8) return keypadPointer
    return keypadPointer + 1
}

fun moveLeft(keypadPointer: Int): Int {
    if (keypadPointer % 3 == 0) return keypadPointer
    if (keypadPointer == 0) return keypadPointer
    return keypadPointer - 1
}

var solution = ""
for (line in input) {
    for (c in line) {
        when (c) {
            'U' -> keypadPointer = moveUp(keypadPointer)
            'R' -> keypadPointer = moveRight(keypadPointer)
            'D' -> keypadPointer = moveDown(keypadPointer)
            'L' -> keypadPointer = moveLeft(keypadPointer)
            else -> {}
        }
    }
    solution += keypad[keypadPointer]
}

println("Solution: $solution")
