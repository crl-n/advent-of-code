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

// Solve
val midY = height / 2
val midX = width / 2

val endPositions = bots.map { it.posAfterSeconds(100) }

val quadrantPredicates = listOf<(Pos) -> Boolean>(
    { (x, y) -> x < midX && y < midY },
    { (x, y) -> x < midX && y > midY },
    { (x, y) -> x > midX && y < midY },
    { (x, y) -> x > midX && y > midY },
)

val endPositionsByQuadrant = endPositions.groupBy { pos ->
    quadrantPredicates.indexOfFirst { qp -> qp(pos) }
}.filter { it.key != -1 } // Filter valid quadrants, -1 are on the midlines

val solution = endPositionsByQuadrant.map { it.value.size }.reduce(Int::times)
println("Solution: $solution")
