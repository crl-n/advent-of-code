import java.io.File

val input = File("./08/08.input").readLines()

class Screen() {
    val cols = 50
    val rows = 6
    val pixels = MutableList(cols * rows, { i -> false })

    fun setPixel(x: Int, y: Int, v: Boolean) {
        pixels[y * cols + x] = v
    }

    fun rect(x: Int, y: Int) {
        val indices = (0..y - 1).flatMap { y -> (0..x - 1).map { x -> Pair(x, y) } }
        indices.forEach { this.setPixel(it.first, it.second, true) }
    }

    fun rotateRow(row: Int, by: Int) {
        val rowStart = row * cols
        val rowEnd = rowStart + cols - 1
        val cutoff = rowEnd - by + 1
        val newRow = pixels.subList(cutoff, rowEnd + 1).plus(
            pixels.subList(rowStart, cutoff)
        )
        newRow.indices.forEach { i ->
            pixels[rowStart + i] = newRow[i]
        }
    }

    fun rotateCol(col: Int, by: Int) {
        val indices = (0..rows - 1).map { y -> y * cols + col }
        val originalCol = indices.map { i -> pixels[i] }
        val cutoff = originalCol.size - by
        val newCol = originalCol.subList(cutoff, originalCol.size).plus(
            originalCol.subList(0, cutoff)
        )
        indices.forEachIndexed { i, j ->
            pixels[j] = newCol[i]
        }
    }

    override fun toString(): String {
        return this.pixels.chunked(cols).map {
            it.map { if (it) "#" else "." }.joinToString("")
        }.joinToString("\n")
    }
}

val screen = Screen()

fun parseIntsFromString(s: String): List<Int> {
    return Regex("\\d+").findAll(s).map { it.value.toInt() }.toList()
}

for (line in input) {
    if (line.startsWith("rect")) {
        val args = parseIntsFromString(line)
        screen.rect(args[0], args[1])
    } else if (line.startsWith("rotate row")) {
        val args = parseIntsFromString(line)
        screen.rotateRow(args[0], args[1])
    } else if (line.startsWith("rotate col")) {
        val args = parseIntsFromString(line)
        screen.rotateCol(args[0], args[1])
    }
}

println("Solution:")
println(screen)

