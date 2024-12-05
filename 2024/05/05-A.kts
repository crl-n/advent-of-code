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

// Now we are ready to solve the problem. We iterate through the updates, checking for unexpected values
// and collecting all updates that pass every check.
val correctUpdates = mutableListOf<List<Int>>()

outer@ for (update in pageUpdates) {
    val seen = mutableSetOf<Int>()

    for (value in update) {
        val expectedNotToBeSeen = pageOrderRules.get(value) ?: listOf()

        if (expectedNotToBeSeen.any { seen.contains(it) }) {
            continue@outer // Continue outer loop at first unexpected value
        }

        seen.add(value)
    }
    correctUpdates.add(update)
}

// Then we simply sum up the middlemost values
val middleValues = correctUpdates.map { it.get(it.size / 2) }

println("Solution: ${middleValues.sum()}")
