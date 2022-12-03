package se.brainleech.adventofcode.aoc2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Aoc2022Day02Test {
    private val solver = Aoc2022Day02()
    private val prefix = "aoc2022/aoc2022day02"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(15, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(12, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
