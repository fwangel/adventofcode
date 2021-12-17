package se.brainleech.adventofcode.aoc2021

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
