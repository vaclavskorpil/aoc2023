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
            (start..(start + size))
        }

    val seedToSoilPos = input.indexOfFirst { it.contains("seed-to-soil map:") }
    val soilToFertPos = input.indexOfFirst { it.contains("soil-to-fertilizer map:") }
    val fertToWaterPos = input.indexOfFirst { it.contains("fertilizer-to-water map:") }
    val waterToLightPos = input.indexOfFirst { it.contains("water-to-light map:") }
    val lightToTempPos = input.indexOfFirst { it.contains("light-to-temperature map:") }
    val tempToHum = input.indexOfFirst { it.contains("temperature-to-humidity map:") }
    val humToLocation = input.indexOfFirst { it.contains("humidity-to-location map:") }

    val maps = listOf(
        createMappingBetweenStages(input, seedToSoilPos, soilToFertPos),
        createMappingBetweenStages(input, soilToFertPos, fertToWaterPos),
        createMappingBetweenStages(input, fertToWaterPos, waterToLightPos),
        createMappingBetweenStages(input, waterToLightPos, lightToTempPos),
        createMappingBetweenStages(input, lightToTempPos, tempToHum),
        createMappingBetweenStages(input, tempToHum, humToLocation),
        createMappingBetweenStages(input, humToLocation, input.size),
    )

    val result = seeds.map {
        maps.transformAllGetMin(it)
    }.min()

    return result

}

private fun createMappingBetweenStages(input: List<String>, startIndex: Int, endIndex: Int): Map<LongRange, LongRange> {
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
    return this.fold(listOf(startRange)) { acc, stageRagnes ->
        val newRanges = acc.flatMap { seedRange ->

            val intersectingRanges = stageRagnes.toList().filter { it.first isIntersecting seedRange }

            intersectingRanges.map { (sourceRange, targedRange) ->
                transfromToTargetRangeLenght(sourceRange, seedRange, targedRange)
            }
        }
        newRanges
    }.minOf { it.first }
}

private fun transfromToTargetRangeLenght(
    sourcRange: LongRange,
    seedRange: LongRange,
    targetRange: LongRange
): LongRange {
    return (sourcRange intersectingRange seedRange)?.let { intersection ->

        val startOffset = intersection.first - sourcRange.first
        val size = intersection.last - intersection.first

        val newRangeStart = targetRange.first + startOffset

        newRangeStart..(newRangeStart + size)
    } ?: sourcRange
}

private infix fun LongRange.intersectingRange(range: LongRange): LongRange? {
    return if (isIntersecting(range)) {

        val start = listOf(first, range.first).max()
        val end = listOf(last, range.last).min()

        return start..end
    } else null
}

private infix fun LongRange.isIntersecting(range: LongRange): Boolean {
    val start = listOf(first, range.first).max()
    val end = listOf(last, range.last).min()

    return end > start
}