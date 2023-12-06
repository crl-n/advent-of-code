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

	var res = 1

	val timeStrs = input[0].split(Regex("\\s+"))
	val times = timeStrs.subList(1, timeStrs.size).map { it.toInt() }

	val recStrs = input[1].split(Regex("\\s+"))
	val recs = recStrs.subList(1, recStrs.size).map { it.toInt() }

	times.mapIndexed { i, time ->
		(1..time - 1).toList().map { wait ->
			(time - wait) * wait
		}.filter { dist ->
			dist > recs[i]
		}
	}.forEach { 
		res *= it.size
	}

	println("Result: $res")
}

main(args)

