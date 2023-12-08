import java.io.File

val cardsRank = arrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

data class Card(val cards: String, val bid: Int) {

    val letterCount: List<Pair<Char, Int>> = createLetterCountList()

    fun createLetterCountList(): List<Pair<Char, Int>> {
        val map = buildMap<Char, Int> {
            cards.forEach { char ->
                val current = this.getOrDefault(char, 0)
                this[char] = current + 1
            }
        }

        val sortedList = map.toList()
            .sortedWith(compareByDescending<Pair<Char, Int>> { it.second })

        return when {
            sortedList.any { it.first == 'J' }.not() -> sortedList
            else -> {
                val (j, rest) = sortedList.partition { it.first == 'J' }
                val first = rest.firstOrNull() ?: ('J' to 0)
                listOf(first.first to first.second + j.first().second) + rest.drop(1)
            }
        }
    }

}

fun main() {

    val comparator = object: Comparator<Card> {
        override fun compare(th: Card, other: Card): Int {
            println("Comparing $th to $other")

            with(th) {
                val distinctSize = other.letterCount.size - letterCount.size
                if (distinctSize != 0) return distinctSize

                val firstCardGroupDifference = letterCount.first().second - other.letterCount.first().second
                if (firstCardGroupDifference != 0) return firstCardGroupDifference

                cards.forEachIndexed { index, char ->
                    val cardValue = cardsRank.indexOf(other.cards[index]) - cardsRank.indexOf(char)
                    if (cardValue != 0) {
                        return cardValue
                    }
                }

                return 0
            }

        }
    }

    val f = File("src/Day07.txt").readLines().map {
        val (cards, bid) = it.split(" ")
        Card(cards, bid.toInt())
    }.sortedWith(comparator)
        .mapIndexed { index, card ->
            println(card.cards)
            println("${card.bid} * ${index + 1}")
            card.bid * (index + 1)
        }.sum()
    println(f)

}
