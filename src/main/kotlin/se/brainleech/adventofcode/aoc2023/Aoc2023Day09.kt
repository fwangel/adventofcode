package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.toListOfLongs
import se.brainleech.adventofcode.verify

class Aoc2023Day09 {

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

    private data class NumberSequence(val numbers: List<Long>) {
        fun nextValue(): Long {
            val tree = mutableListOf(numbers.toMutableList())
            while (true) {
                val diffs = tree.last()
                    .windowed(size = 2) {
                        it.last() - it.first()
                    }
                    .toMutableList()
                tree.add(diffs)
                if (diffs.isEmpty() || diffs.all { it == 0L }) {
                    tree.last().add(0L)
                    break
                }
            }
            var level = tree.size - 1
            while (level > 0) {
                val nextValue = tree[level].last() + tree[level - 1].last()
                tree[level - 1].add(nextValue)
                level--
            }
            return tree.first().last()
        }

        fun previousValue(): Long {
            val tree = mutableListOf(numbers.toMutableList())
            while (true) {
                val diffs = tree.last()
                    .windowed(size = 2) {
                        it.first() - it.last()
                    }
                    .toMutableList()
                tree.add(diffs)
                if (diffs.isEmpty() || diffs.all { it == 0L }) {
                    tree.last().add(0, 0L)
                    break
                }
            }
            var level = tree.size - 1
            while (level > 0) {
                val nextValue = tree[level].first() + tree[level - 1].first()
                tree[level - 1].add(0, nextValue)
                level--
            }
            return tree.first().first()
        }
    }

    private fun String.toNumberSequence(): NumberSequence {
        return NumberSequence(this.trim().replace("  ", " ").toListOfLongs(" "))
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toNumberSequence() }
            .sumOf { it.nextValue() }
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toNumberSequence() }
            .sumOf { it.previousValue() }
    }

}

fun main() {
    val solver = Aoc2023Day09()
    val prefix = "aoc2023/aoc2023day09"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(114L, solver.part1(testData))
    verify(23L, solver.part1(listOf("2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22")))
    verify(-23L, solver.part1(listOf("-2 -3 -4 -5 -6 -7 -8 -9 -10 -11 -12 -13 -14 -15 -16 -17 -18 -19 -20 -21 -22")))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(5L, solver.part2(listOf("10 13  16  21 30 45")))
    verify(2L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}