import java.io.File
import kotlin.math.pow

fun main() {
    println(part2())
}

private fun part2(): Int {

    val resultMap = mutableMapOf<Int, Int>()
    val input = File("src/Day04.txt").readLines()
    for (i in 1..input.size) {
        resultMap[i] = 1
    }

    input.forEachIndexed { index, line ->
        val (myNubersPart, winningPart) = line.substringAfter(':').trim().split("|")
        val myNubers = myNubersPart.split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }
        val winning = winningPart.split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }

        val iWon = myNubers.size - (myNubers - winning.toSet()).size
        if (iWon != 0) {
            val gameIndsx = index + 1
            val allRoudCardsWon = resultMap[gameIndsx]!!
            println("---Game $gameIndsx won, $iWon times saved $allRoudCardsWon")
            repeat(iWon) {
                val nextCardIndx = index + 2 + it
                val nexCards = resultMap[nextCardIndx]!! + allRoudCardsWon
                resultMap[nextCardIndx] = nexCards
                println("-----Game $nextCardIndx :  $nexCards")
            }
        }
    }

    println(resultMap)

    return resultMap.values.sum()

}

private fun part1(): Int {

    return File("src/Day04.txt").readLines().map {
        val (myNubersPart, winningPart) = it.substringAfter(':').trim().split("|")
        val myNubers = myNubersPart.split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }
        val winning = winningPart.split(" ").filter { it.isNotEmpty() }.map { it.trim().toInt() }

        val iWon = myNubers.size - (myNubers - winning.toSet()).size
        if (iWon == 0) {
            0.0
        } else {
            2.0.pow(iWon - 1)
        }
    }.sum().toInt()
}
