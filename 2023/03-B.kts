import java.io.File
import kotlin.math.min

data class CharMtx(val mtx: List<List<Char>>) {
	val rows: Int
	val cols: Int

	init {
		rows = mtx.size
		cols = mtx[0].size
	}
}


fun CharMtx.invalid(i: Int, j: Int): Boolean {
	if (i < 0 || j < 0 || i >= rows || j >= cols) {
		return true
	}
	return false
}

val above = listOf<Pair<Int, Int>>(
	Pair(-1, -1),
	Pair(-1, 0),
	Pair(-1, 1)
)
val same = listOf<Pair<Int, Int>>(
	Pair(0, -1),
	Pair(0, 1)
)
val below = listOf<Pair<Int, Int>>(
	Pair(1, -1),
	Pair(1, 0),
	Pair(1, 1)
)

data class Num(var value: Int)
data class Gear(val i: Int, val j: Int, var adjs: Int = 0) {
	val adjNums = mutableListOf<Num>()
}

fun CharMtx.numFrom(i: Int, j: Int): Num {
	var x = j
	var start = j

	while (x >= 0 && mtx[i][x].isDigit()) {
		start = x
		x--
	}

	val line = mtx[i].joinToString("")


	val numericStr = line.substring(start).takeWhile { 
		it.isDigit()
	}

	return Num(numericStr.toInt())

}

fun Gear.calcAdjs(charMtx: CharMtx) {
	var prevIsDig = false

	above.forEach { (di, dj) ->
		if (!charMtx.invalid(i + di, j + dj)) {
			val c = charMtx.mtx[i + di][j + dj]

			if (c.isDigit()) {
				if (!prevIsDig) {
					adjs++
					adjNums.add(charMtx.numFrom(i + di, j + dj))
				}
				prevIsDig = true
			} else {
				prevIsDig = false
			}
		}
	}
	same.forEach { (di, dj) ->
		if (!charMtx.invalid(i + di, j + dj)) {
			val c = charMtx.mtx[i + di][j + dj]

			if (c.isDigit()) {
				adjs++
				adjNums.add(charMtx.numFrom(i + di, j + dj))
			}
		}
	}
	prevIsDig = false
	below.forEach { (di, dj) ->
		if (!charMtx.invalid(i + di, j + dj)) {
			val c = charMtx.mtx[i + di][j + dj]

			if (c.isDigit()) {
				if (!prevIsDig) {
					adjs++
					adjNums.add(charMtx.numFrom(i + di, j + dj))
				}
				prevIsDig = true
			} else {
				prevIsDig = false
			}
		}
	}
}

fun CharMtx.findGears(): List<Gear> {
	var gears = mutableListOf<Gear>()

	for ((i, line) in mtx.withIndex()) {
		for ((j, c) in line.withIndex()) {
			if (c == '*') {
				gears.add(Gear(i, j))
			}
		}
	}

	return gears.toList()
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
	var sum = 0

	var gears = charMtx.findGears()

	gears.forEach { it.calcAdjs(charMtx) }

	gears = gears.filter { it.adjs == 2 }

	gears.forEach { 
		sum += it.adjNums[0].value * it.adjNums[1].value
	}

	println("Sum of gear ratios: $sum")
}

main(args)

