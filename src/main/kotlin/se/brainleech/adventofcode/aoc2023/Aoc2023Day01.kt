package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.aoc2023.Aoc2023Day01.Companion.toDigits
import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2023Day01 {

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

        private val DIGITS_0_TO_9_AS_WORDS = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        private val DIGITS_1_TO_9_AS_WORDS = listOf(        "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        fun String.toDigits(alsoZero: Boolean = false, allowOverlaps: Boolean = false): String {
            val list = if (alsoZero) DIGITS_0_TO_9_AS_WORDS else DIGITS_1_TO_9_AS_WORDS
            var result = this
            while (true) {
                val indexAndString = result.findAnyOf(list) ?: break
                val word = indexAndString.second
                var digit = DIGITS_0_TO_9_AS_WORDS.indexOf(word).toString()
                if (allowOverlaps) {
                    // allow words to overlap by leaving first and last char intact
                    digit = word.first() + digit + word.last()
                }
                result = result.replace(word, digit)
            }
            return result.filter { it.isDigit() }
        }
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return input
            .filter { it.isNotEmpty() }
            .map { line -> line.filter { it.isDigit() } }
            .sumOf { "${it.first()}${it.last()}".toInt() }
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return input
            .filter { it.isNotEmpty() }
            .map { it.toDigits(allowOverlaps = true) }
            .sumOf { "${it.first()}${it.last()}".toInt() }
    }

}

fun main() {
    val solver = Aoc2023Day01()
    val prefix = "aoc2023/aoc2023day01"
    val testDataP1a = readLines("$prefix.test.p1-a.txt")
    val testDataP2a = readLines("$prefix.test.p2-a.txt")
    val realData = readLines("$prefix.real.txt")

    verify(1256, "oneightwoyzero5six".toDigits(allowOverlaps = false, alsoZero = false).toInt())
    verify(12056, "oneightwoyzero5six".toDigits(allowOverlaps = false, alsoZero = true).toInt())
    verify(182056, "oneightwoyzero5six".toDigits(allowOverlaps = true, alsoZero = true).toInt())

    verify(142, solver.part1(testDataP1a))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(281, solver.part2(testDataP2a))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}