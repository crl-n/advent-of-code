import java.io.File

val input = File("08.input").readLines().map(String::toList)

data class Coord(val x: Int, val y: Int) {
    fun getAntinodesFor(other: Coord): List<Coord> {
        val antinodes = mutableListOf<Coord>(Coord(this.x, this.y))
        val yIncrement = other.y - this.y
        val xIncrement = other.x - this.x
        var currentY = this.y - yIncrement
        var currentX = this.x - xIncrement

        while (currentX >= 0 && currentY >= 0 && currentY < input.size && currentX < input[0].size) {
            antinodes.add(Coord(
                currentX,
                currentY
            ))

            currentX -= xIncrement
            currentY -= yIncrement
        }

        return antinodes
    }
}

val frequencies = listOf(('a'..'z'), ('A'..'Z'), ('0'..'9')).flatten().toSet()
val antennasByFrequency = mutableMapOf<Char, MutableList<Coord>>()

// Find positions of all antennas and group by frequency
for ((y, row) in input.withIndex()) {
    for ((x, c) in row.withIndex()) {
        if (frequencies.contains(c)) {
            antennasByFrequency.getOrPut(c) { mutableListOf() }.add(Coord(x, y))
        }
    }
}

fun getCombinations(list: List<Coord>): List<Pair<Coord, Coord>> {
    val combinations = mutableListOf<Pair<Coord, Coord>>()
    for (i in list.indices) {
        for (j in i + 1 until list.size) {
            combinations.add(Pair(
                list.get(i),
                list.get(j)
            ))
        }
    }
    return combinations
}

val antinodes = mutableListOf<Coord>()

// Iterate over frequencies in map and figure out antinode positions
for (k in antennasByFrequency.keys) {
    val combinations = getCombinations(
        antennasByFrequency.getOrDefault(k, emptyList<Coord>()).toList()
    )

    for (c in combinations) {
        antinodes.addAll(c.first.getAntinodesFor(c.second))
        antinodes.addAll(c.second.getAntinodesFor(c.first))
    }
}

// Discard duplicate antinodes
val uniqueAntinodes = antinodes.toSet()

println("Solution: ${uniqueAntinodes.size}")
