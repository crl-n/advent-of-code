import java.io.File

val input = File("09.input").readText().trim()

var blocks = mutableListOf<Int?>()

for ((i, c) in input.withIndex()) {
    if (i % 2 == 0) {
        blocks.addAll(List(c.digitToInt()) { i / 2 })
    } else {
        blocks.addAll(List(c.digitToInt()) { null })
    }
}

var i = 0
var j = blocks.size - 1

while (i <= j) {
    if (blocks[i] == null) {
        while (blocks[j] == null) j--
        blocks[i] = blocks[j]
        j--
    }
    i++
}

val sublist = blocks.subList(0, j + 1)

val solution = sublist.mapIndexed { i, v -> v!!.toLong() * i.toLong() }.sum()
println("Solution: $solution")
