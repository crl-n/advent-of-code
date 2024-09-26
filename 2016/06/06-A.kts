import java.io.File

val input = File("./06/06.input").readLines()

val transposed = (1..input[0].length).map { i -> input.map { it[i - 1] }.joinToString("") }

val mostFrequent = transposed.map { line ->
    val charCounts = line.fold(mutableMapOf<Char, Int>()) { map, c ->
        map.putIfAbsent(c, 0)
        map[c] = map[c]!! + 1
        map
    }
    charCounts.toList().sortedWith(compareByDescending { it.second }).first().first
}

val solution = mostFrequent.joinToString("")

println("Solution: $solution")
