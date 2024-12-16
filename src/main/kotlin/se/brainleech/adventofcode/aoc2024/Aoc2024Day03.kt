package se.brainleech.adventofcode.aoc2024

import se.brainleech.adventofcode.aoc2024.Aoc2024Day02.Companion.debug
import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.toListOfLongs
import se.brainleech.adventofcode.verify
import kotlin.math.abs

class Aoc2024Day03 {

    companion object {
        const val DEBUG = true
        const val TRACE = false

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }

        private val RE_DO_OR_DONT = """(do|don't)\(\)""".toRegex()
        private val RE_MUL_INSTR = """mul\((\d+,\d+)\)""".toRegex()
    }

    private data class Mul(val x: Long, val y: Long) {
        fun result(): Long = x * y
        fun isZero(): Boolean = x == 0L || y == 0L

        companion object {
            val ZERO = Mul(0L, 0L)
        }
    }

    private fun String.toMul(): Mul {
        val operands = this.toListOfLongs()
        return Mul(operands.first(), operands.last())
    }

    private fun String.toMulInstructions(): List<Mul> {
        return RE_MUL_INSTR.findAll(this)
            .map { m -> m.groupValues[1].toMul() }
            .toList()
    }

    private fun String.toMulInstructionsWithDoOrDont(): List<Mul> {
        val enableDisableMap = RE_DO_OR_DONT.findAll(this)
            .map { m -> if (m.groupValues[1] == "do") m.range.last else -m.range.last }
            .toMutableList()
            .also { it.add(0, 0) } // starts enabled

        var enabled = true
        var enabledIndex = 0
        return RE_MUL_INSTR.findAll(this)
            .map { m ->
                if (enabledIndex < enableDisableMap.size && m.range.first > abs(enableDisableMap[enabledIndex])) {
                    enabled = enableDisableMap[enabledIndex ++] >= 0
                }
                if (enabled) m.groupValues[1].toMul() else Mul.ZERO
            }
            .filter { !it.isZero() }
            .toList()
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .map { it.toMulInstructions() }
            .flatMap { instr -> instr.map { it.result() } }
            .sum()
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .joinToString(separator = "")
            .toMulInstructionsWithDoOrDont()
            .sumOf { it.result() }
    }

}

fun main() {
    val solver = Aoc2024Day03()
    val prefix = "aoc2024/aoc2024day03"
    val testDataP1a = readLines("$prefix.test.p1-a.txt")
    val testDataP2a = readLines("$prefix.test.p2-a.txt")
    val realData = readLines("$prefix.real.txt")

    verify(161L, solver.part1(testDataP1a))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(48L, solver.part2(testDataP2a))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}