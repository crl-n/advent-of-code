import java.io.File

val input = File("./20/20.input").readLines()

fun parseLongs(s: String): List<Long> {
    return Regex("\\d+").findAll(s).map { it.value.toLong() }.toList()
}

val ranges = input.map { parseLongs(it) }.sortedWith(compareBy({ it[0] }, { it[1] }))

var previousHigh: Long = -1L
var blacklistedCount = 0L

for (range in ranges) {
    val (limitLow, limitHigh) = range
    if (limitHigh < previousHigh) {
        continue
    } else if (limitLow <= previousHigh) {
        blacklistedCount += limitHigh - (previousHigh + 1L) + 1L
    } else {
        blacklistedCount += limitHigh - limitLow + 1L
    }
    previousHigh = limitHigh
}

val allowedCount = 4294967295L + 1L - blacklistedCount
println("Solution: $allowedCount")

