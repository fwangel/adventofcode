package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

class Aoc2021Day17Test {
    private val solver = Aoc2021Day17()
    private val prefix = "aoc2021/aoc2021day17"
    private val testData = readText("$prefix.test.txt")
    private val realData = readText("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(45, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(112, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
