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

        private val MISSING_SCORES = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4
        )
    }

    class SyntaxChecker(private val line: String) {
        fun syntaxErrorScore(): Long {
            val expectedChars = LinkedList<Char>()
            for (char in line) {
                if (BUDDIES.containsValue(char)) {
                    if (char != expectedChars.pop()) {
                        return ERROR_SCORES[char]!!.toLong()
                    }
                } else {
                    expectedChars.push(BUDDIES[char])
                }
            }
            return 0L
        }

        fun missingCharsScore(): Long {
            val expectedChars = LinkedList<Char>()
            for (char in line) {
                if (BUDDIES.containsValue(char)) {
                    expectedChars.pop()
                } else {
                    expectedChars.push(BUDDIES[char])
                }
            }
            return expectedChars
                .map { char -> MISSING_SCORES[char]!!.toLong() }
                .reduce { total, score -> total.times(5).plus(score) }
        }
    }

    fun part1(input: Stream<String>): Long {
        return input
            .asSequence()
            .sumOf { line -> SyntaxChecker(line).syntaxErrorScore() }
    }

    fun part2(input: Stream<String>): Long {
        val missingScores = input
            .asSequence()
            .map { line -> SyntaxChecker(line) }
            .filter { checker -> checker.syntaxErrorScore() == 0L }
            .map { checker -> checker.missingCharsScore() }
            .sorted()
            .toList()

        return missingScores.drop(missingScores.size / 2).first()
    }

}
