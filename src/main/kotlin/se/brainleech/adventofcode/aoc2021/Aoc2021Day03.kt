package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import java.util.stream.Collectors

class Aoc2021Day03 {

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
    }

    private fun transpose(matrix: List<IntArray>): Array<IntArray> {
        val rows = matrix.size
        val columns = matrix[0].size
        val transposed = Array(columns) { IntArray(rows) }
        for (i in 0 until rows) {
            for (j in 0 until columns) {
                transposed[j][i] = matrix[i][j]
            }
        }
        return transposed
    }

    private fun Array<IntArray>.mostOf(): Int {
        return this.withIndex().sumOf { it.value.mostOf() shl (this.size - 1 - it.index) }
    }

    private fun IntArray.mostOf(): Int {
        val groups = this.groupBy { it }.withDefault { emptyList() }
        if (groups.getValue(ONE).size >= groups.getValue(ZERO).size) {
            return ONE
        }
        return ZERO
    }

    private fun IntArray.leastOf(): Int {
        val groups = this.groupBy { it }.withDefault { emptyList() }
        if (groups.getValue(ONE).size < groups.getValue(ZERO).size) {
            return ONE
        }
        return ZERO
    }

    private fun Array<IntArray>.mostOfByIndex(): Map<Int, Int> {
        return this.withIndex().associate { it.index to it.value.mostOf() }
    }

    private fun Array<IntArray>.leastOfByIndex(): Map<Int, Int> {
        return this.withIndex().associate { it.index to it.value.leastOf() }
    }

    private fun IntArray.toDecimal(): Int {
        return this.withIndex().sumOf { it.value shl (this.size - 1 - it.index) }
    }

    fun part1(source: List<String>): Int {
        val input = source.stream()
            .map { bits ->
                bits.toCharArray()
                    .map { bit -> bit.digitToInt() }
                    .toIntArray()
            }
            .collect(Collectors.toList())

        val output = transpose(input)

        val maxValue = (2 shl (output.size - 1)) - 1
        val gamma = output.mostOf()
        val epsilon = maxValue - gamma

        return gamma * epsilon
    }

    fun part2(source: List<String>): Int {
        val input = source
            .map { bits ->
                bits.toCharArray()
                    .map { bit -> bit.digitToInt() }
                    .toIntArray()
            }
            .toList()

        fun oxygenGeneratorRating(): Int {
            var bitIndex = 0
            var ratings = input
            do {
                val output = transpose(ratings)
                val byIndex = output.mostOfByIndex()
                ratings = ratings.filter { it[bitIndex] == byIndex[bitIndex] }
                bitIndex++
            } while (ratings.size > 1)
            return ratings.first().toDecimal()
        }

        fun co2ScrubberRating(): Int {
            var bitIndex = 0
            var ratings = input
            do {
                val output = transpose(ratings)
                val byIndex = output.leastOfByIndex()
                ratings = ratings.filter { it[bitIndex] == byIndex[bitIndex] }
                bitIndex++
            } while (ratings.size > 1)
            return ratings.first().toDecimal()
        }

        val oxygen = oxygenGeneratorRating()
        val co2 = co2ScrubberRating()

        return oxygen * co2
    }

}

fun main() {
    val solver = Aoc2021Day03()
    val prefix = "aoc2021/aoc2021day03"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(198, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(230, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}