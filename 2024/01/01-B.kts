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

// Split into left and right lists
val leftList = input.map { it[0] }
val rightList = input.map { it[1] }

// Turn right list into a frequency map
val rightListSorted = rightList.sorted()
val frequencies = rightListSorted.groupingBy { it }.eachCount()

// Calculate similarity scores
val scores = leftList.map { v -> v * frequencies.getOrDefault(v, 0) }

// Sum up similarity scores for answer
val totalSimilarityScore = scores.sum()

println("Solution: $totalSimilarityScore")
