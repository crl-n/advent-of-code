import java.io.File
import kotlin.math.min

val maxRed = 12
val maxGreen = 13
val maxBlue = 14

data class CubeSet(var red: Int, var green: Int, var blue: Int)
data class Game(val id: Int, val sets: MutableList<CubeSet>)
enum class Cube(val color: String) {
	RED("red"),
	GREEN("green"),
	BLUE("blue")
}

fun CubeSet.power(): Int {
	return red * green * blue
}

fun Game.minPower(): Int {
	val maxRed = sets.maxBy { it.red }.red
	val maxGreen = sets.maxBy { it.green }.green
	val maxBlue = sets.maxBy { it.blue }.blue

	val maxSet = CubeSet(maxRed, maxGreen, maxBlue)

	return maxSet.power()
}

fun Game.isPossible(): Boolean {
	return sets.all {
		it.red <= maxRed && it.green <= maxGreen && it.blue <= maxBlue
	}
}

fun parseGames(lines: List<String>): List<Game> {
	val games = mutableListOf<Game>()

	for (line in lines) {
		val match = Regex("Game (\\d+): (.*)").find(line)!!

		val (id, cubesString) = match.destructured

		val setStrings = cubesString.split(";").map { it.trim() }

		val game = Game(id.toInt(), mutableListOf<CubeSet>())

		setStrings.forEach { setString ->
			val set = Cube.values().fold(CubeSet(0, 0, 0)) { acc, enumValue ->
				val match = Regex("(\\d+) ${enumValue.color}").find(setString)

				if (match != null) {
					val (value) = match.destructured

					when (enumValue.color) {
						"red" -> acc.red += value.toIntOrNull() ?: 0
						"green" -> acc.green += value.toIntOrNull() ?: 0
						"blue" -> acc.blue += value.toIntOrNull() ?: 0
						else -> println("None")
					}
				}
				acc.copy()
			}
			game.sets.add(set)
		}

		games.add(game)
	}
	return games.toList()
}

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
		Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
		Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
		Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
		Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
		Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
		""".trimIndent().lines()
	}

	val games = parseGames(input)

	var sum = 0

	games.forEach {
		sum += it.minPower()
	}

	println("Sum of powers: $sum")
}

main(args)

