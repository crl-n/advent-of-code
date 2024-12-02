import java.io.File

// Parse input as List of List of Ints
val input = File("02.input").readLines()
    .map { it.split(" ").map { it.toInt() } }

// Returns a list of steps, or differences, between levels
fun calcSteps(report: List<Int>): List<Int> {
    return report.windowed(2)
        .map { (a, b) -> b - a }
}

// Define functions for checking if a report is safe
fun isIncreasing(steps: List<Int>): Boolean {
    return steps.all { step -> step >= 1 && step <= 3 }
}

fun isDecreasing(steps: List<Int>): Boolean {
    return steps.all { step -> step <= -1 && step >= -3 }
}

fun isSafe(report: List<Int>): Boolean {
    val steps = calcSteps(report)

    return isDecreasing(steps) || isIncreasing(steps)
}

// Count how many reports are safe
val safeCount = input.count(::isSafe)

println("Safe count: $safeCount")
