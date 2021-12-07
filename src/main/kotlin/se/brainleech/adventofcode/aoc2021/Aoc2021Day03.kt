package se.brainleech.adventofcode.aoc2021

import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList

class Aoc2021Day03 {
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
        if (groups.getValue(1).size >= groups.getValue(0).size) {
            return 1
        }
        return 0
    }

    private fun IntArray.leastOf(): Int {
        val groups = this.groupBy { it }.withDefault { emptyList() }
        if (groups.getValue(1).size < groups.getValue(0).size) {
            return 1
        }
        return 0
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

    fun part1(source: Stream<String>): Int {
        val input = source
            .map { it ->
                it.toCharArray()
                    .map { it.digitToInt() }
                    .toIntArray()
            }
            .collect(Collectors.toList())

        val output = transpose(input)

        val maxValue = (2 shl (output.size - 1)) - 1
        val gamma = output.mostOf()
        val epsilon = maxValue - gamma

        return gamma * epsilon
    }

    fun part2(source: Stream<String>): Int {
        val input = source
            .map { it ->
                it.toCharArray()
                    .map { it.digitToInt() }
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
