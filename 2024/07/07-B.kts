import java.io.File

val input = File("07.input").readLines()

fun parseLongs(line: String): List<Long> {
    return Regex("\\d+").findAll(line).map {
        it.value.toLong()
    }.toList()
}

val inputAsLongs = input.map { parseLongs(it) }

fun isTrueCalibration(values: List<Long>): Boolean {
    val expectedResult = values[0]
    var intermediaryResults = listOf<Long>(values[1])
    val terms = values.subList(2, values.size)

    for (term in terms) {
        intermediaryResults = intermediaryResults.flatMap {
            listOf(
                it + term,
                it * term,
                "$it$term".toLong(), // The concatenation operator
            )
        }
    }

    val results = intermediaryResults
    return results.any { it == expectedResult }
}

val trueCalibrations = inputAsLongs.filter { isTrueCalibration(it) }

val totalCalibration = trueCalibrations.map { it[0] }.sum()

println(totalCalibration)
