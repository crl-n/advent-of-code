import java.io.File

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
			LR

			11A = (11B, XXX)
			11B = (XXX, 11Z)
			11Z = (11B, XXX)
			22A = (22B, XXX)
			22B = (22C, 22C)
			22C = (22Z, 22Z)
			22Z = (22B, 22B)
			XXX = (XXX, XXX)
		""".trimIndent().lines()
	}

	val pattern = input[0]

	val nodes = mutableMapOf<String, Pair<String, String>>()
	val starts = mutableListOf<String>()

	input.slice(2 until input.size).forEach {
		val (k, rest) = it.split(Regex(" = ")).map { it.trim() }

		if (k.endsWith("A")) {
			starts.add(k)
		}

		val (l, r) = rest.slice(1 until rest.length - 1).split(Regex(", "))
		nodes[k] = Pair(l, r)
	}

	var currs = starts.toList()
	var steps = 0L
	var i = 0
	while (true) {
		if (pattern[i % pattern.length] == 'L') {
			currs = currs.map { nodes[it]!!.first }
		} else {
			currs = currs.map { nodes[it]!!.second }
		}
		steps++
		i++
		i = i % pattern.length
		if (currs.all { it[2] == 'Z' } ) {
			break
		}
	}

	println("Steps: $steps")

}

main(args)

