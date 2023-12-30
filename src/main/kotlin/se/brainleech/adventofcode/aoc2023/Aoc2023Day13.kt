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

    private data class Valley(val id: Int, val patterns: List<String>) {
        private val width = patterns.first().length
        private val height = patterns.size
        private fun numberOfColumnsLeftOfVerticalLineOfReflection(): Long {
            val rotatedPatterns = mutableListOf<String>()
            for (col in 0 until width) {
                val newRow = mutableListOf<Char>()
                for (row in height - 1 downTo 0) {
                    newRow.add(patterns[row][col])
                }
                rotatedPatterns.add(newRow.joinToString(separator = ""))
            }
            trace { println("#$id: Vertical (rotated):\n" + rotatedPatterns.joinToString(separator = "\n") + "\n") }
            return Valley(id, rotatedPatterns).numberOfRowsAboveHorizontalLineOfReflection()
        }

        private fun numberOfRowsAboveHorizontalLineOfReflection(): Long {
            trace { println("#$id: Horizontal:\n" + patterns.joinToString(separator = "\n") + "\n") }
            val reflectionStarts = patterns.windowed(size = 2, step = 1).mapIndexed() { index, rows -> if (rows.first() == rows.last()) index else -1 }.filter { it >= 0 }
            reflectionStarts.forEach innerLoop@ { reflectionStart ->
                var above = reflectionStart
                var below = reflectionStart + 1
                while (above >= 0 && below < height) {
                    trace { println("    Checking: ${patterns[above]} vs ${patterns[below]} ($above vs $below)") }
                    if (patterns[above --] != patterns[below ++]) return@innerLoop
                }
                return reflectionStart.plus(1).toLong()
                    .debug { println("#$id: Found reflection at: $it") }
            }
            trace { println("#$id: No mirror at all!") }
            return 0L
        }

        fun summary(): Long {
            return numberOfColumnsLeftOfVerticalLineOfReflection().plus(numberOfRowsAboveHorizontalLineOfReflection().times(100L))
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
        return -1L
    }

}

fun main() {
    val solver = Aoc2023Day13()
    val prefix = "aoc2023/aoc2023day13"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(405L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(-1L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}