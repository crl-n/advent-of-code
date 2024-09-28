import java.io.File

val input = File("./09/09.input").readText().trimEnd()

fun parseInts(s: String): List<Int> {
    return Regex("\\d+").findAll(s).map { it.value.toInt() }.toList()
}

var i = 0
var copyFrom = 0
val markerRegex = Regex("\\(\\d+x\\d+\\)")
val decompressedParts = MutableList<String>(0, { i -> "" })

while (i < input.length) {
    if (input[i] != '(') {
        i++
        continue
    }

    markerRegex.find(input, i)?.let { match ->
        // Find next marker and parse marker args
        val markerArgs = parseInts(match.value)
        val subsequentLength = markerArgs[0]
        val multiplier = markerArgs[1]

        // Retrieve part subsequent to marker
        val subsequentStart = match.range.last + 1
        val subsequentEnd = match.range.last + subsequentLength + 1
        val subsequent = input.substring(subsequentStart, subsequentEnd)

        // Retrieve part preceding marker
        val markerStart = match.range.first
        val preceding = input.substring(copyFrom, markerStart)
        decompressedParts.add(preceding)

        // Multiply subsequent part
        (1..multiplier).forEach { decompressedParts.add(subsequent) }

        // Increment i
        i = subsequentEnd
        copyFrom = i
    }
}

// Add part trailing last marker
decompressedParts.add(input.substring(copyFrom, input.length))

val out = decompressedParts.joinToString("")
val solution = out.length

println("Solution: $solution")
