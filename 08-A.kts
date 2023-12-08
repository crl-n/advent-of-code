import java.io.File

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
			LLR

			AAA = (BBB, BBB)
			BBB = (AAA, ZZZ)
			ZZZ = (ZZZ, ZZZ
		""".trimIndent().lines()
	}

	val pattern = input[0]

	val nodes = mutableMapOf<String, Pair<String, String>>()

	input.slice(2 until input.size).forEach {
		val (k, rest) = it.split(Regex(" = ")).map { it.trim() }
		val (l, r) = rest.slice(1 until rest.length - 1).split(Regex(", "))
		nodes[k] = Pair(l, r)
	}

	var curr: String = "AAA"
	var steps = 0
	while (curr != "ZZZ") {
		if (pattern[steps % pattern.length] == 'L') {
			curr = nodes[curr]!!.first
		} else {
			curr = nodes[curr]!!.second
		}
		steps++
	}

	println("Steps: $steps")

}

main(args)

