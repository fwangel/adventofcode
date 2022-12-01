package se.brainleech.adventofcode.aoc2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Aoc2022Day01Test {
    private val solver = Aoc2022Day01()
    private val prefix = "aoc2022/aoc2022day01"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(24000L, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(45000L, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
