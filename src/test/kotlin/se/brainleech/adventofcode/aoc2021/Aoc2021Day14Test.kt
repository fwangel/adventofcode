package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Aoc2021Day14Test {
    private val solver = Aoc2021Day14()
    private val prefix = "aoc2021/aoc2021day14"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(1588L, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(2_188_189_693_529L, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
