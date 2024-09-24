import java.io.File
import kotlin.math.abs

data class Instruction(val turn: Char, val dist: Int)
enum class Direction { NONE, E, S, W, N} // East, South, West, North. None at start.

data class Coord(var x: Int = 0, var y: Int = 0)
fun Coord.update(dir: Direction, dist: Int) {
    when (dir) {
        Direction.E -> x += dist
        Direction.S -> y -= dist
        Direction.W -> x -= dist
        Direction.N -> y += dist
        Direction.NONE -> return
    }
}

// Parse input file contents
val input = File("./01/01.input")
    .readText()
    .trimEnd()
    .split(", ")

// Parse instructions from input
val instructions: List<Instruction> = input.map {
    val turn = it[0]
    val dist = it.drop(1).toInt()
    Instruction(turn, dist)
}

// Table of directions. Turn to right means go up one index, turn to left means go down one.
val dirTable = listOf(Direction.E, Direction.S, Direction.W, Direction.N)

// Pointer to keep track of current direction
var dirTablePointer = -1

fun updateDirection(dir: Direction, turn: Char): Direction {
    if (dir.equals(Direction.NONE) && turn.equals("R")) {
        dirTablePointer = 0
        return Direction.E
    } else if (dir.equals(Direction.NONE) && turn.equals("L")) {
        dirTablePointer = 2
        return Direction.W
    }

    if (turn == 'L') {
        dirTablePointer--
    } else if (turn == 'R') {
        dirTablePointer++
    }
    dirTablePointer = (dirTablePointer + 4) % 4 // Keep dirTablePointer within bounds of dirTable and always positive
    return dirTable[dirTablePointer]
}

var direction = Direction.NONE
var coord = Coord()

// Solve
for (instr in instructions) {
    direction = updateDirection(direction, instr.turn)
    coord.update(direction, instr.dist)
}

val solution = abs(coord.x) + abs(coord.y)
println("Solution: $solution")
