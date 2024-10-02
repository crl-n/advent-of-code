import java.io.File

val input = File("./12/12.input").readLines()

class Machine(val instructions: List<List<String>>) {
    val registers = mutableMapOf<Char, Int>(
        'a' to 0,
        'b' to 0,
        'c' to 1,
        'd' to 0
    )
    var cursor = 0

    fun run() {
        while (cursor < instructions.size) {
            val instruction = instructions[cursor]
            when (instruction.size) {
                3 -> {
                    val (op, arg1, arg2) = instruction
                    when (op) {
                        "cpy" -> cpy(arg1, arg2)
                        "jnz" -> jnz(arg1, arg2)
                        else -> {}
                    }
                }
                2 -> {
                    val (op, arg1) = instruction
                    when (op) {
                        "inc" -> inc(arg1)
                        "dec" -> dec(arg1)
                        else -> {}
                    }
                }
                else -> {}
            }
        }
    }

    fun cpy(arg1: String, arg2: String) {
        if (arg1[0] in registers.keys) {
            registers[arg2[0]] = registers[arg1[0]] ?: throw IllegalArgumentException("Invalid value in cpy")
        } else {
            registers[arg2[0]] = arg1.toInt()
        }
        cursor++
    }

    fun inc(arg1: String) {
        val currentRegisterValue = registers[arg1[0]] ?: throw IllegalArgumentException("Invalid value in inc")
        registers[arg1[0]] = currentRegisterValue + 1
        cursor++
    }

    fun dec(arg1: String) {
        val currentRegisterValue = registers[arg1[0]] ?: throw IllegalArgumentException("Invalid value in dec")
        registers[arg1[0]] = currentRegisterValue - 1
        cursor++
    }

    fun jnz(arg1: String, arg2: String) {
        if (registers[arg1[0]] == 0) {
            cursor++
            return
        }
        cursor += arg2.toInt()
    }
}

val instructions = input.map { it.split(" ") }
val machine = Machine(instructions)
machine.run()

println("Solution: ${machine.registers.get('a')}")
