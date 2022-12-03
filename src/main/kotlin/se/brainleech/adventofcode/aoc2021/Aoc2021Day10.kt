package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import java.util.*

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
            '}' to 1_197,
            '>' to 25_137
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

    fun part1(input: List<String>): Long {
        return input
            .sumOf { line -> SyntaxChecker(line).syntaxErrorScore() }
    }

    fun part2(input: List<String>): Long {
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

fun main() {
    val solver = Aoc2021Day10()
    val prefix = "aoc2021/aoc2021day10"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(26_397L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(288_957L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}