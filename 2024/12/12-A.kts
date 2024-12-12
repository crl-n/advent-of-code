import java.io.File

val input = File("12.input").readLines()

data class Coord(val x: Int, val y: Int) {
    fun value(): Char {
        return input[y][x]
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

data class Region(val char: Char, val coords: List<Coord>) {
    fun area(): Int {
        return coords.size
    }

    fun perimeter(): Int {
        val sameRegionAdjacentsCounts = coords.map {
            it.adjacents().filter { it.isValid() && it.value() == char }.size
        }
        return sameRegionAdjacentsCounts.map { count -> 4 - count }.sum()
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
            return Region(startCoord.value(), regionCoords)
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
