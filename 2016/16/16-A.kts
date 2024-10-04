import java.io.File

val input = File("./16/16.input").readText().trimEnd()

fun List<Boolean>.flip(): List<Boolean> {
    return this.map { if (it) false else true }
}

fun List<Boolean>.toBitString(): String {
    return this.map {
        if (it) '1' else '0'
    }.joinToString("")
}

fun dCurve(s: String, targetSize: Int): List<Boolean> {
    val a = s.map { c -> if (c == '1') true else false }.toMutableList()
    while (a.size < targetSize) {
        val b = a.reversed().flip()
        a.add(false)
        a.addAll(b)
    }
    val result = a.slice(0..targetSize - 1)
    return result
}

fun List<Boolean>.checkSum(): List<Boolean> {
    var checksum = mutableListOf<Boolean>()
    for (pair in this.windowed(2, 2, false)) {
        if (pair[0] == pair[1]) checksum.add(true) else checksum.add(false)
    }
    if (checksum.size % 2 != 0) return checksum.toList()
    return checksum.checkSum()
}

val solution = dCurve(input, 272).checkSum().toBitString()
println("Solution: $solution")
