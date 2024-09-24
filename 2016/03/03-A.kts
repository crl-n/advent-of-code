import java.io.File

val input = File("./03/03.input").readLines()

var possibles = 0

for (line in input) {
    val sortedLengths = line.trimStart()
        .split("\\s+".toRegex())
        .map { it.trim().toInt() }
        .sortedDescending()

    val isPossible = sortedLengths.reduce { a, b -> a - b } < 0
    if (isPossible) possibles++
}

println("Solution: $possibles")
