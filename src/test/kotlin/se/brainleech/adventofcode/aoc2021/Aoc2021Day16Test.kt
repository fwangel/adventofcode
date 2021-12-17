package se.brainleech.adventofcode.aoc2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import se.brainleech.adventofcode.out
import se.brainleech.adventofcode.readText

class Aoc2021Day16Test {
    private val solver = Aoc2021Day16()
    private val prefix = "aoc2021/aoc2021day16"
    private val testData = readText("$prefix.test.txt")
    private val realData = readText("$prefix.real.txt")

    @Test
    fun testPart1() {
        assertEquals(6L, solver.part1("D2FE28"))
        assertEquals(9L, solver.part1("38006F45291200"))
        assertEquals(14L, solver.part1("EE00D40C823060"))
        assertEquals(16L, solver.part1("8A004A801A8002F478"))
        assertEquals(12L, solver.part1("620080001611562C8802118E34"))
        assertEquals(23L, solver.part1("C0015000016115A2E0802F182340"))
        assertEquals(31L, solver.part1(testData))
        out(solver.part1(realData), "$prefix.part1 = ")
    }

    @Test
    fun testPart2() {
        assertEquals(54.toBigInteger(), solver.part2(testData))
        assertEquals(3.toBigInteger(), solver.part2("C200B40A82"))
        assertEquals(54.toBigInteger(), solver.part2("04005AC33890"))
        assertEquals(7.toBigInteger(), solver.part2("880086C3E88112"))
        assertEquals(9.toBigInteger(), solver.part2("CE00C43D881120"))
        assertEquals(1.toBigInteger(), solver.part2("D8005AC2A8F0"))
        assertEquals(0.toBigInteger(), solver.part2("F600BC2D8F"))
        assertEquals(0.toBigInteger(), solver.part2("9C005AC2F8F0"))
        assertEquals(1.toBigInteger(), solver.part2("9C0141080250320F1802104A08"))
        assertEquals(54.toBigInteger(), solver.part2(testData))
        out(solver.part2(realData), "$prefix.part2 = ")
    }

}
