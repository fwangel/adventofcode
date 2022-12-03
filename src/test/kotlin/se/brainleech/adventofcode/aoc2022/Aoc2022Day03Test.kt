package se.brainleech.adventofcode.aoc2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Aoc2022Day03Test {
    private val solver = Aoc2022Day03()
    private val prefix = "aoc2022/aoc2022day03"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(157, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(70, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
