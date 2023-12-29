package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import kotlin.math.sign

class Aoc2023Day07 {

    companion object {
        const val DEBUG = false
        const val TRACE = false

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }
    }

    private data class Card(val symbol: Char) {
        private fun toValue(): Int {
            return when (symbol) {
                in '2'..'9' -> symbol.digitToInt()
                'T' -> 10
                'J' -> 11
                'Q' -> 12
                'K' -> 13
                'A' -> 14
                '*' -> 0 // joker
                else -> throw IllegalStateException("Invalid value: $this")
            }
        }

        val value = toValue()

        override fun toString(): String {
            return "$symbol"
        }
    }

    private enum class HandType(val strength: Int) {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1)
    }

    private data class HandAndBid(val cards: List<Card>, val bid: Int, val withJokers: Boolean = false) : Comparable<HandAndBid> {
        private fun toHandType(): HandType {
            if (withJokers && cards.contains(Card('J'))) {
                val countByCard = cards
                    .filter { it.symbol != 'J' }
                    .sortedByDescending { it.value }
                    .groupingBy { it }
                    .eachCount()
                val bestCard = countByCard
                    .maxByOrNull { it.value }
                    ?.key
                    ?: Card('A')
                val newCards = cards.map { if (it.symbol == 'J') bestCard else it }.toList()
                val newHand = HandAndBid(newCards, bid)
                return newHand.handType
            }

            val counts = cards.groupingBy { it }.eachCount().values.sortedDescending()
            if (counts[0] == 5) return HandType.FIVE_OF_A_KIND
            if (counts[0] == 4) return HandType.FOUR_OF_A_KIND
            if (counts[0] == 3 && counts[1] == 2) return HandType.FULL_HOUSE
            if (counts[0] == 3 && counts[1] == 1) return HandType.THREE_OF_A_KIND
            if (counts[0] == 2 && counts[1] == 2) return HandType.TWO_PAIR
            if (counts[0] == 2 && counts[1] == 1) return HandType.ONE_PAIR
            return HandType.HIGH_CARD
        }

        private fun toStrength(): Int {
            return handType.strength
        }

        val handType: HandType = toHandType()
        val strength: Int = toStrength()

        override operator fun compareTo(other: HandAndBid): Int {
            var result = (strength - other.strength).sign
            if (result == 0) {
                result = cards
                    .map { if (withJokers && it.symbol == 'J') Card('*') else it }
                    .zip(other.cards.map { if (withJokers && it.symbol == 'J') Card('*') else it })
                    .map { pair -> (pair.first.value - pair.second.value).sign }
                    .dropWhile { it == 0 }
                    .firstOrNull()
                    ?: 0
            }
            return result
        }

        override fun toString(): String {
            return "\n[Hand: bid=$bid, type=$handType, strength=$strength, cards=$cards]"
        }
    }

    private fun String.toHandAndBid(withJokers: Boolean = false): HandAndBid {
        val parts = this.split(" ")
        return HandAndBid(parts[0].map { Card(it) }.toList(), parts[1].toInt(), withJokers)
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toHandAndBid() }
            .sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toHandAndBid(true) }
            .sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

}

fun main() {
    val solver = Aoc2023Day07()
    val prefix = "aoc2023/aoc2023day07"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(6440, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(5905, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}