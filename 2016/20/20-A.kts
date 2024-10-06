import java.io.File

val input = File("./20/20.input").readLines()

fun parseLongs(s: String): List<Long> {
    return Regex("\\d+").findAll(s).map { it.value.toLong() }.toList()
}

val ranges = input.map { parseLongs(it) }.sortedBy { it[0] }

var lowest = 0L
for (range in ranges) {
    val (limitLow, limitHigh) = range
    if (lowest >= limitLow) lowest = limitHigh + 1
}
println("Solution: $lowest")
