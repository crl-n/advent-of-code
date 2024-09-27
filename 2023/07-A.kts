import java.io.File
import kotlin.math.pow

fun main(args: Array<String>) {
	var input = emptyList<String>()

	if (args.isNotEmpty()) {
		val fileName = args[0]

		input = File(fileName).readLines()
	} else {
		input = """
			32T3K 765
			T55J5 684
			KK677 28
			KTJJT 220
			QQQJA 483
		""".trimIndent().lines()
	}

	val (hands, bids) = input.map { it.split(' ') }.map { (first, second) -> Pair(first, second.toInt()) }.unzip()

	val uniques: (String) -> (Int) = { it.toSet().size }
	val count: (String) -> (List<Int>) = { str ->
		str.toSet().map { c -> 
			str.count { it == c }
		}
	}
	val checks: List<(String) -> (Boolean)> = listOf(
		{ uniques(it) == 1 }, // 0: five of a kind
		{ uniques(it) == 2 && count(it)[0] in setOf(4, 1) }, // 1: four of a kind
		{ uniques(it) == 2 && count(it)[0] in setOf(3, 2) }, // 2: full house
		{ uniques(it) == 3 && count(it).any { x -> x == 3 } }, // 3: three of a kind
		{ uniques(it) == 3 && count(it).all { x -> x in setOf(1, 2)} }, // 4: two pairs
		{ count(it).any { x -> x == 2} }, // 5: one pair
		{ count(it).all { x -> x == 1} }, // 6: high card
	)

	val categorised = hands.mapIndexed { origIndex, hand ->
		var ret: Triple<String, Int, Int> = Triple("", -1, -1)
		for ((i, check) in checks.withIndex()) {
			if (check(hand)) {
				ret = Triple(hand, i, origIndex)
				break
			}
		}
		ret
	}

	val rankIt: (Char) -> (Int) = { c ->
		when (c) {
			'A' -> 13
			'K' -> 12
			'Q' -> 11
			'J' -> 10
			'T' -> 9
			'9' -> 8
			'8' -> 7
			'7' -> 6
			'6' -> 5
			'5' -> 4
			'4' -> 3
			'3' -> 2
			'2' -> 1
			else -> throw IllegalArgumentException("Invalid card: $c")
		}
	}

	val sorted = categorised.sortedWith(
		compareBy<Triple<String, Int, Int>> { it.second }
			.thenByDescending { it.first.fold(0) { acc, x -> 
					it.first.fold("") { acc, c -> "${acc}${rankIt(c).toString().padStart(2, '0')}" }.toInt()
				} 
			}
	)

	val scored = sorted.mapIndexed { i, it -> Triple(it.first, sorted.size - i, it.third) }

	var res = 0

	scored.forEach { res += bids[it.third] * it.second }

	println("Winnings: $res")
}

main(args)

