package se.brainleech.adventofcode.aoc2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readIntegers

internal class Day01Test {
    private val solver = Day01()
    private val prefix = "aoc2020/day01"
    private val testData = readIntegers("$prefix.testinput.txt")
    private val realData = readIntegers("$prefix.input.txt")

    @Test
    fun part1() {
        assertEquals(514579, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(241861950, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}