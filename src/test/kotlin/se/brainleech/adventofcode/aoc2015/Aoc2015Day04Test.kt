package se.brainleech.adventofcode.aoc2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

internal class Aoc2015Day04Test {
    private val solver = Aoc2015Day04()
    private val prefix = "aoc2015/aoc2015day04"
    private val testData = readText("$prefix.test.txt")
    private val realData = readText("$prefix.real.txt")

    @Test
    fun part1() {
        assertEquals(1048970, solver.part1(testData, 1048000))
        out(solver.part1(realData, 254000), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(5714438, solver.part2(testData, 5714000))
        out(solver.part2(realData, 1038000), "$prefix.part2 = ")
    }

}