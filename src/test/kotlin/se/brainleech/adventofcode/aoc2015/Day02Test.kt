package se.brainleech.adventofcode.aoc2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

internal class Day02Test {
    private val solver = Day02()
    private val prefix = "aoc2015/day02"
    private val testData = readText("$prefix.testinput.txt")
    private val realData = readText("$prefix.input.txt")

    @Test
    fun part1() {
        assertEquals(101L, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(48L, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}