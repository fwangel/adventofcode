package se.brainleech.adventofcode.aoc2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

internal class Aoc2020Day04Test {
    private val solver = Aoc2020Day04()
    private val prefix = "aoc2020/aoc2020day04"
    private val testData = readText("$prefix.test.txt")
    private val realData = readText("$prefix.real.txt")

    @Test
    fun part1() {
        assertEquals(2L, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(-1L, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}