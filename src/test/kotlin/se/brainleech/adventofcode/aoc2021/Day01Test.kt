package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readIntegers

class Day01Test {
    private val solver = Day01()
    private val prefix = "aoc2021/day01"
    private val testData = readIntegers("$prefix.testinput.txt")
    private val realData = readIntegers("$prefix.input.txt")

    @Test
    fun testPart1() {
        assertEquals(7, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(5, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
