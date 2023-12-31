package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.verify

class Aoc2023Day13 {

    companion object {
        const val DEBUG = false
        const val TRACE = false

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }
    }

    private data class Valley(val id: Int, val patterns: List<String>, val withSmudges: Boolean = false, val rotated: Boolean = false) {
        private val width = patterns.first().length
        private val height = patterns.size
        private val expectedSmudges = if (withSmudges) 1 else 0

        private fun orientation() = if (rotated) "Vertical (rotated)" else "Horizontal"

        private fun numberOfColumnsLeftOfVerticalLineOfReflection(): Long {
            val rotatedPatterns = mutableListOf<String>()
            for (col in 0 until width) {
                val newRow = mutableListOf<Char>()
                for (row in height - 1 downTo 0) {
                    newRow.add(patterns[row][col])
                }
                rotatedPatterns.add(newRow.joinToString(separator = ""))
            }
            return Valley(id, rotatedPatterns, withSmudges, rotated = true).numberOfRowsAboveHorizontalLineOfReflection()
        }

        private fun numberOfRowsAboveHorizontalLineOfReflection(): Long {
            trace { println("\n#$id: ${orientation()}:\n" + patterns.joinToString(separator = "\n")) }

            for (row in 0 until height - 1) {
                var smudges = 0
                for (rowOffset in 0 until height) {
                    val above = row - rowOffset
                    val below = row + 1 + rowOffset
                    if (above >= 0 && below < height) {
                        smudges += (0 until width).count { col -> patterns[above][col] != patterns[below][col] }
                    }
                }
                if (smudges == expectedSmudges) {
                    debug { println("#$id: Mirrored at ${row.plus(1)} (orientation: ${orientation()})") }
                    return row.plus(1).toLong()
                }
            }

            debug { println("#$id: Not mirrored (orientation: ${orientation()})") }
            return 0L
        }

        private fun Long.orElse(other: () -> Long): Long {
            return if (this == 0L) other() else this
        }

        fun summary(): Long {
            return numberOfRowsAboveHorizontalLineOfReflection().times(100L)
                .orElse { numberOfColumnsLeftOfVerticalLineOfReflection() }
        }
    }

    fun part1(input: String) : Long {
        if (input.isEmpty()) return -1L
        return input
            .split("\n\n")
            .map { it.split("\n") }
            .mapIndexed { index, patterns -> Valley(index + 1, patterns).summary() }
            .sum()
    }

    fun part2(input: String) : Long {
        if (input.isEmpty()) return -1L
        return input
            .split("\n\n")
            .map { it.split("\n") }
            .mapIndexed { index, patterns -> Valley(index + 1, patterns, withSmudges = true).summary() }
            .sum()
    }

}

fun main() {
    val solver = Aoc2023Day13()
    val prefix = "aoc2023/aoc2023day13"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(405L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(400L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}