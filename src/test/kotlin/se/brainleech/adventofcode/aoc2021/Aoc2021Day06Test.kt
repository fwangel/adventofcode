package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

class Aoc2021Day06Test {
    private val solver = Aoc2021Day06()
    private val prefix = "aoc2021/aoc2021day06"
    private val testData = readText("$prefix.test.txt")
    private val realData = readText("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(26L, solver.part1(testData, 18))
        assertEquals(59_34L, solver.part1(testData, 80))
        assertEquals(379_114L, solver.part1(realData, 80))
        out(solver.part1(realData, 80), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(26_984_457_539L, solver.part2(testData, 256))
        out(solver.part2(realData, 256), "$prefix.part2 = ")
    }

}
