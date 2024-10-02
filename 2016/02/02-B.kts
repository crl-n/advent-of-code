import java.io.File

val input = File("./02/02.input").readLines()

val keys = mutableListOf(
    '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D'
)
val keysPerRow = listOf(1, 3, 5, 3, 1)
val keypad = keysPerRow.map { value ->
    val emptyKeysPerSide = (5 - value) / 2
    val side = List<Char?>(emptyKeysPerSide) { null }
    val middle = (1..value).map { keys.removeFirst() }
    side.plus(middle).plus(side)
}

data class Coord(var x: Int, var y: Int) {
    fun up() {
        if (y == 0 || keypad[y - 1][x] == null) return
        y--
    }

    fun down() {
        if (y == 4 || keypad[y + 1][x] == null) return
        y++
    }

    fun right() {
        if (x == 4 || keypad[y][x + 1] == null) return
        x++
    }

    fun left() {
        if (x == 0 || keypad[y][x - 1] == null) return
        x--
    }
}

var keypadPointer = Coord(0, 2)
var solution = ""

for (line in input) {
    for (c in line) {
        when (c) {
            'U' -> keypadPointer.up()
            'R' -> keypadPointer.right()
            'D' -> keypadPointer.down()
            'L' -> keypadPointer.left()
            else -> {}
        }
    }
    solution += keypad[keypadPointer.y][keypadPointer.x]
}

println("Solution: $solution")
