import java.io.File

val input = File("./04/04.input").readLines()

val rooms = input.map { line ->
    listOf(
        line.substringBeforeLast('-'),
        line.substringAfterLast('-').split("[").first(),
        line.split("[").last().trimEnd(']'),
    )
}

// Final sum for solution
var sum = 0

for (room in rooms) {
    val encryptedName = room[0]
    val sectorIdAsInt = room[1].toInt()
    val checksum = room[2]
    val counts = mutableMapOf<Char, Int>()

    // Count chars and order counts correctly
    for (c in encryptedName) {
        if (!c.isLowerCase()) continue
        counts.putIfAbsent(c, 0)
        counts[c] = counts[c]!! + 1
    }
    val orderedCounts = counts.toList()
        .sortedWith(
            compareByDescending<Pair<Char, Int>> { it.second }
                .thenBy { it.first }
        )

    // Check for real rooms
    val fiveMostCommon = orderedCounts.subList(0, 5).map { it.first }.joinToString("")
    val isReal = fiveMostCommon == checksum

    if (isReal) {
        sum += sectorIdAsInt
    }
}

println("Solution: $sum")
