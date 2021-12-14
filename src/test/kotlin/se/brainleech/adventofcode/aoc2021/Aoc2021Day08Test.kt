package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Aoc2021Day08Test {
    private val solver = Aoc2021Day08()
    private val prefix = "aoc2021/aoc2021day08"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(26, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(61_229L, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
