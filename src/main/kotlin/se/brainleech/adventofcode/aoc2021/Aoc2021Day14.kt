package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.sortedByChar
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

class Aoc2021Day14 {

    companion object {
        private const val PADDED_ARROW = " -> "
    }

    // 0: NNCB
    // 1: NCNBCHB
    // 2: NBCCNBBBCBHCB
    // 3: NBBBCNCCNBBNBNBBCHBHHBCHB
    // 4: NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB
    // ...
    // NOTES:
    // * The first and last element character is always the same.
    // * For every pair processed, the result is two new pairs
    //   built from the element mapped via the original pair.

    interface PolymerExpander {
        fun process(steps: Int): PolymerExpander
        fun maxMinusMinQuantity(): Long
    }

    open class RealPolymerExpander(
        template: String,
        private val insertionRules: Map<String, String>
    ) : PolymerExpander {
        private var chain = StringBuilder(template)

        override fun process(steps: Int): PolymerExpander {
            // insert the mapped element for every pair on each iteration
            repeat(steps) {
                chain.windowed(2).withIndex()
                    .map { indexedPair ->
                        chain.insert(1.plus(indexedPair.index.times(2)), insertionRules[indexedPair.value])
                    }
            }
            return this
        }

        private fun quantitiesByElement(): SortedMap<String, Long> {
            return chain
                .toString()
                .sortedByChar()
                .groupingBy { it.toString() }
                .eachCount()
                .mapValues { it.value.toLong() }
                .toSortedMap()
        }

        override fun maxMinusMinQuantity(): Long {
            return quantitiesByElement().map { it.value }.sorted().let { quantities ->
                quantities.last().minus(quantities.first())
            }
        }
    }

    class SimulatedPolymerExpander(
        private val template: String,
        private val insertionRules: Map<String, String>
    ) : PolymerExpander {
        private var quantitiesPerPair: Map<String, Long> = mapOf()

        private fun String.firstCharAsString(): String = this.first().toString()
        private fun String.lastCharAsString(): String = this.last().toString()
        private fun Long.half(): Long = this.div(2)
        private val mergeBySum: Long.(Long) -> Long = { other -> this.plus(other) }

        override fun process(steps: Int): PolymerExpander {
            quantitiesPerPair = template
                .windowed(2)
                .groupingBy { it }
                .eachCount()
                .mapValues { entry -> entry.value.toLong() }

            repeat(steps) {
                val updatedQuantities = mutableMapOf<String, Long>()
                quantitiesPerPair.forEach { (pair, pairQuantity) ->
                    val newElement = insertionRules[pair]
                    val newPairBefore = "${pair.firstCharAsString()}${newElement}"
                    val newPairAfter = "${newElement}${pair.lastCharAsString()}"
                    updatedQuantities.merge(newPairBefore, pairQuantity, mergeBySum)
                    updatedQuantities.merge(newPairAfter, pairQuantity, mergeBySum)
                }
                quantitiesPerPair = updatedQuantities.toMap()
            }

            return this
        }

        override fun maxMinusMinQuantity(): Long {
            // the first and last element must be counted
            // separately from the pairs, since elements
            // are inserted inside each pair
            val quantitiesPerElement = mutableMapOf(
                template.firstCharAsString() to 1L,
                template.lastCharAsString() to 1L
            )
            // ...then split each pair and add up the totals
            // (but keep in mind it will be double)
            quantitiesPerPair.forEach { (pair, pairQuantity) ->
                val firstElement = pair.firstCharAsString()
                val secondElement = pair.lastCharAsString()
                quantitiesPerElement.merge(firstElement, pairQuantity, mergeBySum)
                quantitiesPerElement.merge(secondElement, pairQuantity, mergeBySum)
            }

            // adjust the totals by half and determine the MAX and MIN difference
            return quantitiesPerElement.map { it.value.half() }.sorted().let { quantities ->
                quantities.last().minus(quantities.first())
            }
        }

    }

    private fun Stream<String>.asPolymer(simulated: Boolean = false): PolymerExpander {
        val lines = this.toList()
        val template = lines.first()
        val rules = lines
            .asSequence()
            .drop(2)
            .map { line -> line.split(PADDED_ARROW) }
            .groupBy(keySelector = { it.first() }, valueTransform = { it.last() })
            .mapValues { it.value.first() }

        return if (simulated) SimulatedPolymerExpander(template, rules)
        else RealPolymerExpander(template, rules)
    }

    fun part1(input: Stream<String>): Long {
        return input.asPolymer(simulated = false).process(10).maxMinusMinQuantity()
    }

    fun part2(input: Stream<String>): Long {
        return input.asPolymer(simulated = true).process(40).maxMinusMinQuantity()
    }

}
