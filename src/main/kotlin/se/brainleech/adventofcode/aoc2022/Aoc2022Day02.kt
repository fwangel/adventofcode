package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day02 {

    companion object {
        private const val ROCK = "A"
        private const val PAPER = "B"
        private const val SCISSORS = "C"
        private const val LOSE = "X"
        private const val DRAW = "Y"
        private const val WIN = "Z"
    }

    private fun String.score(): Int {
        val (opponent, you_encoded) = this.split(" ")
        val baseScore = (you_encoded[0] - 'X') + 1
        val you = ('A' + (you_encoded[0] - 'X')).toString()
        return baseScore +
                 if ((opponent == ROCK && you == PAPER) || (opponent == PAPER && you == SCISSORS) || (opponent == SCISSORS && you == ROCK)) 6
            else if ((opponent == ROCK && you == ROCK) || (opponent == PAPER && you == PAPER) || (opponent == SCISSORS && you == SCISSORS)) 3
            else if ((opponent == ROCK && you == SCISSORS) || (opponent == PAPER && you == ROCK) || (opponent == SCISSORS && you == PAPER)) 0
            else Int.MIN_VALUE
    }

    private fun String.scoreByExpectedOutcome(): Int {
        val (opponent, you) = this.split(" ")
        val move =
                 if ((opponent == ROCK && you == WIN) || (opponent == PAPER && you == DRAW) || (opponent == SCISSORS && you == LOSE)) PAPER
            else if ((opponent == PAPER && you == WIN) || (opponent == SCISSORS && you == DRAW) || (opponent == ROCK && you == LOSE)) SCISSORS
            else if ((opponent == SCISSORS && you == WIN) || (opponent == ROCK && you == DRAW) || (opponent == PAPER && you == LOSE)) ROCK
            else "?"
        val encoded = ('X' + (move[0] - 'A')).toString()
        return ("$opponent $encoded").score()
    }

    fun part1(input: List<String>): Int {
        return input
            .map { it.score() }
            .reduce { total, score -> total.plus(score) }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { it.scoreByExpectedOutcome() }
            .reduce { total, score -> total.plus(score) }
    }

}

fun main() {
    val solver = Aoc2022Day02()
    val prefix = "aoc2022/aoc2022day02"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(15, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(12, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}