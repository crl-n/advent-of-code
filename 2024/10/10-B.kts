import java.io.File

val input = File("10.input").readLines().map { it.map { char -> char.digitToInt() } }
val rows = input.size
val cols = input[0].size

data class Coord(val x: Int, val y: Int) {
    fun adjacents(): List<Coord> {
        return listOf(
            Coord(x, y - 1),
            Coord(x + 1, y),
            Coord(x, y + 1),
            Coord(x - 1, y)
        ).filter(Coord::isValid)
    }

    fun value(): Int? {
        return input.get(y)?.get(x)
    }

    fun isValid(): Boolean {
        return y >= 0 && x >= 0 && y < rows && x < cols
    }
}

// Find all trailheads
val trailheads = input.flatMapIndexed { y, row ->
    row.mapIndexed { x, v -> if (v == 0) Coord(x, y) else null }
        .filterNotNull()
}

fun calcScore(trailhead: Coord): Int {
    // We use a queue of entries consisting of coord and expected value
    val initialAdjacents = trailhead.adjacents().map { Pair(it, 1) }
    val queue = ArrayDeque<Pair<Coord, Int>>(initialAdjacents)
    var score = 0

    while (queue.isNotEmpty()) {
        val (coord, expected) = queue.removeFirst()

        if (coord.value() != expected) {
            continue
        }

        if (coord.value() == 9) {
            score++
            continue
        }

        val adjacents = coord.adjacents().map { Pair(it, expected + 1) }
        queue.addAll(adjacents)
    }

    return score
}

var totalScore = 0
for (th in trailheads) {
    totalScore += calcScore(th)
}

println("Solution: $totalScore")


