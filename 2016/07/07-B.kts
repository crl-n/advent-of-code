import java.io.File

val input = File("./07/07.input").readLines()

fun isAba(s: String): Boolean {
    return s.length == 3 && s[0] != s[1] && s[0] == s[2]
}

fun abaToBab(aba: String): String {
    return "${aba.substring(1)}${aba[1]}"
}

fun getAllAbas(s: String): List<String> {
    return s.windowed(3).filter { isAba(it) }
}

fun containsBab(s: String, validBabs: List<String>): Boolean {
    return s.windowed(3).any { it in validBabs }
}

data class IPV7(
    val hnetSequences: MutableList<String> = mutableListOf(),
    val snetSequences: MutableList<String> = mutableListOf()
)

fun IPV7.supportsSsl(): Boolean {
    val abas = this.snetSequences.flatMap { getAllAbas(it) }
    val validBabs = abas.map { abaToBab(it) }
    return this.hnetSequences.any { containsBab(it, validBabs) }
}

// Parse IPV7s from input
val ipv7s = input.map { line ->
    line.split('[', ']').foldIndexed(IPV7()) { i, ipv7, sequence ->
        if (i % 2 == 0) ipv7.snetSequences.add(sequence) else ipv7.hnetSequences.add(sequence)
        ipv7
    }
}

// Count how many IPV7s support super secret listening
val sslSupportCount = ipv7s.count { it.supportsSsl() }

println("Solution: $sslSupportCount")
