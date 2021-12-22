package se.brainleech.adventofcode.aoc2021

import java.util.stream.Stream
import kotlin.streams.toList

class Aoc2021Day18StringBuilder {

    companion object {
        private val REGULAR_NUMBER_PAIR = Regex("\\[(\\d+),(\\d+)]")
    }

    class SnailfishNumber(input: String) {
        val value = StringBuilder(input)

        fun plus(other: SnailfishNumber): SnailfishNumber {
            value.insert(0, '[')
            value.append(',')
            value.append(other.value)
            value.append(']')
            return this.reduce()
        }

        fun magnitude(): Int {
            var minimized = value.toString()
            do {
                minimized = REGULAR_NUMBER_PAIR.replace(minimized) {
                    3.times(it.groupValues[1].toInt())
                        .plus(2.times(it.groupValues[2].toInt()))
                        .toString()
                }
            } while (minimized.contains('['))
            return minimized.toInt()
        }

        fun reduce(
            maxSteps: Int = Int.MAX_VALUE,
            allowExplode: Boolean = true,
            allowSplit: Boolean = true
        ): SnailfishNumber {
            val context = this
            var stepsCompleted = 0
            do {
                var actionPerformed = false
                if (allowExplode) findIndexOfFirstExplodablePair()?.apply {
                    explodeRegularNumberPairAt(this)
                    actionPerformed = true
                    if (++stepsCompleted >= maxSteps) return context
                }

                if (allowSplit && !actionPerformed) findIndexOfFirstSplittableNumber()?.apply {
                    splitRegularNumberAt(this)
                    actionPerformed = true
                    if (++stepsCompleted >= maxSteps) return context
                }

            } while (actionPerformed)

            return this
        }

        private fun findIndexOfFirstExplodablePair(): IntRange? {
            var depth = 0
            var index = 0
            while (index < value.length) {
                when (value[index]) {
                    '[' -> depth++
                    ']' -> depth--
                    in '0'..'9' -> {
                        if (depth > 4) {
                            var subIndex = index
                            while (subIndex < value.length && value[subIndex] == ',' || value[subIndex] in '0'..'9') {
                                subIndex++
                            }
                            if (value[subIndex] == ']') {
                                return index - 1..subIndex
                            }
                        }
                    }
                }
                index++
            }
            return null
        }

        private fun findIndexOfFirstRegularNumberToTheLeftOf(startIndex: Int): IntRange? {
            var index = startIndex
            while (index >= 0) {
                when (value[index]) {
                    in '0'..'9' -> {
                        var subIndex = index
                        while (subIndex >= 0 && value[subIndex] in '0'..'9') {
                            subIndex--
                        }
                        return subIndex + 1..index
                    }
                }
                index--
            }
            return null
        }

        private fun findIndexOfFirstRegularNumberToTheRightOf(startIndex: Int): IntRange? {
            var index = startIndex
            while (index < value.length) {
                when (value[index]) {
                    in '0'..'9' -> {
                        var subIndex = index
                        while (subIndex < value.length && value[subIndex] in '0'..'9') {
                            subIndex++
                        }
                        return index until subIndex
                    }
                }
                index++
            }
            return null
        }

        private fun explodeRegularNumberPairAt(pairRange: IntRange) {
            val (leftValue, rightValue) = value.substring(pairRange).trim('[', ']').split(',').map { it.toInt() }

            // "the pair's left value is added to the first regular number to the left of the exploding pair (if any)"
            findIndexOfFirstRegularNumberToTheRightOf(pairRange.last)?.apply {
                value.replaceRangeInline(this, value.substring(this).toInt().plus(rightValue).toString())
            }

            // "the entire exploding pair is replaced with the regular number 0."
            value.replaceRangeInline(pairRange, "0")

            // "the pair's right value is added to the first regular number to the right of the exploding pair (if any)"
            findIndexOfFirstRegularNumberToTheLeftOf(pairRange.first - 1)?.apply {
                value.replaceRangeInline(this, value.substring(this).toInt().plus(leftValue).toString())
            }
        }

        private fun findIndexOfFirstSplittableNumber(): IntRange? {
            var index = 0
            while (index < value.length) {
                when (value[index]) {
                    in '0'..'9' -> {
                        var subIndex = index
                        while (subIndex < value.length && value[subIndex] in '0'..'9') {
                            subIndex++
                        }
                        if (subIndex.minus(index) > 1) {
                            // any number with more than one digit is a match
                            return index until subIndex
                        }
                    }
                }
                index++
            }
            return null
        }

        private fun splitRegularNumberAt(range: IntRange) {
            val regularNumber = value.substring(range).toInt()
            val halfDown = regularNumber.div(2)
            val halfUp = regularNumber.plus(1).div(2)
            value.replaceRangeInline(range, "[$halfDown,$halfUp]")
        }

        private fun StringBuilder.replaceRangeInline(range: IntRange, replacement: String): StringBuilder {
            this.deleteRange(range.first, range.last + 1)
            this.insert(range.first, replacement)
            return this
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    fun testExplodeOnce(a: String): String {
        return a.asSnailfishNumber().reduce(maxSteps = 1, allowSplit = false).toString()
    }

    fun testSplitOnce(a: String): String {
        return a.asSnailfishNumber().reduce(maxSteps = 1, allowExplode = false).toString()
    }

    fun testReduce(a: String): String {
        return a.asSnailfishNumber().reduce().toString()
    }

    fun testPlus(a: String, b: String): String {
        return a.asSnailfishNumber().plus(b.asSnailfishNumber()).toString()
    }

    private fun String.asSnailfishNumber(): SnailfishNumber {
        return SnailfishNumber(this)
    }

    fun part1(input: Stream<String>): Int {
        return input.toList()
            .map { it.asSnailfishNumber() }
            .reduce(SnailfishNumber::plus)
            .magnitude()
    }

    fun part2(input: Stream<String>): Int {
        val list = input.toList()
        return list.indices.flatMap { i -> list.indices.map { j -> i to j } }
            .filter { (i, j) -> i != j }
            .maxOf { (i, j) ->
                val first = list[i].asSnailfishNumber()
                val second = list[j].asSnailfishNumber()
                first.plus(second).magnitude()
            }
    }

}
