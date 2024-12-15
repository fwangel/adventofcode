package se.brainleech.adventofcode.aoc2024

import se.brainleech.adventofcode.aoc2024.Aoc2024Day01.Companion.debug
import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.toListOfLongs
import se.brainleech.adventofcode.verify
import kotlin.math.abs

class Aoc2024Day01 {

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

    private fun List<String>.parseAsColumnsOfLongs(): List<List<Long>> {
        return this
            .filter { it.isNotEmpty() }
            .flatMap { it.toListOfLongs(" ", 2) }
            .withIndex()
            .partition { (it.index % 2) == 0 }
            .toList()
            .map { indexed -> indexed.map { it.value }.sorted() }
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        // plan:
        // add two numbers per line to big list,
        // partition by odd/even indexes,
        // sort each list
        // zip the lists using the diff
        // sum the diffs
        val (first, second) = input.parseAsColumnsOfLongs()
        return first
            .zip(second) { a, b -> abs(a - b) }
            .sum()
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        // plan:
        // add two numbers per line to big list,
        // partition by odd/even indexes,
        // sort each list
        // second list to frequency map
        // sum the first list using occurrences multiplier
        val (first, second) = input.parseAsColumnsOfLongs()
        val appearances = second.groupingBy { it }.eachCount()
        return first
            .sumOf { it.times(appearances.getOrDefault(it, 0)) }
    }

}

fun main() {
    val solver = Aoc2024Day01()
    val prefix = "aoc2024/aoc2024day01"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(11L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(31L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}