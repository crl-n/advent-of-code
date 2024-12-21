import java.io.File

val input = File("16.input").readLines()

val grid = input.map { line -> line.toList() }

fun List<List<Char>>.at(pos: Pos) = this[pos.y][pos.x]

data class Pos(val x: Int, val y: Int) {
    operator fun minus(pos: Pos) = Pos(x - pos.x, y - pos.y)

    fun nonWallAdjacents(): List<Pos> {
        return listOf(
            Pos(x, y - 1),
            Pos(x + 1, y),
            Pos(x, y + 1),
            Pos(x - 1, y)
        ).filter { !it.isWall() }
    }

    fun inDirFrom(pos: Pos): Dir {
        val offset = this - pos
        return when (offset) {
            Pos(0, 1) -> Dir.Up
            Pos(1, 0) -> Dir.Left
            Pos(0, -1) -> Dir.Down
            Pos(-1, 0) -> Dir.Right
            else -> throw IllegalArgumentException("Positions not adjacent")
        }
    }

    fun isWall(): Boolean {
        return grid.at(this) == '#'
    }
}

enum class Dir {
    Up, Right, Down, Left;

    fun opposite(): Dir {
        when (this) {
            Dir.Up -> return Dir.Down
            Dir.Right -> return Dir.Left
            Dir.Down -> return Dir.Up
            Dir.Left -> return Dir.Right
        }
    }

    fun rotationsNeeded(targetDir: Dir): Int {
        if (this == targetDir) return 0
        if (this == targetDir.opposite()) return 2
        return 1
    }
}

data class State(val pos: Pos, val dir: Dir, val cost: Int)

data class PriorityQueue(val items: MutableList<State>) {
    fun pop(): State {
        val (i, item) = items.withIndex().minBy { it.value.cost }
        items.removeAt(i)
        return item
    }

    fun add(state: State) {
        items.add(state)
    }

    fun hasNext(): Boolean {
        return items.size != 0
    }
}

val startPos = Pos(1, grid.size - 2)
val endPos = Pos(grid[0].size - 2, 1)
val queue = PriorityQueue(mutableListOf(State(startPos, Dir.Right, 0)))
val visited = mutableSetOf(startPos)

while (queue.hasNext()) {
    val state = queue.pop()
    visited.add(state.pos)

    if (state.pos == endPos) {
        println("Solution: ${state.cost}")
        break
    }

    val adjs = state.pos.nonWallAdjacents()

    for (adj in adjs) {
        if (adj in visited) {
            continue
        }
        val newDir = adj.inDirFrom(state.pos)
        val rotations = newDir.rotationsNeeded(state.dir)
        val newState = State(
            pos = adj,
            dir = newDir,
            cost = state.cost + 1 + rotations * 1000
        )
        queue.add(newState)
    }
}
