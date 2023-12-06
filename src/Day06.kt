import java.io.File

fun main() {

    val f = File("src/Day06.txt").readLines()
    val times =
        listOf(f[0].substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }.joinToString("")
            .toLong()
        )
    val records =
        listOf(f[1].substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }.joinToString("")
            .toLong()
        )

    times.zip(records).map { (time, record) ->
        val range = (1..time)
        val first = range.indexOfFirst { it * (time - it) > record }
        val last = range.indexOfLast { it * (time - it) > record }
        last - first + 1
    }.reduce { acc, i ->
        acc * i
    }.also {
        println(it)
    }
}
