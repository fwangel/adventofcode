package se.brainleech.adventofcode.aoc2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

internal class Aoc2015Day05Test {
    private val solver = Aoc2015Day05()
    private val prefix = "aoc2015/aoc2015day05"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun part1() {
        assertEquals(3, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(2, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}