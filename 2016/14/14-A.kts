import java.io.File
import java.security.MessageDigest

val input = File("./14/14.input").readText().trimEnd()

@OptIn(ExperimentalStdlibApi::class)
fun md5(s: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.toByteArray())
    return digest.toHexString()
}

val hashMemo = mutableListOf<String>()
fun getHash(i: Int): String {
    while (hashMemo.size <= i) {
        val newHash = md5("$input${hashMemo.size}")
        hashMemo.add(newHash)
    }
    return hashMemo[i]
}

fun getFirstTriple(s: String): String? {
    return s.windowed(3, 1, false)
        .find { window -> window.all { c -> c == window[0] } }
}

fun listQuintuples(s: String): Set<String> {
    return s.windowed(5, 1, false)
        .filter { window -> window.all { c -> c == window[0] } }.toSet()
}

val keysNeeded = 64
val keys = mutableListOf<String>()
var i = 0
while (true) {
    val hash = getHash(i)

    val triple = getFirstTriple(hash)
    if (triple == null) {
        i++
        continue
    }
    val c = triple[0]
    val match = (i + 1..i + 1000).asSequence().any {
        listQuintuples(getHash(it)).any {
            quintuple -> quintuple[0] == c
        }
    }
    if (match) keys.add(hash)
    if (keys.size == keysNeeded) break
    i++
}

println("Solution: $i")

