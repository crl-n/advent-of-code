import java.io.File

fun lcm(a: Long, b: Long): Long {
	return a * b / gcd(a, b)
}

fun gcd(a: Long, b: Long): Long {
	return if (b == 0L) a else gcd(b, a % b)
}

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

	var steps = 0
	var loops = mutableListOf<Long>()

	while (currs.size > 0) {
		if (pattern[steps % pattern.length] == 'L') {
			currs = currs.map { nodes[it]!!.first }
		} else {
			currs = currs.map { nodes[it]!!.second }
		}
		if (currs.any { it[2] == 'Z' }) {
			loops.add((steps + 1).toLong())
			currs = currs.filter { it[2] != 'Z' }
		}
		steps++
	}

	val lcmVal = loops.reduce { acc, x -> lcm(acc, x)}

	println("Steps: $lcmVal")
}

main(args)

