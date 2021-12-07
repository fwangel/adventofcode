package se.brainleech.adventofcode.aoc2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

internal class Aoc2020Day02Test {
    private val solver = Aoc2020Day02()
    private val prefix = "aoc2020/aoc2020day02"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun part1() {
        assertEquals(2, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(1, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}