import java.io.File

val input = File("./11/11.input").readLines()

enum class Isotope {
    Plutonium,
    Promethium,
    Ruthenium,
    Strontium,
    Thulium;

    fun stringEncoded(): String {
        return this.name.slice(0..1).uppercase()
    }

    companion object {
        fun fromString(value: String): Isotope {
            return values().find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid isotope: $value")
        }

        fun valuesLowerCase(): List<String> {
            return values().map { it.name.lowercase() }
        }
    }
}

// Direction elevator can travel in
enum class Direction {
    Up,
    Down
}

data class Move(val items: List<Item>, val direction: Direction)

sealed class Item {
    data class Microchip(val isotope: Isotope) : Item()
    data class Generator(val isotope: Isotope) : Item()

    fun stringEncoded(): String {
        when (this) {
            is Microchip -> return "${this.isotope.stringEncoded()}M"
            is Generator -> return "${this.isotope.stringEncoded()}G"
        }
    }
}

data class Floor(
    val number: Int,
    val generators: MutableList<Item.Generator>,
    val chips: MutableList<Item.Microchip>
) {
    fun items(): List<Item> {
        return this.generators.plus(this.chips).sortedBy { it.stringEncoded() }
    }

    fun fried(): List<Item.Microchip> {
        val generatorIsotopes = generators.map { it.isotope }
        val unprotectedChips = chips.filter { chip ->
            chip.isotope !in generatorIsotopes
        }
        val isotopesOtherThanUnprotected = generatorIsotopes.filter { isotope ->
            isotope !in unprotectedChips.map { it.isotope }
        }

        if (isotopesOtherThanUnprotected.size != 0) {
            return unprotectedChips
        }
        return listOf<Item.Microchip>()
    }

    fun subsets(): List<List<Item>> {
        val items = this.generators.plus(this.chips)

        if (items.size == 0) return listOf()

        val subsetsOfOneIndices = items.indices.toList().map { listOf(it) }
        val subsetsOfTwoIndices = items.indices.flatMap { i ->
            (i + 1..items.size - 1).map { j -> listOf(i, j) }
        }
        val allValidSubsetsIndices = subsetsOfOneIndices.plus(subsetsOfTwoIndices)
        val subsets = allValidSubsetsIndices.map { it.map { i -> items[i] } }

        return subsets
    }

    fun add(itemsToAdd: List<Item>) {
        val chipsToAdd = itemsToAdd.filterIsInstance<Item.Microchip>()
        val generatorsToAdd = itemsToAdd.filterIsInstance<Item.Generator>()

        val chipsSizeBefore = this.chips.size
        this.chips.addAll(chipsToAdd)

        val generatorsSizeBefore = this.generators.size
        this.generators.addAll(generatorsToAdd)
    }

    fun remove(itemsToRemove: List<Item>) {
        val chipsToRemove = itemsToRemove.filterIsInstance<Item.Microchip>()
        val generatorsToRemove = itemsToRemove.filterIsInstance<Item.Generator>()

        val chipsSizeBefore = this.chips.size
        this.chips.removeAll { it in chipsToRemove }

        val generatorsSizeBefore = this.generators.size
        this.generators.removeAll { it in generatorsToRemove }
    }

    fun deepCopy(): Floor {
        val copy = Floor(
            this.number,
            this.generators.map { it.copy() }.toMutableList(),
            this.chips.map { it.copy() }.toMutableList()
        )
        return copy
    }

    fun stringEncoded(): String {
        return this.items().map { it.stringEncoded() }.joinToString(" ")
    }

    override fun toString(): String {
        return "Floor $number:\nGenerators ${generators}\nMicrochips $chips\n"
    }
}

data class Elevator(var floor: Int) {
    fun moveUp() {
        this.floor++
        assert(this.floor >= 0 && this.floor <= 3)
    }

    fun moveDown() {
        this.floor--
        assert(this.floor >= 0 && this.floor <= 3)
    }

    fun stringEncoded(): String {
        return "E$floor"
    }
}

data class State(
    val floors: MutableList<Floor>,
    val elevator: Elevator,
    var movesTaken: Int
) {

    fun currentFloor(): Floor {
        return floors[elevator.floor]
    }

    fun isValidState(): Boolean {
        val friedChips = this.floors.flatMap { floor -> floor.fried() }
        return friedChips.size == 0
    }

    fun itemsOnFloor(number: Int): Int {
        return this.floors[number].generators.size + this.floors[number].chips.size
    }

    fun transition(move: Move): State {
        val newState = this.deepCopy()

        newState.movesTaken++
        newState.currentFloor().remove(move.items)

        when (move.direction) {
            Direction.Up -> newState.elevator.moveUp()
            Direction.Down -> newState.elevator.moveDown()
        }

        newState.currentFloor().add(move.items)

        return newState
    }

    fun deepCopy(): State {
        val copy = State(
            this.floors.map { it.deepCopy() }.toMutableList(),
            this.elevator.copy(),
            this.movesTaken
        )
        return copy
    }

    fun stringEncoded(): String {
        val floorsEncoded = this.floors.map { it.stringEncoded() }.joinToString("|")
        val elevatorEncoded = this.elevator.stringEncoded()
        return "$elevatorEncoded|$floorsEncoded"
    }

    override fun toString(): String {
        return "State:\n${this.floors.joinToString("")}"
    }
}

fun parseGenerators(s: String): List<Item.Generator> {
    return Regex("(${Isotope.valuesLowerCase().joinToString("|")})(?: generator)")
        .findAll(s)
        .map { Isotope.fromString(it.groupValues[1]) }
        .map { isotope -> Item.Generator(isotope) }
        .toList()
}

fun parseMicrochips(s: String): List<Item.Microchip> {
    return Regex("(${Isotope.valuesLowerCase().joinToString("|")})(?:-compatible microchip)")
        .findAll(s)
        .map { Isotope.fromString(it.groupValues[1]) }
        .map { isotope -> Item.Microchip(isotope) }
        .toList()
}

// Initialize list of floors
val floors = mutableListOf<Floor>()

// Parse initial state from input
for ((i, line) in input.withIndex()) {
    val generators = parseGenerators(line).toMutableList()
    val chips = parseMicrochips(line).toMutableList()
    floors.add(Floor(i, generators, chips))
}

// Start solving
val initialState = State(floors, Elevator(0), 0)
val totalNumberOfItems = 10

// Bread-first search, returns shortest possible path or -1 for no path found
fun search(initialState: State): Int {
    val queue = mutableListOf<State>(initialState)
    val visited = mutableSetOf<String>(initialState.stringEncoded())

    while (queue.isNotEmpty()) {
        val currentState = queue.removeFirst()

        if (currentState.itemsOnFloor(3) == totalNumberOfItems) {
            return currentState.movesTaken
        }

        if (!currentState.isValidState()) {
            continue
        }

        // Move each subset of size 1 and 2 up and down in order to search
        // all possible states we can transition to from the current state
        for (subset in currentState.currentFloor().subsets()) {
            val subsetMoves = mutableListOf<Move>()

            if (currentState.currentFloor().number != 3) subsetMoves.add(
                Move(subset, Direction.Up)
            )
            if (currentState.currentFloor().number != 0) subsetMoves.add(
                Move(subset, Direction.Down)
            )

            subsetMoves.forEach { move ->
                val newState = currentState.transition(move)
                if (newState.stringEncoded() !in visited) {
                    queue.add(newState)
                    visited.add(newState.stringEncoded())
                }
            }
        }
    }
    return -1
}

val solution = search(initialState)
println("Solution: $solution")
