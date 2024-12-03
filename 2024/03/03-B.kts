import java.io.File

// Function for extracting only enabled parts of the program
fun filterEnabled(text: String): List<String> {
    // Multiplication is enabled at the start, so we capture everything up until the
    // first `don't`
    val initialEnabledPart = Regex("(^.*?)don't\\(\\)").find(text)?.groupValues?.get(1)
        ?: ""

    // Then we get all the enabled parts between `do` and `don't` instructions
    // and the part between the last `do` and the end of the input
    val remainingEnabledParts = Regex("do\\(\\)(.*?)(?:don't\\(\\)|$)").findAll(text)
        .map { it.groupValues.get(1) }
        .toList()

    // Combine and return
    val parts = remainingEnabledParts.toMutableList()
    parts.add(initialEnabledPart)

    return parts.toList()
}

// Function for extracting multiplication instructions from input
fun parseMulInstructions(text: String): List<List<Int>> {
    return Regex("mul\\((\\d+),(\\d+)\\)").findAll(text)
        .map { it.groupValues.subList(1, 3).map(String::toInt) }
        .toList()
}

// Parse input as one line
val input = File("03.input").readLines().joinToString()

// Filter the enabled parts
val enabledParts = filterEnabled(input)
val mulInstructions = enabledParts.flatMap(::parseMulInstructions)

// Apply multiplication
val products = mulInstructions.map { (a, b) -> a * b }

// Sum products together
val sum = products.sum()

println("Solution: $sum")
