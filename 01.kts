import java.io.File

val input = """
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
"""

fun calibrate(array: List<String>): Int {
	var totalSum = 0

	array.forEach { line ->
		var lineSum = 0

		for (char in line) {
			if (char.isDigit()) {
				lineSum += char.toString().toInt() * 10
				break
			}
		}

		for (char in line.reversed()) {
			if (char.isDigit()) {
				lineSum += char.toString().toInt()
				break
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
