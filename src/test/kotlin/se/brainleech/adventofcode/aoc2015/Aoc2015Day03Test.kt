package se.brainleech.adventofcode.aoc2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

internal class Aoc2015Day03Test {
    private val solver = Aoc2015Day03()
    private val prefix = "aoc2015/aoc2015day03"
    private val testData = readText("$prefix.test.txt")
    private val realData = readText("$prefix.real.txt")

    @Test
    fun part1() {
        assertEquals(4, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(3, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}