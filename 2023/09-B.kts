import java.io.File

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
			0 3 6 9 12 15
			1 3 6 10 15 21
			10 13 16 21 30 45
		""".trimIndent().lines()
	}

	val groups = input.map { line -> mutableListOf<List<Long>>(line.split(" ").map { it.toLong() }) }

	var res = 0L
	groups.forEach { group ->
		while (group[group.size - 1].any { it != 0L }) {
			val nl = group[group.size - 1].zipWithNext { a, b -> b - a }
			group.add(nl)
		}

		var v = 0L
		for (i in group.size - 1 downTo 0) {
			v = group[i][0] - v
		}
		res += v

	}
	println("res $res")
}

main(args)

