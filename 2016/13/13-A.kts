import java.io.File

val input = File("./13/13.input").readText().trimEnd()

val favoriteNumber = input.toInt()

// **
// ** Incremental calculation
// **

// I've split the expression f(x) = x*x + 3*x + 2*x*y + y + y*y
// into three functions. f(x, y) = g(x) + h(x, y) + i(y), where
// g(x) = x*x + 3*x
// h(x, y) = 2*x*y
// i(y) = y + y*y
//
// Instead of using g and i directly, I memoize values for g(x)
// and i(y) and use the functions g(x+1) - g(x) and i(y+1) - i(y)
// to incrementally calculate values for g(x) based on previous values.
// This reduces the amount of operations needed for figure out values
// for those parts of the functions. h(x, y) is calculated as is.
//
// The functions for g(x+1) - g(x) and i(y+1) - i(y) I solved by hand
// on paper.

// ** g(x+1) - g(x)
fun ginc(x: Int): Int { return 2 * x + 4 }

// ** i(y+1) - i(y)
fun iinc(y: Int): Int { return 2 * y + 2 }

val gmemo = mutableListOf<Int>(0)
val imemo = mutableListOf<Int>(0)

fun g(x: Int): Int {
    while (gmemo.size <= x) {
        gmemo.add(gmemo.last() + ginc(gmemo.size - 1)) // Just think it through, you'll get it
    }
    return gmemo[x]
}

fun i(y: Int): Int {
    while (imemo.size <= y) {
        imemo.add(imemo.last() + iinc(imemo.size - 1))
    }
    return imemo[y]
}

fun h(x: Int, y: Int): Int {
    return 2 * x * y
}

fun f(x: Int, y: Int): Int {
    return g(x) + h(x, y) + i(y) + favoriteNumber
}

// Now we have the functions we need to relatively efficiently
// calculate values for the equation. To get the tile type we
// use the following function.
fun isOpenTile(x: Int, y: Int): Boolean {
    return f(x, y).countOneBits() % 2 == 0
}

// **
// ** Breadth-first search
// **

data class Coord(val x: Int, val y: Int) {
    fun isValid(): Boolean {
        return x >= 0 && y >= 0
    }

    fun neighbors(): List<Coord> {
        return listOf(
            Coord(x, y - 1),
            Coord(x + 1, y),
            Coord(x, y + 1),
            Coord(x - 1, y),
        ).filter { it.isValid() }
    }
}

fun search(): Int {
    val initialMoves = 0
    val start = Coord(1, 1)
    val initialState = Pair(start, initialMoves)
    val queue = mutableListOf<Pair<Coord, Int>>(initialState)
    val visited = mutableSetOf(start)

    while (true) {
        val (current, moves) = queue.removeFirst()

        if (current == Coord(31, 39)) return moves

        for (neighbor in current.neighbors()) {
            if (neighbor !in visited
                && isOpenTile(neighbor.x, neighbor.y)
            ) {
                queue.add(Pair(neighbor, moves + 1))
                visited.add(neighbor)
            }
        }
    }
}

println("Solution: ${search()}")
