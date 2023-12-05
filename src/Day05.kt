import java.io.File

fun main() {

    val result = part1()
    println(result)
}

private fun part1(): Long {
    val input = File("src/Day05.txt").readLines()

    val seeds = input.take(1).first().substringAfter(':').trim().split(" ")
        .filter { it.isNotEmpty() }
        .map { it.trim().toLong() }
        .chunked(2)
        .map {
            val (start, size) = it
            println("Creating chunk")
            (start..(start + size))
        }

    println("got seeds ")

    val seedToSoilPos = input.indexOfFirst { it.contains("seed-to-soil map:") }
    val soilToFertPos = input.indexOfFirst { it.contains("soil-to-fertilizer map:") }
    val fertToWaterPos = input.indexOfFirst { it.contains("fertilizer-to-water map:") }
    val waterToLightPos = input.indexOfFirst { it.contains("water-to-light map:") }
    val lightToTempPos = input.indexOfFirst { it.contains("light-to-temperature map:") }
    val tempToHum = input.indexOfFirst { it.contains("temperature-to-humidity map:") }
    val humToLocation = input.indexOfFirst { it.contains("humidity-to-location map:") }

    val maps = listOf(
        createMap(input, seedToSoilPos, soilToFertPos),
        createMap(input, soilToFertPos, fertToWaterPos),
        createMap(input, fertToWaterPos, waterToLightPos),
        createMap(input, waterToLightPos, lightToTempPos),
        createMap(input, lightToTempPos, tempToHum),
        createMap(input, tempToHum, humToLocation),
        createMap(input, humToLocation, input.size),
    )

    maps.forEachIndexed { index, map ->
        println("Map $index\n $map")
    }

    println("-------")
    val result = seeds.map {
        "transforming $it"
        maps.transformAllGetMin(it)
    }.min()

    return result

}

private fun createMap(input: List<String>, startIndex: Int, endIndex: Int): Map<LongRange, LongRange> {
    val block = input.subList(startIndex + 1, endIndex)

    return block
        .filter { it.isNotEmpty() || it.firstOrNull()?.isLetter()?.not() ?: false }
        .map {
            val (destRangStart, sourceRangeStart, size) = it.split(" ").filter { it.isNotEmpty() }
                .map { it.trim().toLong() }
            val sourceRange = sourceRangeStart..sourceRangeStart + size
            val destRange = destRangStart..destRangStart + size

            sourceRange to destRange
        }.toMap()
}

private fun List<Map<LongRange, LongRange>>.transformAllGetMin(startRange: LongRange): Long {

    return this.foldIndexed(listOf(startRange)) { index, acc, stageRagnes ->
//        println("seed $acc, step: $index")

        val newRanges = acc.flatMap { seedRange ->
            val intersectingRanges = stageRagnes.toList().filter { it.first isIntersecting seedRange }

            intersectingRanges.map { (sourceRange, targedRange) ->
                transfromToTargetRangeLenght(sourceRange, seedRange, targedRange)
            }
        }

        newRanges
    }.minOf { it.first }
}

private infix fun LongRange.isIntersecting(range: LongRange): Boolean {
    val start = listOf(first, range.first).max()
    val end = listOf(last, range.last).min()

    return end > start
}

private infix fun LongRange.intersectingRange(range: LongRange): LongRange {
    val start = listOf(first, range.first).max()
    val end = listOf(last, range.last).min()

    return start..end
}

private fun transfromToTargetRangeLenght(
    sourcRange: LongRange,
    seedRange: LongRange,
    targetRange: LongRange
): LongRange {
    val intersection = sourcRange intersectingRange seedRange

    val startOffset = sourcRange.first - intersection.first
    val size = intersection.last - intersection.first

    val newRangeStart = targetRange.first + startOffset

    return newRangeStart..(newRangeStart + size)
}

