import java.io.File

val input = """
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
"""

fun buildRegexPattern(keys: Set<String>): Regex {
	val escapedKeys = keys.map { Regex.escape(it) }

	val joinedKeys = escapedKeys.joinToString("|")

	val regex = Regex("^(?:$joinedKeys)")
	
	return regex
}

val literals = hashMapOf(
	"one" to 1,
	"two" to 2,
	"three" to 3,
	"four" to 4,
	"five" to 5,
	"six" to 6,
	"seven" to 7,
	"eight" to 8,
	"nine" to 9
)
val literalRegex = buildRegexPattern(literals.keys)

fun valueFromChar(char: Char): Int {
	return char.toString().toInt()
}

fun valueFromLiteral(line: String): Int {
	val match = literalRegex.find(line)!!.value

	val value = literals[match] ?: 0

	return value
}

fun calibrate(array: List<String>): Int {
	var totalSum = 0

	array.forEach { line ->
		var lineSum = 0

		for (i in 0 until line.length) {
			when {
				line[i].isDigit() -> {
					lineSum += valueFromChar(line[i]) * 10
					break
				}
				literalRegex.containsMatchIn(line.substring(i)) -> {
					lineSum += valueFromLiteral(line.substring(i)) * 10
					break
				}
				else -> continue
			}
		}

		for (i in line.length - 1 downTo 0) {
			val char = line[i]

			when {
				line[i].isDigit() -> {
					lineSum += char.toString().toInt()
					break
				}
				literalRegex.containsMatchIn(line.substring(i)) -> {
					lineSum += valueFromLiteral(line.substring(i))
					break
				}
				else -> continue
			}
		}

		totalSum += lineSum
	}

	return totalSum
}

fun main(args: Array<String>) {
	if (args.isNotEmpty()) {
		val fileName = args[0]

		val content = File(fileName).readText()

		println(calibrate(content.lines()))
	} else {
		val sum = calibrate(input.trimIndent().lines())
		println(sum)
	}
}

main(args)
