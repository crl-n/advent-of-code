import java.io.File
import kotlin.math.min

data class Button(val x: Int, val y: Int, val cost: Int)

data class Coord(val x: Int, val y: Int)

data class ClawMachine(val a: Button, val b: Button, val prize: Coord) {
    fun solve(): Int {
        val aMax = min(prize.x / a.x, prize.y / a.y)
        val bMax = min(prize.x / b.x, prize.y / b.y)

        val solutions = mutableMapOf<Coord, Int>()

        for (aCurrent in 0..aMax) {
            for (bCurrent in 0..bMax) {
                val tokens = aCurrent * a.cost + bCurrent * b.cost

                val coord = Coord(
                    x = aCurrent * a.x + bCurrent * b.x,
                    y = aCurrent * a.y + bCurrent * b.y
                )

                if (coord == prize) solutions.set(coord, tokens)
            }
        }

        return solutions.values.minOrNull() ?: 0
    }
}

// Parse
val input = File("13.input").readText()

fun parseInts(line: String): List<Int> {
    return Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
}

val machines = input.split("\n\n")
    .map { section -> section.split("\n").map { parseInts(it) } }
    .map { (a, b, c) ->
        ClawMachine(
            a = Button(x = a[0], y = a[1], cost = 3),
            b = Button(x = b[0], y = b[1], cost = 1),
            prize = Coord(x = c[0], y = c[1])
        )
    }

// Solve
var tokensSpentTotal = 0

for (machine in machines) {
    val tokensSpent = machine.solve()
    tokensSpentTotal += tokensSpent
}

println("Solution: $tokensSpentTotal")
