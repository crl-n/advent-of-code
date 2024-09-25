import java.io.File

val input = File("./03/03.input").readLines()

val linesAsIntLists = input.map {
    it.trimStart()
    .split("\\s+".toRegex())
    .map { it.trim().toInt() }
}

// Rearrange values so that each list's first element are first, then second, then third.
// Then chunk back into lists of three elements.
val triangles = (0..2).flatMap { i ->
    linesAsIntLists.map { it[i] }
}.chunked(3)

var possibles = 0
for (triangle in triangles) {
    val sortedLengths = triangle.sortedDescending()
    val isPossible = sortedLengths.reduce { a, b -> a - b } < 0
    if (isPossible) possibles++
}

println("Solution: $possibles")

