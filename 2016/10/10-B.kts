import java.io.File

val input = File("./10/10.input").readLines()

class Bot(val number: Int) {
    var values = mutableListOf<Int>()
    var highDest = -1
    var lowDest = -1
    var destTypeHigh = ""
    var destTypeLow = ""

    fun add(value: Int) {
        this.values.add(value)
    }

    fun give(bots: List<Bot>, outputBins: List<MutableList<Int>>) {
        // Sort values so high is last and low is first
        this.values.sort()

        when (destTypeHigh) {
            "bot" -> bots[highDest].add(values.removeLast())
            "output" -> outputBins[highDest].add(values.removeLast())
            else -> {}
        }
        when (destTypeLow) {
            "bot" -> bots[lowDest].add(values.removeFirst())
            "output" -> outputBins[lowDest].add(values.removeFirst())
            else -> {}
        }
    }

    fun hasTwo(): Boolean {
        return this.values.size == 2
    }

    fun setDests(highDest: Int, destTypeHigh: String, lowDest: Int, destTypeLow: String) {
        this.highDest = highDest
        this.lowDest = lowDest
        this.destTypeHigh = destTypeHigh
        this.destTypeLow = destTypeLow
    }

    override fun toString(): String {
        return "Bot $number: ${this.values.toString()}"
    }
}

fun parseInts(s: String): List<Int> {
    return Regex("\\d+").findAll(s).map { it.value.toInt() }.toList()
}

// Destination type designates whether the destination is a bot or an output bin
fun parseDestTypes(s: String): List<String> {
    return Regex("(bot|output)").findAll(s).toList().map { it.value }.slice(1..2)
}

// Create list of bots, by inspecting input it is clear there are 210 bots
val numberOfBots = 210
val bots = List<Bot>(numberOfBots) { i -> Bot(i) }

// Create list of 21 output bins
val numberOfOutputBins = 21
val outputBins = List<MutableList<Int>>(numberOfOutputBins) { mutableListOf() }

// Parse configuration defined in input
for (line in input) {
    if (line.startsWith("value")) {
        val (value, botNumber) = parseInts(line)
        bots[botNumber].add(value)
    } else {
        val (srcBotNumber, lowDest, highDest) = parseInts(line)
        val (destTypeLow, destTypeHigh) = parseDestTypes(line)
        bots[srcBotNumber].setDests(highDest, destTypeHigh, lowDest, destTypeLow)
    }
}

// Iterate until stable state
while (true) {
    var anyBotHadTwo = false
    for (bot in bots) {
        if (bot.hasTwo()) {
            anyBotHadTwo = true
            bot.give(bots, outputBins)
        }
    }
    if (!anyBotHadTwo) break
}

val solution = outputBins.slice(0..2).flatten().fold(1) { product, term -> product * term }
println("Solution: $solution")
