package se.brainleech.adventofcode.aoc2020

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2020Day03 {

    companion object {
        private const val TREE = '#'
        private const val HIT = 'X'
        private const val MISS = 'O'
    }

    data class Forest(val slices: List<String>) {
        private fun String.markedAt(column: Int): String {
            val output = this.toCharArray()
            val index = (column - 1).mod(this.length)
            output[index] = if (output[index] == TREE) HIT else MISS
            return String(output)
        }

        fun treeCount(dx: Int, dy: Int): Long {
            var column = 1
            return slices.asSequence()
                .drop(dy)
                .filterIndexed { row, _ -> row.mod(dy) == 0 }
                .onEach { column += dx }
                .map { it.markedAt(column) }
                .count { it.contains(HIT) }
                .toLong()
        }
    }

    fun part1(input: List<String>): Long {
        return Forest(input).treeCount(3, 1)
    }

    fun part2(input: List<String>): Long {
        val forest = Forest(input)
        return forest.treeCount(1, 1)
            .times(forest.treeCount(3, 1))
            .times(forest.treeCount(5, 1))
            .times(forest.treeCount(7, 1))
            .times(forest.treeCount(1, 2))
    }

}

fun main() {
    val solver = Aoc2020Day03()
    val prefix = "aoc2020/aoc2020day03"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(7L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(336L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}