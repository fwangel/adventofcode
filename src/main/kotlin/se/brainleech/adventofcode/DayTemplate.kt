package se.brainleech.adventofcode

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class DayTemplate {

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
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return -1
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return -1
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