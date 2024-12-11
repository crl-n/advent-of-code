import java.io.File
import java.util.LinkedList

val input = File("11.input").readText().split(" ").map { it.toLong() }
val stones = LinkedList<Long>(input)

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

for (i in 1..25) {
    var j = 0
    while (j < stones.size) {
        val stone = stones.removeAt(j)

        val result = transform(stone)
        stones.addAll(j, result)
        j += result.size
    }
}

println("Solution: ${stones.size}")
