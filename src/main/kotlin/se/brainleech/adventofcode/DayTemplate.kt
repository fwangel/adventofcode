package se.brainleech.adventofcode

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class DayTemplate {

    fun part1(input: List<String>) : Int {
        return input
            .count()
    }

    fun part2(input: List<String>) : Int {
        return input
            .count()
    }

}

fun main() {
    val solver = DayTemplate()
    val prefix = "aoc<year>/aoc<year>day<day>"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(-1, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(-1, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}