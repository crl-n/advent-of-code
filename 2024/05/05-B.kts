import java.io.File

// Split the input into two parts
val (pageOrderInput, updateInput) = File("05.input").readText().split("\n\n")

// Parse page ordering input into a map. Keys are ints that should come before all ints
// in the value list
fun parsePageOrders(lines: List<String>): Map<Int, List<Int>> {
    val mutableMap = mutableMapOf<Int, MutableList<Int>>()

    for (line in lines) {
        val (left, right) = Regex("(\\d+)\\|(\\d+)").find(line)!!.let {
            it.groupValues.subList(1, 3)
                .map { it.toInt() }
        }

        if (mutableMap.contains(left)) {
            mutableMap[left]!!.add(right)
        } else {
            mutableMap.set(left, mutableListOf(right))
        }
    }
    return mutableMap.toMap()
}

// Parse updates as lists of ints
fun parseUpdate(line: String): List<Int> {
    return Regex("\\d+").findAll(line).map { it.value.toInt() }.toList()
}

// Apply the parsing functions
val pageOrderRules = parsePageOrders(pageOrderInput.split("\n"))
val pageUpdates = updateInput.split("\n").map(::parseUpdate)

// Solve. Same as part A, except this time we collect the incorrect updates and then reorder them.
val incorrectUpdates = mutableListOf<List<Int>>()

outer@ for (update in pageUpdates) {
    val seen = mutableSetOf<Int>()

    for (value in update) {
        val expectedNotToBeSeen = pageOrderRules.get(value) ?: listOf()

        if (expectedNotToBeSeen.any { seen.contains(it) }) {
            incorrectUpdates.add(update)
            continue@outer // Continue outer loop at first unexpected value
        }

        seen.add(value)
    }
}

// Reorder incorrect updates
val correctedIncorrectUpdates = incorrectUpdates.map { it.toMutableList() }

var i = 0
outer@ while (i < correctedIncorrectUpdates.size) {
    val update = correctedIncorrectUpdates[i]
    val seen = mutableMapOf<Int, Int>()

    for ((index, value) in update.withIndex()) {
        val expectedNotToBeSeen = pageOrderRules.get(value) ?: listOf()

        for (unexpected in expectedNotToBeSeen) {
            // If we find a seen value that should be after the current value we place the current value
            // before that one, and restart the process from the top
            if (seen.contains(unexpected)) {
                val seenIndex = seen.get(unexpected)!!
                update.removeAt(index)
                update.add(seenIndex, value)

                i = 0
                continue@outer
            }
        }
        seen.put(value, index)
    }
    i++
}

// Sum up the middlemost values
val middleValues = correctedIncorrectUpdates.map { it.get(it.size / 2) }

println("Solution: ${middleValues.sum()}")
