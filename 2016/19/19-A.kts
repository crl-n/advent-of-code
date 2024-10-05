import java.io.File

val input = File("./19/19.input").readText().trimEnd()

fun solve(elfs: Sequence<Int>, skipInitial: Boolean): Sequence<Int> {
    var skipTop = skipInitial
    val newElfs = sequence<Int> {
        var skip = skipInitial
        val elfsIterator = elfs.iterator()
        while (elfsIterator.hasNext()) {
            val elf = elfsIterator.next()
            if (!skip) yield(elf)
            skip = !skip
            skipTop = skip
        }
    }
    val n = newElfs.count()
    if (n == 1) return newElfs

    return solve(newElfs, skipTop)
}

val n = input.toInt()
var elfs = (1..n).asSequence()
val solution = solve(elfs, false).toList()
println("Solution: ${solution.first()}")
