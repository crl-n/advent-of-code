import java.io.File

data class Range(val lo: Long, val hi: Long)

data class RangeMap(val dest: Range, val src: Range) {
	fun srcRange(): LongRange {
		return src.lo..src.hi
	}

	fun destRange(): LongRange {
		return dest.lo..dest.hi
	}

	fun offset(): Long {
		return src.lo - dest.lo
	}
}

fun parseInput(args: Array<String>): Pair<List<Range>,  List<List<RangeMap>>> {
	val fileName = args[0]
	val (seedsLine, remainingInput) = File(fileName).readText().split("\n\n", limit = 2)

	// Parse seed ranges
	val seedRanges = Regex("\\d+").findAll(seedsLine)
		.map { it.value.toLong() }
		.chunked(2)
		.map { (start, length) -> Range(start, start + length - 1) }
		.toList()

	// Parse maps, we split input into sections, one for each map. For each section we
	// parse all ints from all lines that start with a digit
	val maps = remainingInput.split("\n\n").map { inputSection ->
		inputSection.split("\n")
			.filter { it[0].isDigit() }
			.map { Regex("\\d+").findAll(it).map { it.value.toLong() }.toList() }
			.map { (destStart, srcStart, length) ->
				RangeMap(
					dest = Range(destStart, destStart + length - 1),
					src = Range(srcStart, srcStart + length - 1),
				)
			}
	}

	return Pair(seedRanges, maps)
}

fun main(args: Array<String>) {
	val (seedRanges, maps) = parseInput(args)

	// This time we transform ranges instead of
	// individual seeds and split them as necessary
	var ranges = mutableListOf<Range>()
	ranges.addAll(seedRanges)

	for (map in maps) {
		val newRanges = mutableListOf<Range>()

		mid@ while (ranges.isNotEmpty()) {
			val range = ranges.removeFirst()

			for (rangeMap in map) {
				if (range.lo in rangeMap.srcRange() && range.hi in rangeMap.srcRange()) {
					val newRange = Range(
						range.lo - rangeMap.offset(),
						range.hi - rangeMap.offset(),
					)
					newRanges.add(newRange)
					continue@mid
				} else if (range.lo in rangeMap.srcRange()) {
					val newRange = Range(
						range.lo - rangeMap.offset(),
						rangeMap.src.hi - rangeMap.offset(),
					)
					newRanges.add(newRange)

					val remaining = Range(
						rangeMap.src.hi + 1,
						range.hi
					)
					ranges.add(remaining)

					continue@mid
				} else if (range.hi in rangeMap.srcRange()) {
					val newRange = Range(
						rangeMap.src.lo - rangeMap.offset(),
						range.hi - rangeMap.offset(),
					)
					newRanges.add(newRange)

					val remaining = Range(
						range.lo,
						rangeMap.src.lo - 1
					)
					ranges.add(remaining)

					continue@mid
				}
			}

			// If range didn't match any range mappings, we transfer it to the next stage as it is
			newRanges.add(range)
		}

		ranges = newRanges
	}

	val minLocation = ranges.map { it.lo }.min()

	println("Solution: $minLocation")
}

main(args)
