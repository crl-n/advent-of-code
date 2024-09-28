import java.io.File

val input = File("./09/09.input").readText().trimEnd()

fun parseInts(s: String): List<Int> {
    return Regex("\\d+").findAll(s).map { it.value.toInt() }.toList()
}

val markerRegex = Regex("\\(\\d+x\\d+\\)")

// Returns length of decompressed string, recursively decompressing decompressed parts
fun decompress(s: String, multiplier: Int): Long {
    var i = 0
    var decompressedLength = 0L

    while (i < s.length) {
        val match = markerRegex.find(s, i)

        if (match != null) {
            // Get the subsequent string using parsed marker args
            val (subsequentLength, subsequentMultiplier) = parseInts(match.value)
            val subsequent = s.substring(match.range.last + 1, match.range.last + 1 + subsequentLength)

            // Recursively decompress
            val decompressedSubsequentLength = decompress(subsequent, subsequentMultiplier)

            // Match can be ahead of i, so we need to add the difference to our total length
            val charsBeforeMatch = match.range.first - i

            // Increment i and decompressedLength
            i = match.range.last + 1 + subsequentLength
            decompressedLength += decompressedSubsequentLength + charsBeforeMatch
        } else {
            // If there are no matches left or no matches at all in s we can return early
            val charsLeft = s.length - i
            return (decompressedLength + charsLeft) * multiplier
        }
    }
    return decompressedLength * multiplier
}

val solution = decompress(input, 1)
println("Solution: $solution")
