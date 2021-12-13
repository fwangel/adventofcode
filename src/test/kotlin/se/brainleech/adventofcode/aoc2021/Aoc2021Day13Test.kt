package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

class Aoc2021Day13Test {
    private val solver = Aoc2021Day13()
    private val prefix = "aoc2021/aoc2021day13"
    private val testData = readText("$prefix.test.txt")
    private val realData = readText("$prefix.real.txt")

    companion object {
        private val SQUARE = """
            #####
            #...#
            #...#
            #...#
            #####
            .....
            .....
        """.trimIndent()
    }

    @Test
    fun testPart1() {
        assertEquals(17, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(SQUARE, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = \n")
    }

}
