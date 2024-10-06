import java.io.File

// I really struggled with this one and felt silly when I realized
// how simple the pattern is. The way it clicked for me was when I
// solved basic cases (n = 3, n = 4, n = 5, n = 6, n = 7) on paper.
// The first loser is the elf in the middle of the original circle.
// What you'll notice is that each elf after that follow a pattern
// of steps on the circle, relative to the previous losing elf.
//
// The pattern is as simple as this:
// Starting from middle elf, remove every elf except for when the
// amount of elfs left is equal. In that case, skip the current
// elf and remove the next one instead.

val input = File("./19/19.input").readText().trimEnd().toInt()

fun solve(n: Int): Int {
    val mid = n / 2 + 1
    val elfs = ArrayDeque((mid..n).plus(1..mid - 1))

    while (elfs.size > 1) {
        elfs.removeFirst()
        if (elfs.size % 2 == 0) {
            elfs.addLast(elfs.removeFirst())
        }
    }
    return elfs[0]
}

val solution = solve(input)
println("$solution")
