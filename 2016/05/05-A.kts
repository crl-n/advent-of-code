import java.io.File
import java.security.MessageDigest

val input = File("./05/05.input").readText().trim()

@OptIn(ExperimentalStdlibApi::class)
fun md5(s: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(s.toByteArray())
    return digest.toHexString()
}

var i = 1
var pw = ""
while (pw.length < 8) {
    val hash = md5("$input$i")
    if (hash.startsWith("00000")) pw += hash[5]
    i++
}

println("Solution: $pw")
