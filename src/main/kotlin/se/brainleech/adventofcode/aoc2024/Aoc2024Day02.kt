package se.brainleech.adventofcode.aoc2024

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.toListOfLongs
import se.brainleech.adventofcode.verify

class Aoc2024Day02 {

    companion object {
        const val DEBUG = true
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

    private fun List<Long>.isSafe(withTolerance: Boolean = false): Boolean {
        // plan:
        // use window to get two values at a time
        // determine distance (positive / negative)
        // check if all dists are either positive or negative
        //  and within expectation
        // if tolerance is requested, repeat while removing
        //   one value each time from a different index
        val limit = if (withTolerance) this.size else 0
        for (indexToRemove in -1..< limit) {
            val diffs = this
                .filterIndexed { index, _ -> index != indexToRemove }
                .windowed(size = 2, step = 1)
                .map { if (it.count() > 1) it.last() - it.first() else 0 }
            val safe = diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
            if (safe) return true
        }
        return false
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .map { it.toListOfLongs(" ") }
            .count { it.isSafe() }
            .toLong()
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .map { it.toListOfLongs(" ") }
            .count { it.isSafe(withTolerance = true) }
            .toLong()
    }

}

fun main() {
    val solver = Aoc2024Day02()
    val prefix = "aoc2024/aoc2024day02"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(2L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(4L, solver.part2(listOf(
        "1 2 3 4 5",
        "5 2 3 4 5",
        "1 1 1 2 3", // false
        "1 2 3 4 4",
        "1 2 9 4 5")))
    verify(4L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}