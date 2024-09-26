import java.io.File

val input = File("./06/06.input").readLines()

val transposed = input[0].indices.map { i -> input.map { it[i] }.joinToString("") }

val mostFrequentChars = transposed.map { line ->
    val charCounts = line.groupingBy { it.code }.eachCount()
    charCounts.maxBy { it.value }.key.toChar()
}

val solution = mostFrequentChars.joinToString("")

println("Solution: $solution")
