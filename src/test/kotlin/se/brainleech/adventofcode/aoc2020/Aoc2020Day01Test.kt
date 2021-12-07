package se.brainleech.adventofcode.aoc2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readIntegers

internal class Aoc2020Day01Test {
    private val solver = Aoc2020Day01()
    private val prefix = "aoc2020/aoc2020day01"
    private val testData = readIntegers("$prefix.test.txt")
    private val realData = readIntegers("$prefix.real.txt")

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