package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Day03Test {
    private val solver = Day03()
    private val prefix = "aoc2021/day03"
    private val testData = readLines("$prefix.testinput.txt")
    private val realData = readLines("$prefix.input.txt")

    @Test
    fun testPart1() {
        assertEquals(198, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(230, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
