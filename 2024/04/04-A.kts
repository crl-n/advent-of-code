import java.io.File

val input = File("04.input").readLines()

val cols = input[0].length
val rows = input.size
val term= "XMAS"
val termRev = term.reversed()

// Extension function for checking if it is a match
fun String.isMatch(): Boolean {
    return this.equals(term) || this.equals(termRev)
}

// Counting the horizontal matches is straight forward, we check all substrings of
// length 4 against XMAS and SAMX
fun countHorizontal(input: List<String>): Int {
    return input.sumOf { line ->
        line.windowed(term.length).count(String::isMatch)
    }
}

// To get the diagonals we think of the input as a grid consisting of coordinates
data class Coord(val x: Int, val y: Int)

// Returns all valid diagonals starting from the coord. We only return "downward"
// diagonals to avoid counting duplicates
fun Coord.getDiagonals(): List<List<Coord>> {
    var diagonals = mutableListOf<List<Coord>>()

    // South-east diagonal
    if (this.y <= rows - term.length && this.x <= cols - term.length) {
        diagonals.add(
            (0..<term.length).map { i -> Coord(this.x + i, this.y + i) }
        )
    }

    // South-west diagonal
    if (this.y <= rows - term.length && this.x - (term.length - 1) >= 0) {
        diagonals.add(
            (0..<term.length).map { i -> Coord(this.x - i, this.y + i) }
        )
    }

    return diagonals.toList()
}

// Lists all coords in the input
fun getAllCoords(input: List<String>): List<Coord> {
    return (0..<rows).flatMap { y -> (0..<cols).map { x -> Coord(x, y) } }
}
val allCoords = getAllCoords(input)

// Now we can count the diagonals by iterating throught the grid
fun countDiagonals(): Int {
    val allDiagonals = allCoords.flatMap { coord ->
        coord.getDiagonals()
    }

    val allDiagonalStrings = allDiagonals.map { coords ->
        coords.map { coord -> input[coord.y][coord.x] }.joinToString("")
    }

    return allDiagonalStrings.count(String::isMatch)
}

// Returns a list of coords if a vertical string starts from the coord
// Returns null if there are not enough rows left to form a long enough
// vertical string starting from the coord
fun Coord.getVertical(): List<Coord>? {
    if (this.y + (term.length - 1) < rows) {
        return (0..<term.length).map { i -> Coord(this.x, this.y + i) }
    }
    return null
}

// Now we can easily count the vertical matches by iterating through the grid
fun countVertical(): Int {
    return allCoords.count { coord ->
        coord.getVertical()?.map { coord -> input[coord.y][coord.x] }
            ?.joinToString("")
            ?.isMatch()
            ?: false
    }
}

val totalCount = countHorizontal(input) + countDiagonals() + countVertical()

println("Total XMAS matches: $totalCount")
