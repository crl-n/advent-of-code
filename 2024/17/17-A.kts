import java.io.File

// Parse input
val (registerInput, programInput) = File("17.input").readText().split("\n\n")

val initialRegisterValues = registerInput.split("\n").map { line ->
    Regex("\\d+").find(line)!!.value.toInt()
}

val program = programInput.split(" ")
    .let { it[1] }
    .split(",")
    .map(String::toInt)

// Define class for virtual machine
data class Vm(var a: Int, var b: Int, var c: Int, var ip: Int) {
    val output = mutableListOf<Int>()

    fun combo(v: Int): Int {
        return when (v) {
            in 0..3 -> return v
            4 -> return a
            5 -> return b
            6 -> return c
            else -> throw IllegalArgumentException("Illegal combo: $v")
        }
    }

    fun adv(operand: Int) {
        a = a / (0x1 shl combo(operand))
    }

    fun bxl(operand: Int) {
        b = b xor operand
    }

    fun bst(operand: Int) {
        b = combo(operand) % 8
    }

    fun jnz(operand: Int) {
        if (a == 0) return
        ip = operand
    }

    fun bxc(operand: Int) {
        b = b xor c
    }

    fun out(operand: Int) {
        output.add(combo(operand) % 8)
    }

    fun bdv(operand: Int) {
        b = a / (0x1 shl combo(operand))
    }

    fun cdv(operand: Int) {
        c = a / (0x1 shl combo(operand))
    }

    fun run(program: List<Int>) {
        val ops = listOf(
            ::adv,
            ::bxl,
            ::bst,
            ::jnz,
            ::bxc,
            ::out,
            ::bdv,
            ::cdv
        )

        while (ip + 1 < program.size) {
            val opcode = program[ip]
            val operand = program[ip + 1]

            ip += 2

            ops[opcode](operand)
        }
    }

    override fun toString(): String {
        return "VM: A = $a, B = $b, C = $c"
    }
}

// Solve
val vm = Vm(
    ip = 0,
    a = initialRegisterValues[0],
    b = initialRegisterValues[1],
    c = initialRegisterValues[2],
)

vm.run(program)

println(vm.output.joinToString(","))
