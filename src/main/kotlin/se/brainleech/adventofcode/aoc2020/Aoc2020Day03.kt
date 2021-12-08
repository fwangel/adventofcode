package se.brainleech.adventofcode.aoc2020

import java.util.stream.Stream
import kotlin.streams.toList

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

    fun part1(input: Stream<String>): Long {
        return Forest(input.toList()).treeCount(3, 1)
    }

    fun part2(input: Stream<String>): Long {
        val forest = Forest(input.toList())
        return forest.treeCount(1, 1)
            .times(forest.treeCount(3, 1))
            .times(forest.treeCount(5, 1))
            .times(forest.treeCount(7, 1))
            .times(forest.treeCount(1, 2))
    }

}
