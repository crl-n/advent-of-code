import java.io.File

val input = File("06.input").readLines()

enum class Direction {
    Up, Down, Left, Right;

    fun next(): Direction {
        when (this) {
            Up -> return Right
            Right -> return Down
            Down -> return Left
            Left -> return Up
        }
    }
}

data class Coord(val x: Int, val y: Int) {
    fun adjacentInDirection(direction: Direction): Coord {
        when (direction) {
            Direction.Up -> return Coord(x, y - 1)
            Direction.Right -> return Coord(x + 1, y)
            Direction.Down -> return Coord(x, y + 1)
            Direction.Left -> return Coord(x - 1, y)
        }
    }
}

data  class Guard(
    var pos: Coord,
    var dir: Direction
)

data class State(
    val grid: List<List<Char>>
) {
    var guard: Guard
    val visited: MutableSet<Coord> = mutableSetOf()

    init {
        // Find the position of the guard
        val y = grid.indexOfFirst { it.contains('^') }
        val x = grid[y].indexOfFirst { it == '^' }
        guard = Guard(
            Coord(x, y),
            Direction.Up
        )
    }

    fun getCellType(coord: Coord): Char? {
        return grid.getOrNull(coord.y)
            ?.getOrNull(coord.x)
            ?: null
    }

    fun guardCanMoveTo(destinationCell: Coord): Boolean {
        if (!isValidCoord(destinationCell)) return true
        return getCellType(destinationCell) in listOf('.', '^')
    }

    fun isValidCoord(pos: Coord): Boolean {
        return pos.y >= 0
                && pos.y < grid.size
                && pos.x >= 0
                && pos.x < grid[0].size
    }

    fun moveGuardTo(destinationCell: Coord) {
        guard.pos = destinationCell
    }

    fun rotateGuard() {
        guard.dir = guard.dir.next()
    }

    fun markCurrentAsVisited() {
        visited.add(guard.pos)
    }
}

val state = State(grid = input.map { line -> line.toCharArray().toList() })

while (state.isValidCoord(state.guard.pos)) {
    state.markCurrentAsVisited()

    val destinationCell = state.guard.pos.adjacentInDirection(state.guard.dir)

    if (state.guardCanMoveTo(destinationCell)) {
        state.moveGuardTo(destinationCell)
    } else {
        state.rotateGuard()
    }
}

println("Solution: ${state.visited.size}")
