package se.brainleech.adventofcode.aoc2021

import java.util.*
import java.util.stream.Stream
import kotlin.streams.asSequence

class Aoc2021Day10 {

    companion object {
        private val BUDDIES = mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>'
        )

        private val ERROR_SCORES = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
    }

    class SyntaxChecker(val input: CharArray) {
        fun syntaxErrorScore(): Int {
            val expectedChars = LinkedList<Char>()
            for (char in input) {
                if (BUDDIES.containsValue(char)) {
                    if (char != expectedChars.pop()) {
                        return ERROR_SCORES[char]!!
                    }
                } else {
                    expectedChars.push(BUDDIES[char])
                }
            }
            return 0
        }
    }

    fun part1(input: Stream<String>): Long {
        return input
            .asSequence()
            .sumOf {
                SyntaxChecker(it.toCharArray())
                    .syntaxErrorScore()
                    .toLong()
            }
    }

    fun part2(input: Stream<String>): Long {
        return -1
    }

}
