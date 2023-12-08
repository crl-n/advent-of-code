import java.io.File
import kotlin.math.pow

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
			Time:      7  15   30
			Distance:  9  40  200
		""".trimIndent().lines()
	}

	val time = input[0].split(":")[1].replace(Regex("\\s+"), "").toLong()
	val record = input[1].split(":")[1].replace(Regex("\\s+"), "").toLong()

	println("time $time")
	println("record $record")

	var start = 0L
	var end = 0L

	for (wait in 1..time - 1) {
		if ((time - wait) * wait > record) {
			start = wait
			break
		}
	}
	for (wait in time - 1 downTo 1) {
		if ((time - wait) * wait > record) {
			end = wait
			break
		}
	}

	val res = end - start + 1

	println("Result: $res")
}

main(args)

