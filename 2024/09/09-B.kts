import java.io.File

val input = File("09.input").readText().trim()

data class BlockGroup(
    var count: Int,
    var value: Int?
)

var groups = mutableListOf<BlockGroup>()

for ((i, c) in input.withIndex()) {
    if (i % 2 == 0) {
        groups.add(
            BlockGroup(
                count = c.digitToInt(),
                value = i / 2
            )
        )
    } else {
        groups.add(
            BlockGroup(
                count = c.digitToInt(),
                value = null
            )
        )
    }
}

var i = groups.size - 1

outer@ while (i >= 0) {
    val sourceGroup = groups[i]
    if (sourceGroup.value == null) {
        i--
        continue
    }

    var j = 0
    while (j < i) {
        val destinationGroup = groups[j]

        if (destinationGroup.value == null && destinationGroup.count >= sourceGroup.count) {
            val leftOver = destinationGroup.count - sourceGroup.count

            destinationGroup.value = sourceGroup.value
            destinationGroup.count = sourceGroup.count
            sourceGroup.value = null

            if (leftOver > 0) {
                groups.add(j + 1, BlockGroup(leftOver, null))
            }
            continue@outer
        }
        j++
    }
    i--
}

val blocks = groups.flatMap { group ->
    List(group.count) { group.value }
}

val solution = blocks.mapIndexed { i, v -> (v?.toLong() ?: 0L) * i.toLong() }.sum()
println("Solution: $solution")
