import scala.io.Source
import scala.collection.mutable.ListBuffer

val input = Source.fromFile("01.input").getLines().next()

val charBuffer = ListBuffer[Char]()
charBuffer.addAll(input).addOne(input.charAt(0))

val values = charBuffer.map(_.asDigit).toList

val solution = values.sliding(2).collect {
    case List(a, b) if a == b => a
}.sum

println(s"Solution: $solution")
