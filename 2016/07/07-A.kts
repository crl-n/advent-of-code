import java.io.File

val input = File("./07/07.input").readLines()

fun isAbba(s: String): Boolean {
    return s.length == 4 && s[0] != s[1] && s.substring(0,2) == s.substring(2,4).reversed()
}
fun containsAbba(s: String): Boolean {
    return s.windowed(4).any { subStr -> isAbba(subStr) }
}

data class IPV7(
    val hnetSequences: MutableList<String> = mutableListOf(),
    val normalSequences: MutableList<String> = mutableListOf()
)
fun IPV7.supportsTls(): Boolean {
    return (
        this.normalSequences.any { sequence -> containsAbba(sequence) } &&
        !this.hnetSequences.any { sequence -> containsAbba(sequence) }
    )
}

// Parse IPV7s from input
val ipv7s = input.map { line ->
    line.split('[', ']').foldIndexed(IPV7()) { i, ipv7, sequence ->
        if (i % 2 == 0) ipv7.normalSequences.add(sequence) else ipv7.hnetSequences.add(sequence)
        ipv7
    }
}

// Count how many IPV7s support transport layer snooping
val tlsSupportCount = ipv7s.count { it.supportsTls() }

println("Solution: $tlsSupportCount")
