import java.io.File

val moves = mapOf(
	"nw" to listOf(-1, -1),
	"n" to listOf(-1, 0),
	"ne" to listOf(-1, 1),
	"w" to listOf(0, -1),
	"e" to listOf(0, 1),
	"sw" to listOf(1, -1),
	"s" to listOf(1, 0),
	"se" to listOf(1, 1)
)

fun opposite(move: List<Int>): List<Int> {
	return move.map { it * -1 }
}

val dirs = mapOf(
	'F' to Pair(moves["e"], moves["s"]),
	'-' to Pair(moves["e"], moves["w"]),
	'|' to Pair(moves["n"], moves["s"]),
	'J' to Pair(moves["n"], moves["w"]),
	'7' to Pair(moves["s"], moves["w"]),
	'L' to Pair(moves["n"], moves["e"]),
)

fun findStart(input: List<String>): List<Int> {
	var start = mutableListOf(0, 0)

	for ((i, line) in input.withIndex()) {
		for ((j, c) in line.withIndex()) {
			if (c == 'S') {
				start[0] = i
				start[1] = j
			}
		}
	}

	return start.toList()
}

fun valueAt(input: List<String>, loc: List<Int>): Char {
	return input[loc[0]][loc[1]]
}

fun moveTo(input: List<String>, curr: List<Int>, move: List<Int>): List<Int> {
	return curr.zip(move).map { (a, b) -> a + b }
}

fun findValidFirstMoves(input: List<String>, start: List<Int>): Pair<
	Pair<List<Int>, List<Int>>,
	Pair<List<Int>, List<Int>>
> {
	var firstMoves = mutableListOf<Pair<List<Int>, List<Int>>>()

	for (m in moves.values) {
		val dest = moveTo(input, start, m)
		val c = valueAt(input, dest)


		if (c != null && c != '.') {
			val d = dirs[c] ?: throw Error("Null d")
			val opt1 = d.first ?: throw Error("Null opt1")
			val opt2 = d.second ?: throw Error("Null opt2")

			if (start == moveTo(input, dest, opt1)) {
				firstMoves.add(Pair(dest.toList(), opt2))
			}
			if (start == moveTo(input, dest, opt2)) {
				firstMoves.add(Pair(dest.toList(), opt1))
			}
		}

	}

	return Pair(firstMoves[0], firstMoves[1])
}

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
			.....
			.S-7.
			.|.|.
			.L-J.
			.....
		""".trimIndent().lines()
	}

	val start = findStart(input)

	println("start $start")

	var (p1, p2) = findValidFirstMoves(input, start) ?: throw Error()
	var (p1curr, p1nextMove) = p1 ?: throw Error()
	var (p2curr, p2nextMove) = p2 ?: throw Error()

	println("p1 -> $p1")
	println("p2 -> $p2")

	var steps = 1
	while (p1curr != p2curr) {
		p1curr = moveTo(input, p1curr, p1nextMove)
		var c = valueAt(input, p1curr) ?: throw Error("No char")
		p1nextMove = dirs[c]!!.toList().find { it != opposite(p1nextMove) } ?: throw Error("Error")

		p2curr = moveTo(input, p2curr, p2nextMove)
		c = valueAt(input, p2curr) ?: throw Error("No char")
		p2nextMove = dirs[c]!!.toList().find { it != opposite(p2nextMove) } ?: throw Error("Error")

		steps++
	}

	println(steps)
	
}

main(args)

