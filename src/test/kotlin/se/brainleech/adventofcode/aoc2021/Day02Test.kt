package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Day02Test {
    private val solver = Day02()
    private val prefix = "aoc2021/day02"
    private val testData = readLines("$prefix.testinput.txt")
    private val realData = readLines("$prefix.input.txt")

    @Test
    fun testPart1() {
        assertEquals(150, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(900, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}