import java.io.File
import java.net.CookieManager
import java.net.CookieHandler
import java.net.HttpCookie
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.net.URI

fun areValidArgs(args: Array<String>): Boolean {
    if (args.size != 2) return false

    return isValidDayArg(args[0]) && isValidPartArg(args[1])
}

fun isValidDayArg(day: String): Boolean {
    val dayInt = day.toIntOrNull()

    if (dayInt == null) return false
    if (dayInt >= 0 && dayInt <= 25) return true
    return false
}

fun isValidPartArg(part: String): Boolean {
    val validParts = listOf("A", "B")
    return validParts.contains(part.toUpperCase())
}

fun createDayDirectory(day: String): Boolean {
    return File("./$day").mkdir()
}

fun createPartKtsFile(day: String, part: String): Boolean {
    return File("./$day/$day-$part.kts").createNewFile()
}

fun createInputFile(day: String): Boolean {
    return File("./$day/$day.input").createNewFile()
}

fun inputFileExists(day: String): Boolean {
    return File("./$day/$day.input").exists()
}

fun getInput(day: Int): String? {
    var session = System.getenv("AOCSESSION")
    if (session == null) {
        println("Could not get input. Set AOCSESSION env variable to authenticate.")
        return null
    }

    println("Requesting input from AOC api")

    var sessionCookie = HttpCookie("session", session)
    sessionCookie.setPath("/")
    sessionCookie.setVersion(0)

    var cookieManager = CookieManager()
    cookieManager.getCookieStore()
            .add(URI.create("https://adventofcode.com"), sessionCookie)

    var client = HttpClient.newBuilder()
            .cookieHandler(cookieManager)
            .version(HttpClient.Version.HTTP_2)
            .build()

    val request = HttpRequest.newBuilder()
            .uri(URI.create("https://adventofcode.com/2016/day/$day/input"))
            .build()

    val response = client.send(request, BodyHandlers.ofString())

    return response.body()
}

fun bootstrap(day: Int, part: String) {
    println("Bootstrapping day $day part $part")

    val dayPadded = day.toString().padStart(2, '0')
    createDayDirectory(dayPadded)
    createPartKtsFile(dayPadded, part)

    if (!inputFileExists(dayPadded)) {
        createInputFile(dayPadded)
        val input = getInput(day)
        input?.let { input -> File("./$dayPadded/$dayPadded.input").writeText(input) }
    }
}

fun main(args: Array<String>) {
    if (!areValidArgs(args)) {
        println("Usage: kotlinc -script bootstrap.kts [day] [part]")
        println("Example: kotlinc -script bootstrap.kts 01 A")
    } else {
        val day = args[0].toInt()
        val part = args[1].toUpperCase()
        bootstrap(day, part)
    }
}

main(args)
