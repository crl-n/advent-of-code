import java.io.File
import kotlin.math.min

data class Num(val value: Int, val start: Pair<Int, Int>, val end: Pair<Int, Int>, val charMtx: CharMtx) {
	var isAdj: Boolean

	init {
		isAdj = false

		val i = start.first
		for (j in start.second..end.second) {
			if (charMtx.isAdj(i, j)) {
				isAdj = true
			}
		}
	}
}

data class CharMtx(val mtx: List<List<Char>>) {
	val rows: Int
	val cols: Int

	init {
		rows = mtx.size
		cols = mtx[0].size
	}
}

val offsets = listOf<Pair<Int, Int>>(
	Pair(-1, 0),
	Pair(-1, 1),
	Pair(0, 1),
	Pair(1, 1),
	Pair(1, 0),
	Pair(1, -1),
	Pair(0, -1),
	Pair(-1, -1)
)
val notSymbols = (0..9).map { ('0' + it).toChar() } + '.'
fun CharMtx.isAdj(i: Int, j: Int): Boolean {
	var adj = false

	for ((di, dj) in offsets) {
		val newI = i + di
		val newJ = j + dj

		if (newI < 0 || newJ < 0 || newI >= rows || newJ >= cols) {
			continue
		}

		if (mtx[newI][newJ] !in notSymbols) {
			adj = true
		}
	}
	return adj
}

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
		467..114..
		...*......
		..35..633.
		......#...
		617*......
		.....+.58.
		..592.....
		......755.
		...$.*....
		.664.598..
		""".trimIndent().lines()
	}

	val charMtx = CharMtx(input.map { it.toCharArray().toList() })
	val nums = mutableListOf<Num>()

	for ((i, line) in input.withIndex()) {
		var j = 0

		while (j < line.length) {
			val c = line[j]

			if (c.isDigit()) {
				val startJ = j
				val numericStr = line.substring(j).takeWhile { 
					it.isDigit()
				}
				j += numericStr.length

				nums.add(
					Num(numericStr.toInt(),
						Pair(i, startJ),
						Pair(i, j - 1),
						charMtx
					)
				)
				continue
			}
			j++
		}
	}

	var sum = 0

	for (num in nums) {
		if (num.isAdj) {
			sum += num.value
		}
	}

	println("Sum of engine parts: $sum")
}

main(args)

