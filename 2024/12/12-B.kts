import java.io.File

val input = File("12.input").readLines()

data class Coord(val x: Int, val y: Int) {
    fun value(): Char {
        return input.getOrNull(y)?.getOrNull(x) ?: '#' // A bit hacky but works
    }

    fun adjacentInDir(dir: Direction): Coord {
        when (dir) {
            Direction.Up -> return Coord(x, y - 1)
            Direction.Right -> return Coord(x + 1, y)
            Direction.Down -> return Coord(x, y + 1)
            Direction.Left -> return Coord(x - 1, y)
        }
    }

    fun isValid(): Boolean {
        return x >= 0 && y >= 0 && x < input.first().length && y < input.size
    }

    fun adjacents(): List<Coord> {
        return listOf(
            Coord(x, y - 1),
            Coord(x + 1, y),
            Coord(x, y + 1),
            Coord(x - 1, y),
        ).filter { it.isValid() }
    }
}

enum class Direction {
    Up, Down, Left, Right
}

data class Region(val char: Char, val topLeft: Coord, val coords: List<Coord>) {
    fun area(): Int {
        return coords.size
    }

    fun perimeter(): Int {
        if (coords.size == 1) {
            return 4 // The region is a dot, hence 4 sides and perimeter 4
        }

        // If the region consists of multiple plots, we count the corners of the shape,
        // the perimeter will be the same as the number of corners
        var cornersTotal = 0

        for (coord in coords) {
            val up = coord.adjacentInDir(Direction.Up)
            val right = coord.adjacentInDir(Direction.Right)
            val down = coord.adjacentInDir(Direction.Down)
            val left = coord.adjacentInDir(Direction.Left)

            // We check the 3 coords around a corner, for each corner
            val cornerAdjacents = listOf(
                listOf(up, right, up.adjacentInDir(Direction.Right)),
                listOf(right, down, right.adjacentInDir(Direction.Down)),
                listOf(down, left, down.adjacentInDir(Direction.Left)),
                listOf(left, up, left.adjacentInDir(Direction.Up))
            )

            val corners = cornerAdjacents.map { list -> list.map { it.value() } }
                .count { list ->
                    val (a, b, diagonal) = list
                    val isOutsideCorner = list.all { it != char }
                    val isInsideCorner = a == char && b == char && diagonal != char
                    val isTrickyCorner = a != char && b != char && diagonal == char
                    isOutsideCorner || isInsideCorner || isTrickyCorner
                }

            cornersTotal += corners
        }

        return cornersTotal
    }

    companion object {
        fun from(startCoord: Coord): Region {
            val regionCoords = mutableListOf(startCoord)

            var i = 0
            while (i < regionCoords.size) {
                val current = regionCoords[i]
                val adjacentsOfSameType = current.adjacents().filter {
                    it !in regionCoords && it.value() == startCoord.value()
                }
                regionCoords.addAll(adjacentsOfSameType)
                i++
            }
            return Region(startCoord.value(), startCoord, regionCoords)
        }
    }
}

val regions = mutableListOf<Region>()
val visited = mutableSetOf<Coord>()

for ((y, line) in input.withIndex()) {
    for ((x, c) in line.withIndex()) {
        val currentCoord = Coord(x, y)

        if (currentCoord in visited) {
            continue
        }

        val region = Region.from(currentCoord)
        regions.add(region)
        visited.addAll(region.coords)
    }
}

val totalCost = regions.sumOf { it.area() * it.perimeter() }
println("Solution: $totalCost")
