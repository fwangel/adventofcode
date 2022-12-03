package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.verify
import java.math.BigInteger

class Aoc2021Day16 {

    companion object {
        private const val HEXADECIMAL_BASE = 16
        private const val BINARY_BASE = 2
        private const val SINGLE_BIT = 1
        private const val BITS_PER_NYBBLE = 4
        private const val BITS_PER_BYTE = 8
        private const val TYPE_ID_SUM = 0
        private const val TYPE_ID_MUL = 1
        private const val TYPE_ID_MIN = 2
        private const val TYPE_ID_MAX = 3
        private const val TYPE_ID_LITERAL = 4
        private const val TYPE_ID_GT = 5
        private const val TYPE_ID_LT = 6
        private const val TYPE_ID_EQ = 7

        private const val HEADER_VERSION_BITS = 3
        private const val HEADER_TYPE_ID_BITS = 3
    }

    private fun StringBuilder.takeAndDrop(n: Int): String {
        return this.take(n).toString().also { this.deleteRange(0, n) }
    }

    private fun String.toBits(): String {
        return this.windowed(size = 2, step = 2).joinToString(separator = "") {
            it.toInt(HEXADECIMAL_BASE).toString(BINARY_BASE).padToBytes()
        }
    }

    private fun String.padToBytes(): String {
        val extraBits = (this.length % BITS_PER_BYTE)
        if (extraBits == 0) return this
        val expectedLength = this.length + (BITS_PER_BYTE - extraBits)
        return this.padStart(expectedLength, '0')
    }

    private fun String.toPacket(): Packet {
        return StringBuilder(this.toBits()).consumeAndParseBitsAsPacket()
    }

    private fun StringBuilder.consumeAndParseBitsAsPacket(): Packet {
        val bits = this
        val bitsAvailableBefore = bits.length
        val version = bits.takeAndDrop(HEADER_VERSION_BITS).toInt(BINARY_BASE)
        val typeId = bits.takeAndDrop(HEADER_TYPE_ID_BITS).toInt(BINARY_BASE)
        val subPackets = mutableListOf<Packet>()
        val value: BigInteger
        if (typeId == TYPE_ID_LITERAL) {
            // decode the literal value
            value = buildString {
                do {
                    val hasNext = bits.takeAndDrop(SINGLE_BIT) == "1"
                    val subBits = bits.takeAndDrop(BITS_PER_NYBBLE)
                    append(subBits)
                } while (hasNext)
            }.toBigInteger(BINARY_BASE)
        } else {
            // decode Length Type ID
            if (bits.takeAndDrop(SINGLE_BIT) == "0") {
                val numberOfSubPacketBits = bits.takeAndDrop(15).toInt(BINARY_BASE)
                var totalBitsConsumed = 0
                do {
                    val subPacket = bits.consumeAndParseBitsAsPacket().also { totalBitsConsumed += it.bitsConsumed }
                    subPackets.add(subPacket)
                } while (totalBitsConsumed < numberOfSubPacketBits)

            } else {
                val numberOfImmediateSubPackets = bits.takeAndDrop(11).toInt(BINARY_BASE)
                repeat(numberOfImmediateSubPackets) {
                    subPackets.add(bits.consumeAndParseBitsAsPacket())
                }
            }

            // determine the value based on operation Type ID
            val values = subPackets.map { it.value }
            value = when (typeId) {
                TYPE_ID_SUM -> values.fold(BigInteger.ZERO) { a, b -> a.plus(b) }
                TYPE_ID_MUL -> values.fold(BigInteger.ONE) { a, b -> a.multiply(b) }
                TYPE_ID_MIN -> values.minOf { it }
                TYPE_ID_MAX -> values.maxOf { it }
                TYPE_ID_GT -> if (values[0] > values[1]) BigInteger.ONE else BigInteger.ZERO
                TYPE_ID_LT -> if (values[0] < values[1]) BigInteger.ONE else BigInteger.ZERO
                TYPE_ID_EQ -> if (values[0] == values[1]) BigInteger.ONE else BigInteger.ZERO
                else -> error("Unknown typeId ($typeId)")
            }
        }

        return Packet(version, typeId, value, bitsConsumed = bitsAvailableBefore - bits.length, subPackets)
    }

    data class Packet(
        val version: Int,
        val typeId: Int,
        val value: BigInteger,
        val bitsConsumed: Int,
        val subPackets: MutableList<Packet> = mutableListOf()
    ) {
        private fun versionNumbers(): List<Int> {
            return mutableListOf(version).apply { addAll(subPackets.flatMap { it.versionNumbers() }) }
        }

        fun versionSum(): Long = versionNumbers().sumOf { it.toLong() }
    }

    fun part1(input: String): Long {
        return input.toPacket().versionSum()
    }

    fun part2(input: String): BigInteger {
        return input.toPacket().value
    }

}

fun main() {
    val solver = Aoc2021Day16()
    val prefix = "aoc2021/aoc2021day16"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(6L, solver.part1("D2FE28"))
    verify(9L, solver.part1("38006F45291200"))
    verify(14L, solver.part1("EE00D40C823060"))
    verify(16L, solver.part1("8A004A801A8002F478"))
    verify(12L, solver.part1("620080001611562C8802118E34"))
    verify(23L, solver.part1("C0015000016115A2E0802F182340"))
    verify(31L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(3.toBigInteger(), solver.part2("C200B40A82"))
    verify(54.toBigInteger(), solver.part2("04005AC33890"))
    verify(7.toBigInteger(), solver.part2("880086C3E88112"))
    verify(9.toBigInteger(), solver.part2("CE00C43D881120"))
    verify(1.toBigInteger(), solver.part2("D8005AC2A8F0"))
    verify(0.toBigInteger(), solver.part2("F600BC2D8F"))
    verify(0.toBigInteger(), solver.part2("9C005AC2F8F0"))
    verify(1.toBigInteger(), solver.part2("9C0141080250320F1802104A08"))
    verify(54.toBigInteger(), solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}