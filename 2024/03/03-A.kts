import java.io.File

// Function for extracting multiplication instructions from input
fun parseMulInstructions(text: String): List<List<Int>> {
    return Regex("mul\\((\\d+),(\\d+)\\)").findAll(text)
        .map { it.groupValues.subList(1, 3).map(String::toInt) }
        .toList()
}

// Parse input
val input = File("03.input").readText()
val mulInstructions = parseMulInstructions(input)

// Apply multiplication
val products = mulInstructions.map { (a, b) -> a * b }

// Sum products together
val sum = products.sum()

println("Solution: $sum")
