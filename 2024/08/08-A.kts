import java.io.File

val input = File("08.input").readLines().map(String::toList)

data class Coord(val x: Int, val y: Int) {
    fun getAntinodeFor(other: Coord): Coord {
        return Coord(
            this.y - (other.y - this.y),
            this.x - (other.x - this.x)
        )
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
        antinodes.addAll(listOf(
            c.first.getAntinodeFor(c.second),
            c.second.getAntinodeFor(c.first)
        ))
    }
}

// Discard out-of-bounds and duplicate antinodes
val validAntinodes = antinodes.filter { node ->
    node.x >= 0 && node.y >= 0 && node.y < input.size && node.x < input[0].size
}
val uniqueAntinodes = validAntinodes.toSet()

println("Solution: ${uniqueAntinodes.size}")
