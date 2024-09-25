import java.io.File
import java.security.MessageDigest

val input = File("./05/05.input").readText().trim()

@OptIn(ExperimentalStdlibApi::class)
fun md5(s: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.toByteArray())
    return digest.toHexString()
}

var pw = MutableList<Char>(8, { _ -> ' ' })
var stops = (0..7).map { x -> "00000$x" }.toMutableList()

var i = 1
while (stops.size > 0) {
    val hash = md5("$input$i")
    val hashSubStr = hash.substring(0, 6)
    if (hashSubStr in stops) {
        pw[hashSubStr.toInt()] = hash[6]
        stops.remove(hashSubStr)
    }
    i++
}

val solution = pw.joinToString("")
println("Solution: $solution")
