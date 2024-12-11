import java.io.File

val originalStones = File("11.input").readText().split(" ").map { it.toLong() }

fun transform(stone: Long): List<Long> {
    if (stone == 0L) {
        return listOf(1L)
    }

    val string = stone.toString()
    if (string.length % 2 == 0) {
        return listOf(
            string.substring(0, string.length / 2).toLong(),
            string.substring(string.length / 2, string.length).toLong()
        )
    }

    return listOf(stone * 2024)
}

// I first tried several different ways of memoizing the results, but they ended up requiring far too
// much heap memory. What worked in the end was the simple change of tracking counts/frequencies of stones,
// instead of individual stones:
var frequencies = originalStones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()

for (i in 1..75) {
    val newFrequencies = mutableMapOf<Long, Long>()

    for ((k, v) in frequencies) {
        val result = transform(k)

        result.forEach { newFrequencies[it] = newFrequencies.getOrDefault(it, 0L) + v }
    }
    frequencies = newFrequencies
}

val count = frequencies.values.sum()
println("Solution: $count")

