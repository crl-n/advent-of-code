import java.io.File

val (gridInput, commandsInput) = File("15.input").readText().split("\n\n")

data class Pos(var x: Int, var y: Int) {
    fun adjacent(dir: Char): Pos {
        return when (dir) {
            '^' -> return Pos(x, y - 1)
            '>' -> return Pos(x + 1, y)
            'v' -> return Pos(x, y + 1)
            '<' -> return Pos(x - 1, y)
            else -> throw IllegalArgumentException("Illegal argument to adjacent: $dir")
        }
    }
}

fun oppositeDir(dir: Char): Char {
    return when (dir) {
        '^' -> 'v'
        '>' -> '<'
        'v' -> '^'
        '<' -> '>'
        else -> throw IllegalArgumentException("Illegal argument to oppositeDir: $dir")
    }
}

data class State(val grid: List<MutableList<Char>>, var botPos: Pos) {

    fun charAt(pos: Pos) = grid[pos.y][pos.x]

    fun setCharAt(pos: Pos, char: Char) {
        grid[pos.y][pos.x] = char
    }

    // Returns null for false and the first empty pos for true
    fun canMove(pos: Pos, dir: Char): Pos? {
        val nextPos = pos.adjacent(dir)
        val nextPosChar = charAt(nextPos)

        if (nextPosChar == '.') return nextPos
        if (nextPosChar == 'O') return canMove(nextPos, dir)
        return null
    }

    // Takes the first empty pos and recursively moves boxes
    fun pushBoxes(pos: Pos, dir: Char) {
        val nextPos = pos.adjacent(dir)
        val nextPosChar = charAt(nextPos)

        when (nextPosChar) {
            'O' -> {
                setCharAt(pos, nextPosChar)
                return pushBoxes(nextPos, dir)
            }
            '@' -> {
                setCharAt(pos, nextPosChar)
                setCharAt(nextPos, '.')
            }
            else -> throw IllegalStateException("Illegal state in pushBoxes, nextPosChar = $nextPosChar")
        }
    }

    fun execute(c: Char) {
        if (c !in setOf('^', '>', 'v', '<')) return

        val firstEmtpyPos = canMove(botPos, c)

        if (firstEmtpyPos != null) {
            pushBoxes(firstEmtpyPos, oppositeDir(c))
            botPos = botPos.adjacent(c)
        }
    }

    fun run(commands: String) {
        for (command in commands) {
            execute(command)
        }
    }

    fun gpsSum(): Int {
        var sum = 0

        for ((y, row) in grid.withIndex()) {
            for ((x, char) in row.withIndex()) {
                if (char == 'O') {
                    sum += y * 100 + x
                }
            }
        }
        return sum
    }

    fun visualize() {
        for (row in grid) {
            println(row.joinToString(""))
        }
        println()
    }
}

fun stateFrom(input: String): State {
    // Parse initial grid state
    val grid = input.split("\n").map { it.toMutableList() }

    // Find initial bot position
    val botPos = grid.indexOfFirst { row -> '@' in row }.let { y ->
        val x = grid[y].indexOfFirst { c -> c == '@' }
        Pos(x, y)
    }

    return State(grid, botPos)
}

// Solve
val state = stateFrom(gridInput)
state.visualize()

state.run(commandsInput)
state.visualize()

println("Solution: ${state.gpsSum()}")
