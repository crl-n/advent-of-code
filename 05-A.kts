import java.io.File

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
			seeds: 79 14 55 13

			seed-to-soil map:
			50 98 2
			52 50 48

			soil-to-fertilizer map:
			0 15 37
			37 52 2
			39 0 15

			fertilizer-to-water map:
			49 53 8
			0 11 42
			42 0 7
			57 7 4

			water-to-light map:
			88 18 7
			18 25 70

			light-to-temperature map:
			45 77 23
			81 45 19
			68 64 13

			temperature-to-humidity map:
			0 69 1
			1 0 69

			humidity-to-location map:
			60 56 37
			56 93 4
		""".trimIndent().lines()
	}

	val sections = mutableListOf<MutableList<String>>()
	var currentList = mutableListOf<String>()

	for (line in input) {
		if (line.startsWith("seeds:")) {
			currentList.add(line.split(Regex(": "))[1])
			continue
		}
		if (line.isBlank()) {
			sections.add(currentList)
			currentList = mutableListOf<String>()
			continue
		}
		if (line.endsWith("map:")) {
			continue
		}

		currentList.add(line)
	}
	sections.add(currentList)

	var maps = sections.map { it.map { it.split(" ").map { it.toLong() } } }
	var seeds = maps[0][0]
	maps = maps.slice(1..maps.size - 1)

	val locations = mutableListOf<Long>()

	seeds.forEach { seed ->
		var value = seed
		for (map in maps) {
			for (it in map) {
				if (value in it[1]..(it[1] + it[2] - 1)) {
					value = it[0] + value - it[1]
					break
				}
			}
		}
		locations.add(value)
	}

	println(locations.min())
}

main(args)
