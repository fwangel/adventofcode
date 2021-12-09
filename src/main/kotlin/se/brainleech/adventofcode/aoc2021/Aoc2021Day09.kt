package se.brainleech.adventofcode.aoc2021

import java.util.stream.Stream
import kotlin.streams.toList

class Aoc2021Day09 {

    data class Position(val row: Int, val col: Int, val height: Long)

    class Heightmap(
        private val rows: Int,
        private val columns: Int,
        private var heights: List<String>
    ) {
        private fun heightAt(row: Int, col: Int): Char {
            if (row < 0 || row >= rows || col < 0 || col >= columns) return '_'
            return heights[row][col]
        }

        fun lowPoints(): List<Position> {
            val lowPoints = mutableListOf<Position>()
            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    val current = heightAt(row, col)
                    if ((current != '9') // never low-point
                        && (heightAt(row - 1, col) >= current) // up
                        && (heightAt(row, col + 1) >= current) // right
                        && (heightAt(row + 1, col) >= current) // down
                        && (heightAt(row, col - 1) >= current) // left
                    ) {
                        lowPoints.add(Position(row, col, current.digitToInt().toLong()))
                    }
                }
            }
            return lowPoints.toList()
        }
    }

    fun part1(input: Stream<String>): Long {
        val heights = input.toList()
        if (heights.isEmpty()) return -1L
        val rows = heights.count()
        val columns = heights.first().length
        val map = Heightmap(rows, columns, heights)
        val lowPoints = map.lowPoints()

        return lowPoints.sumOf { it.height + 1 }
    }

    fun part2(input: Stream<String>): Long {
        return -1
    }

}

