package se.brainleech.adventofcode.aoc2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

internal class Day01Test {
    private val solver = Day01()
    private val prefix = "aoc2015/day01"
    private val testData = readText("$prefix.testinput.txt")
    private val realData = readText("$prefix.input.txt")

    @Test
    fun part1() {
        assertEquals(-1, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(5, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}