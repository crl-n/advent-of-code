import java.io.File
import kotlin.math.abs

val input = File("14.input").readLines()

val width = 101
val height = 103

fun wrapAround(v: Int, modulus: Int): Int {
    if (v < 0) return ((v % modulus) + modulus) % modulus
    return v % modulus
}

data class Pos(var x: Int, var y: Int)

data class Bot(var pos: Pos, val v: Pos) {
    fun posAfterSeconds(seconds: Int): Pos {
        var x2 = wrapAround(pos.x + v.x * seconds, width)
        var y2 = wrapAround(pos.y + v.y * seconds, height)

        return Pos(x2, y2)
    }
}

// Parse
fun parseInts(line: String): List<Int> {
    return Regex("-?\\d+").findAll(line).map { it.value.toInt() }.toList()
}

val bots = input.map { line -> parseInts(line) }
    .map { (x, y, vx, vy) -> Bot(Pos(x, y), Pos(vx, vy)) }

// Solve by printing grid for each increment
//
// By inspecting output for a while, you notice a pattern of converging positions
// around certain values of i. The step between these turned out to be the height,
// so we start from one of these situations and keep incrementing with 103
var i = 5780 // Start value might need to be lower for some inputs
while (true) {
    val endPositions = bots.map { it.posAfterSeconds(i) }

    val chars = (0..height).map { (0..width).map { '.' }.toMutableList() }
    for (pos in endPositions) {
        chars[pos.y][pos.x] = '#'
    }
    val output = chars.map { it.joinToString("") }.joinToString("\n")

    println("i: $i")
    println(output)
    println()
    val key = readln()
    if (key == "q") break

    i += 103
}
