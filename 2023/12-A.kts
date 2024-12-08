import java.io.File

data class Record(
    val conditionRecord: String,
    val groupSizes: List<Int>
) {
    fun getAllPossibleArrangements(): List<String> {
        var possibleArrangements = listOf<MutableList<Char>>(conditionRecord.toMutableList())

        for (i in conditionRecord.indices) {
            if (conditionRecord[i] == '?') {
                possibleArrangements = possibleArrangements.flatMap {
                    listOf(
                        it.toMutableList().apply { this[i] = '.' },
                        it.toMutableList().apply { this[i] = '#' }
                    )
                }
            }
        }

        return possibleArrangements.map { it.joinToString("") }
    }

    fun countValidArrangements(): Int {
        val possibleArrangements = getAllPossibleArrangements()
        var count = 0

        for (arrangement in possibleArrangements) {
            val parts = arrangement.trim('.').split(Regex("\\.+"))
            val partLengths = parts.map { it.length }

            if (partLengths.equals(groupSizes)) count++
        }

        return count
    }
}

fun parseInput(args: Array<String>): List<Record> {
    val filename = args[0]
    val input = File(filename).readLines()

    val records = input.map { line ->
        val lineParts = line.split(" ")
        val conditionRecord = lineParts[0]
        val groupSizes = lineParts[1].split(",").map { it.toInt() }
        Record(conditionRecord, groupSizes)
    }
    return records
}

fun solve(records: List<Record>): Int {
    return records.sumBy(Record::countValidArrangements)
}

fun main(args: Array<String>) {
    val records = parseInput(args)
    val solution = solve(records)
    println(solution)
}

main(args)
