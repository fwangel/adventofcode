package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readLines

class Aoc2021Day18Test {
    private val solver = Aoc2021Day18()
    private val prefix = "aoc2021/aoc2021day18"
    private val testData = readLines("$prefix.test.txt")
    private val realData = readLines("$prefix.real.txt")

    @Test
    fun testLogic01() {
        assertEquals("[[[[0,9],2],3],4]", solver.testExplodeOnce("[[[[[9,8],1],2],3],4]"))
    }

    @Test
    fun testLogic02() {
        assertEquals("[7,[6,[5,[7,0]]]]", solver.testExplodeOnce("[7,[6,[5,[4,[3,2]]]]]"))
    }

    @Test
    fun testLogic03() {
        assertEquals("[[6,[5,[7,0]]],3]", solver.testExplodeOnce("[[6,[5,[4,[3,2]]]],1]"))
    }

    @Test
    fun testLogic04() {
        assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", solver.testExplodeOnce("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"))
    }

    @Test
    fun testLogic05() {
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", solver.testExplodeOnce("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"))
    }

    @Test
    fun testLogic06() {
        assertEquals("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", solver.testSplitOnce("[[[[0,7],4],[15,[0,13]]],[1,1]]"))
    }

    @Test
    fun testLogic07() {
        assertEquals("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", solver.testSplitOnce("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"))
    }

    @Test
    fun testLogic08() {
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", solver.testExplodeOnce("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"))
    }

    @Test
    fun testLogic09() {
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", solver.testReduce("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"))
    }

    @Test
    fun testLogic10() {
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", solver.testPlus("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]"))
    }

    @Test
    fun testPart1() {
        assertEquals(4140, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(3993, solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
