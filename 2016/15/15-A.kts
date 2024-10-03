import java.io.File

val input = File("./15/15.input").readLines()

fun parseInts(s: String): List<Int> {
    return Regex(" \\d+").findAll(s).map {
        it.value.trim().toInt()
    }.toList()
}

data class Disc(val i: Int, val positions: Int, val start: Int) {
    // target is where we want the disc to be when we drop the capsule
    val target = positions - 1 - (i % positions)

    fun firstTimeAtTarget(): Int {
        return positions - target
    }

    fun positionAtTime(time: Int): Int {
        return (start + time) % positions
    }

    override fun toString(): String {
        return "Disc(i=$i, positions=$positions, start=$start, target=$target)"
    }
}

// ** Parse discs from input
val discs = mutableListOf<Disc>()
for ((i, line) in input.withIndex()) {
    val (positions, start) = parseInts(line)
    val disc = Disc(i, positions, start)
    discs.add(disc)
}

val firstDisc = discs[0]
val remainingDiscs = discs.slice(1..discs.size - 1)

var time = firstDisc.firstTimeAtTarget() // Start checking first time first disc is at target

while (true) {
    val remainingDiscsAtTarget = remainingDiscs.all {
        it.positionAtTime(time) == it.target
    }
    if (remainingDiscsAtTarget) break
    time += firstDisc.positions // Only check points in time where first disc is at target
}

println("Solution: $time")
