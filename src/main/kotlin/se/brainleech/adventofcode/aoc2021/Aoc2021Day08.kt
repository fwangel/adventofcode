package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.sortedByChar
import java.util.stream.Stream
import kotlin.streams.asSequence

class Aoc2021Day08 {

    companion object {
        //    @@@@     ....     @@@@     @@@@     ....     @@@@     @@@@     @@@@     @@@@     @@@@
        //   @    @   .    @   .    @   .    @   @    @   @    .   @    .   .    @   @    @   @    @
        //   @    @   .    @   .    @   .    @   @    @   @    .   @    .   .    @   @    @   @    @
        //    ....     ....     @@@@     @@@@     @@@@     @@@@     @@@@     ....     @@@@     @@@@
        //   @    @   .    @   @    .   .    @   .    @   .    @   @    @   .    @   @    @   .    @
        //   @    @   .    @   @    .   .    @   .    @   .    @   @    @   .    @   @    @   .    @
        //    @@@@     ....     @@@@     @@@@     ....     @@@@     @@@@     ....     @@@@     @@@@

        private val KNOWN_DIGITS_BY_LENGTHS = mapOf(
            "ab".length to 1,
            "abef".length to 4,
            "abd".length to 7,
            "abcdefg".length to 8
        )

        private val KNOWN_LENGTHS_BY_DIGIT = KNOWN_DIGITS_BY_LENGTHS
            .entries.associateBy({ it.value }) { it.key }

    }

    data class NotebookEntry(
        val patterns: List<String>,
        val digitOutputs: List<String>
    ) {
        // true for strings that contain all the specified digits, possibly having other characters in between
        private fun String.containsAll(digits: String): Boolean {
            return this.contains(Regex(digits.toCharArray().joinToString(".*")))
        }

        fun decoderByPattern(): Map<String?, Char> {
            // determine the pattern for each digit, using known properties of
            // digits 1, 4, 7, and 8 plus the expected forms of the other digits

            // use known unique lengths to determine 1, 4, 7, and 8
            val decoder = mutableMapOf(
                1 to patterns.find { it.length == KNOWN_LENGTHS_BY_DIGIT[1] },
                4 to patterns.find { it.length == KNOWN_LENGTHS_BY_DIGIT[4] },
                7 to patterns.find { it.length == KNOWN_LENGTHS_BY_DIGIT[7] },
                8 to patterns.find { it.length == KNOWN_LENGTHS_BY_DIGIT[8] }
            )

            // identify the groups for (0, 6, 9) and (2, 3, 5)
            val digit0or6or9 = patterns.filter { it.length == KNOWN_LENGTHS_BY_DIGIT[8]!!.minus(1) }
            val digit2or3or5 = patterns.filter { it.length == KNOWN_LENGTHS_BY_DIGIT[4]!!.plus(1) }

            // digit 3 must be the one that contains the same characters as digit 7
            decoder[3] = digit2or3or5.find { it.containsAll(decoder[7]!!) }

            // digit 9 must be the one that contains the same characters as digits 3 and 4
            decoder[9] = digit0or6or9.find { it.containsAll(decoder[3]!!) && it.containsAll(decoder[4]!!) }

            // remove the characters of digit 1 from digit 4 to match against 5 and 6
            val digits5or6 = decoder[4]!!.filterNot { decoder[1]!!.contains(it) }
            decoder[5] = digit2or3or5.find { it.containsAll(digits5or6) && it != decoder[3] }
            decoder[6] = digit0or6or9.find { it.containsAll(digits5or6) && it != decoder[9] }

            // remaining digits are 0 and 2
            decoder[0] = digit0or6or9.find { it != decoder[6] && it != decoder[9] }
            decoder[2] = digit2or3or5.find { it != decoder[3] && it != decoder[5] }

            return decoder.entries.associateBy({ it.value }) { it.key.digitToChar() }
        }
    }

    private fun String.asCharSortedPatterns(): List<String> {
        return this.split(" ").map { it.sortedByChar() }.toList()
    }

    private fun String.asNotebookEntry(): NotebookEntry {
        val (rawPatterns, rawDigitOutputs) = this.split(" | ")
        return NotebookEntry(rawPatterns.asCharSortedPatterns(), rawDigitOutputs.asCharSortedPatterns())
    }

    fun part1(input: Stream<String>): Int {
        // count number of occurrences of 1, 4, 7, and 8
        return input
            .asSequence()
            .map { line ->
                line.asNotebookEntry().digitOutputs
                    .map { output -> output.length }
                    .count { len -> KNOWN_DIGITS_BY_LENGTHS.containsKey(len) }
            }.sum()
    }

    fun part2(input: Stream<String>): Long {
        return input
            .asSequence()
            .map { it.asNotebookEntry() }
            .map { entry ->
                val decoderByPattern = entry.decoderByPattern()
                entry.digitOutputs
                    .map { encoded -> decoderByPattern.getOrDefault(encoded, '0') }
                    .joinToString("")
                    .toLong()
            }.sum()
    }

}
