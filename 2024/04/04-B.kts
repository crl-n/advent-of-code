import java.io.File

val input = File("04.input").readLines()

val cols = input[0].length
val rows = input.size
val term= "MAS"
val termRev = term.reversed()

// Extension function for checking if it is a match
fun String.isMatch(): Boolean {
    return this.equals(term) || this.equals(termRev)
}

// We think of the input as a grid consisting of coordinates
data class Coord(val x: Int, val y: Int)

// Lists all coords in the input
fun getAllCoords(input: List<String>): List<Coord> {
    return (0..<rows).flatMap { y -> (0..<cols).map { x -> Coord(x, y) } }
}
val allCoords = getAllCoords(input)

fun Coord.getSEDiagonal(): List<Coord>? {
    if (this.y <= rows - term.length && this.x <= cols - term.length) {
        return (0..<term.length).map { i -> Coord(this.x + i, this.y + i) }
    }
    return null
}

fun Coord.getSWDiagonal(): List<Coord>? {
    if (this.y <= rows - term.length && this.x - (term.length - 1) >= 0) {
        return (0..<term.length).map { i -> Coord(this.x - i, this.y + i) }
    }
    return null
}

fun countCrosses(): Int {
    var count = 0

    for (coord in allCoords) {
        // Check if a south-east diagonal can be formed
        val seDiagonal = coord.getSEDiagonal()
        if (seDiagonal == null) continue

        // Check if the south-west diagonal of the cross can be formed
        val swDiagonalStartCoord = Coord(coord.x + 2, coord.y)
        val swDiagonal = swDiagonalStartCoord.getSWDiagonal()
        if (swDiagonal == null) continue

        // Check if the cross is a X-MAS cross
        val seString = seDiagonal.map { coord -> input[coord.y][coord.x] }.joinToString("")
        val swString = swDiagonal.map { coord -> input[coord.y][coord.x] }.joinToString("")
        if (seString.isMatch() && swString.isMatch()) {
            count++
        }
    }
    return count
}

val totalCount = countCrosses()

println("Total XMAS matches: $totalCount")
