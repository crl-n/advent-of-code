import java.io.File
import kotlin.math.abs

fun parseTwoInts(line: String): List<Int> {
    return Regex("(\\d+)\\s+(\\d+)").find(line)!!.let { result ->
        val (first, second) = result.destructured
        listOf(first.toInt(), second.toInt())
    }
}

// Parse input as List of List of Ints
val input = File("01.input").readLines().map(::parseTwoInts)

// Split into left and right lists and sort
val leftList = input.map { it[0] }.sorted()
val rightList = input.map { it[1] }.sorted()

// Calculate distances
fun calcDistance(first: Int, second: Int): Int {
    return abs(first - second)
}
val distances = leftList.zip(rightList) { l, r -> calcDistance(l, r) }

// Sum up distances for the answer
val solution = distances.sum()

println("First Solution: $solution")
