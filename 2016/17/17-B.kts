import java.io.File
import java.security.MessageDigest

val input = File("./17/17.input").readText().trimEnd()

@OptIn(ExperimentalStdlibApi::class)
fun md5(s: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.toByteArray())
    return digest.toHexString()
}

data class Coord(val x: Int, val y: Int) {

    fun isValid(): Boolean {
        return x >= 0 && x <= 3 && y >= 0 && y <= 3
    }

    fun neighbors(): List<Coord> {
        return listOf(
            Coord(x, y - 1),
            Coord(x, y + 1),
            Coord(x - 1, y),
            Coord(x + 1, y)
        )
    }
}

data class State(
    val path: List<Char> = listOf(),
    val coord: Coord = Coord(0, 0),
)

val dirIndicesToChars = mapOf(
    0 to 'U',
    1 to 'D',
    2 to 'L',
    3 to 'R'
)
val openDoorChars = ('b'..'f').toList()
val goal = Coord(3, 3)
val queue = mutableListOf<State>(State())
var longestPath: List<Char> = listOf()
var current: State = State()

while (queue.size > 0) {
    current = queue.removeFirst()

    if (current.coord == goal) {
        longestPath = current.path
        continue
    }

    val stringToHash = "$input${current.path.joinToString("")}"
    val hash = md5(stringToHash)
    val openDirs = hash.slice(0..3).toList().map { it in openDoorChars }
    val openDirCoordPairs = openDirs.zip(current.coord.neighbors())

    for ((i, pair) in openDirCoordPairs.withIndex()) {
        val (isOpen, coord) = pair
        if (isOpen && coord.isValid()) {
            val dirChar = dirIndicesToChars[i] ?: throw IllegalStateException("Invalid dir char")
            val newPath = current.path.plus(dirChar)
            val newState = State(newPath, coord)
            queue.add(newState)
        }
    }
}

println("Solution: ${longestPath.size}")
