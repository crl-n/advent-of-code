import java.io.File

val input = File("./04/04.input").readLines()

val rooms = input.map { line ->
    listOf(
        line.substringBeforeLast('-'),
        line.substringAfterLast('-').split("[").first(),
        line.split("[").last().trimEnd(']'),
    )
}

fun rotate(encryptedName: String, iters: Int): String {
    return encryptedName.map { c ->
        if (!c.isLowerCase()) {
            c
        } else {
            ((c.code - 97 + iters) % 26 + 97).toChar() // Ascii code magique
        }
    }.joinToString("").replace('-', ' ')
}

val decryptedNamesWithSectorIds = rooms.map { room ->
    val encryptedName = room[0]
    val sectorIdAsInt = room[1].toInt()

    Pair(rotate(encryptedName, sectorIdAsInt), sectorIdAsInt)
}

decryptedNamesWithSectorIds.filter { it.first.startsWith("north")}
