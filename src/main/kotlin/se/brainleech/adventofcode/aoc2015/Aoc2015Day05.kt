package se.brainleech.adventofcode.aoc2015

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2015Day05 {
    companion object {
        private val NON_VOWELS = Regex("[^aeiou]")
        private val DOUBLE_CHAR = Regex("([a-z])\\1")
        private val NAUGHTY_COMBINATIONS = Regex("(ab|cd|pq|xy)")

        private val PAIR = Regex("([a-z]{2}).*\\1")
        private val SURROUNDING = Regex("([a-z]).\\1")
    }

    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .filter { !it.contains(NAUGHTY_COMBINATIONS) }
            .filter { it.replace(NON_VOWELS, "").length > 2 }
            .filter { it.contains(DOUBLE_CHAR) }
            .count()
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .filter { it.contains(PAIR) }
            .filter { it.contains(SURROUNDING) }
            .count()
    }

}

fun main() {
    val solver = Aoc2015Day05()
    val prefix = "aoc2015/aoc2015day05"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(3, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(2, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}
