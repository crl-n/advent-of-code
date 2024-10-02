import java.io.File

val input = File("./11/11.input").readLines()

enum class Isotope {
    Dilithium,
    Elerium,
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
    abstract val isotope: Isotope
    data class Microchip(override val isotope: Isotope) : Item()
    data class Generator(override val isotope: Isotope) : Item()

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
        return "E${floor + 1}"
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

    // Canonical string encoding of state (treating symmetrical states as equal).
    // Using this to compare states prunes a lot of the search space.
    // Example: All items on the fourth floor would be encoded as follows
    // E4|44-44-44-44-44-44-44
    fun canonicalEncoding(): String {
        // What this ugly piece of code essentially does is encode
        // the state as a series of integers, one for each isotope.
        // Each isotope becomes a 1-2 digit integer. The rightmost
        // digit encodes the floor of the microchip, the left the
        // floor of the generator with that same isotope, e.g.
        // an isotope with both chip and generator at the fourth floor
        // would become 44. If the generator was on the first floor it
        // would become 14. These integers are then sorted and turned
        // into a string and prefixed with a string encoding of the
        // elevator state. The resulting string can be used to treat
        // distinct but symmetrical states as equal to one another.
        val canonicallyEncodedIsotopes = floors.flatMap { floor ->
            floor.items().map { item -> Pair(item, floor.number + 1) }
        }.groupBy { pair -> pair.first.isotope }.map { (isotope, pairs) ->
            pairs.fold(0) { acc, pair ->
                if (pair.first is Item.Microchip) acc + pair.second
                else acc + (pair.second * 10)
            }
        }.sorted().joinToString("-")

        var canonicalEncoding = "${elevator.stringEncoded()}|$canonicallyEncodedIsotopes"

        return canonicalEncoding
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

// Add extra part B items
floors[0].chips.add(Item.Microchip(Isotope.Elerium))
floors[0].generators.add(Item.Generator(Isotope.Elerium))
floors[0].chips.add(Item.Microchip(Isotope.Dilithium))
floors[0].generators.add(Item.Generator(Isotope.Dilithium))

// Start solving
val initialState = State(floors, Elevator(0), 0)
val totalNumberOfItems = 14

// Bread-first search, returns shortest possible path or -1 for no path found
fun search(initialState: State): Int {
    val queue = mutableListOf<State>(initialState)
    val visited = mutableSetOf<String>(initialState.canonicalEncoding())

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
                if (newState.canonicalEncoding() !in visited) {
                    queue.add(newState)
                    visited.add(newState.canonicalEncoding())
                }
            }
        }
    }
    return -1
}

val solution = search(initialState)
println("Solution: $solution")