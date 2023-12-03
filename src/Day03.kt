import java.io.File

fun main() {
    val matrix = File("src/Day03.txt").readLines()

    val resultList = mutableListOf<Pair<Int, Position>>()
    matrix.forEachIndexed { rowIndex, line ->

        var i: Int = 0
        while (i in line.indices) {
            if (line[i].isDigit()) {
                val (newIndex, num) = line.readNumber(startIndex = i)
                val symbolNear = num.hasSymbolNear(matrix, i, newIndex, rowIndex)
                if (symbolNear != null) {
                    resultList.add(num.toInt() to symbolNear)
                    println("$num - true - ${matrix[symbolNear.row][symbolNear.column]}, row: $rowIndex, index $i  - $newIndex")
                }
                i = newIndex + 1
            } else {
                i += 1
            }
        }
    }

    val result = resultList
        .groupBy({ it.second }, { it.first })
        .filter { it.value.size == 2 }
        .mapValues {
            it.value[0] * it.value[1]
        }.map { it.value }.sum()


    println(result)
//    part1(input).println()
//    part2(input).println()
}

fun String.readNumber(startIndex: Int): Pair<Int, String> {
    val substring = substring(startIndex).takeWhile { it.isDigit() }
    val endIndex = substring.length + startIndex
    return endIndex to substring
}

fun String.hasSymbolNear(matrix: List<String>, startIndex: Int, endIndex: Int, row: Int): Position? {
    val positions = ((startIndex - 1)..endIndex).map {
        val rowAbove = (row - 1)
        val rowBelow = (row + 1).coerceAtMost(matrix.size - 1)

        buildList<Position> {
            if (rowAbove >= 0) {
                val rowLength = matrix[rowAbove].length
                if (it in 0..<rowLength) {
                    add(Position(rowAbove, it))
                }

            }

            if (rowBelow < matrix.size) {
                val rowLength = matrix[rowBelow].length
                if (it in 0..<rowLength) {
                    add(Position(rowBelow, it))
                }
            }
        }
    }.flatten() + buildList {
        val rowSize = matrix[row].length
        if (startIndex != 0) add(Position(row, startIndex - 1))
        if (endIndex < (rowSize - 1)) add(Position(row, endIndex))
    }


    return positions.firstOrNull {
        val c = matrix[it.row][it.column]
        c == '*'
    }
}

data class Position(val row: Int, val column: Int)