import java.io.File

val input = File("./18/18.input").readText().trimEnd()

val topRow = input.map { it == '^' }
val n = 400_000
val rowLen = topRow.size
val rows = mutableListOf(topRow)

// Does not guarantee valid indices, i.e. indices can be out of bound
fun getLcrIndices(i: Int): List<Int> {
    return listOf(-1, 0, 1).map { it + i }
}

// Out of bound indices become false (safe tile)
fun getLcrValues(indices: List<Int>, row: List<Boolean>): List<Boolean> {
    return indices.map { i ->
        if (i < 0 || i >= row.size) false
        else row[i]
    }
}

while (rows.size < n) {
    val previousRow = rows.last()
    var newRow = MutableList<Boolean>(rowLen) { false }
    for (i in (0..rowLen - 1)) {
        val lcrIndices = getLcrIndices(i)
        val lcrValues = getLcrValues(lcrIndices, previousRow)

        // If tile is a trap is determined by xor on l and r values
        if (lcrValues[0].xor(lcrValues[2])) {
            newRow[i] = true
        }
    }
    rows.add(newRow)
}

val solution = rows.flatten().count { it == false }
println("Solution: $solution")
