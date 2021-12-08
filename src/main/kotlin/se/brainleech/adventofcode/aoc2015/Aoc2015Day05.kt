package se.brainleech.adventofcode.aoc2015

import java.util.stream.Stream
import kotlin.streams.asSequence

class Aoc2015Day05 {
    companion object {
        private val NON_VOWELS = Regex("[^aeiou]")
        private val DOUBLE_CHAR = Regex("([a-z])\\1")
        private val NAUGHTY_COMBINATIONS = Regex("(ab|cd|pq|xy)")

        private val PAIR = Regex("([a-z]{2}).*\\1")
        private val SURROUNDING = Regex("([a-z]).\\1")
    }

    fun part1(input: Stream<String>): Int {
        return input
            .asSequence()
            .filter { !it.contains(NAUGHTY_COMBINATIONS) }
            .filter { it.replace(NON_VOWELS, "").length > 2 }
            .filter { it.contains(DOUBLE_CHAR) }
            .count()
    }

    fun part2(input: Stream<String>): Int {
        return input
            .asSequence()
            .filter { it.contains(PAIR) }
            .filter { it.contains(SURROUNDING) }
            .count()
    }

}
