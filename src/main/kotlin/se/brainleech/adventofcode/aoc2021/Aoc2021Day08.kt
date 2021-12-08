package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.sortedByChar
import java.util.stream.Stream
import kotlin.streams.asSequence

class Aoc2021Day08 {

    companion object {
        // .dddd.
        // e....a
        // e....a
        // .ffff.
        // g....b
        // g....b
        // .cccc.

        private val KNOWN_DIGITS_LENGTHS = mapOf(
            "ab".length to 1,
            "abef".length to 4,
            "abd".length to 7,
            "abcdefg".length to 8
        )

        private val EXPECTED_DIGIT_PATTERNS = mapOf(
            "abcdeg" to 0,
            "ab" to 1,
            "acdfg" to 2,
            "abcdf" to 3,
            "abef" to 4,
            "bcdef" to 5,
            "bcdefg" to 6,
            "abd" to 7,
            "abcdefg" to 8,
            "abcdef" to 9
        )
    }

    data class NotebookEntry(val patterns: List<String>, val digitOutputs: List<String>) {
        init {
            // för varje av de 10 pattern:
            // identifiera de med unikt antal tecken
            // avkoda vilken bokstav som motsvarar det förväntade 8-mönstret
            // mappa om de övriga 6 siffrorna
        }

        fun decode(encoded: String): Char {
            return '0'
        }
    }

    private fun String.asSortedPatterns(): List<String> {
        return this.split(" ").map { it.sortedByChar() }.toList().sorted()
    }

    private fun String.asNotebookEntry(): NotebookEntry {
        val (rawPatterns, rawDigitOutputs) = this.split(" | ")
        return NotebookEntry(rawPatterns.asSortedPatterns(), rawDigitOutputs.asSortedPatterns())
    }

    fun part1(input: Stream<String>): Int {
        // count number of occurrences of 1, 4, 7, and 8
        val numberOf1478Digits = input
            .asSequence()
            .map { it.asNotebookEntry() }
            .map { entry ->
                entry.digitOutputs
                    .map { output -> output.length }
                    .count { len -> KNOWN_DIGITS_LENGTHS.containsKey(len) }
            }.sum()

        return numberOf1478Digits
    }

    fun part2(input: Stream<String>): Long {
        return input
            .asSequence()
            .map { it.asNotebookEntry() }
            .map { entry ->
                entry.digitOutputs
                    .map { encoded -> entry.decode(encoded) }
                    .joinToString("")
                    .toLong()
            }.sum()
    }

}
