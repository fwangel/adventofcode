package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.toListOfInts

internal class Day07Test {
    private val solver = Day07()
    private val prefix = "aoc2021/day07"
    private val testData = readText("$prefix.testinput.txt").toListOfInts()
    private val realData = readText("$prefix.input.txt").toListOfInts()

    @Test
    fun part1() {
        assertEquals(37, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun part2() {
        assertEquals(168, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}