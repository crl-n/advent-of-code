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

data class CoordWithDir(
    val x: Int,
    val y: Int,
    val direction: Direction,
)

data  class Guard(
    var pos: Coord,
    var dir: Direction
)

data class State(
    val grid: List<List<Char>>
) {
    val originalGuardPos: Coord
    var guard: Guard
    val visited: MutableSet<CoordWithDir> = mutableSetOf()

    init {
        // Find the position of the guard
        val y = grid.indexOfFirst { it.contains('^') }
        val x = grid[y].indexOfFirst { it == '^' }
        guard = Guard(
            Coord(x, y),
            Direction.Up
        )
        originalGuardPos = Coord(x, y)
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
        visited.add(CoordWithDir(
            guard.pos.x,
            guard.pos.y,
            guard.dir
        ))
    }

    fun isVisited(): Boolean {
        return visited.contains(CoordWithDir(
            guard.pos.x,
            guard.pos.y,
            guard.dir
        ))
    }

    // Returns true if run results in loop
    fun run(): Boolean {
        while (this.isValidCoord(guard.pos)) {
            if (this.isVisited()) return true
            this.markCurrentAsVisited()

            val destinationCell = guard.pos.adjacentInDirection(guard.dir)

            if (this.guardCanMoveTo(destinationCell)) {
                this.moveGuardTo(destinationCell)
            } else {
                this.rotateGuard()
            }
        }
        return false
    }

    fun copyWithObstruction(obstructionCoord: Coord): State {
        val modifiedInput = input.map { line ->
            line.toCharArray().toMutableList()
        }

        modifiedInput.get(obstructionCoord.y)
            .set(obstructionCoord.x, '#')

        return State(grid = modifiedInput)
    }
}

// Solve: First we solve the original map to get the full set of positions the guard will
// visit. We then test adding obstructions to each of the positions the guard will visit
// and perform a check if it will cause a loop.
val state = State(grid = input.map { line -> line.toCharArray().toList() })
state.run()

var loopCount = 0

// Filter out duplicates as visited now may contain the same coord many time
val originalVisited = state.visited.map { Coord(it.x, it.y) }.toSet().toList()

for (coord in originalVisited) {
    // We don't try to obstruct the starting position
    if (coord.equals(state.originalGuardPos)) continue
    val newState = state.copyWithObstruction(coord)
    val isLoop = newState.run()

    if (isLoop) {
        loopCount++
    }
}

println("Solution: $loopCount")
