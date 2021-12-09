package se.brainleech.adventofcode.aoc2021

import java.util.stream.Stream
import kotlin.streams.toList

class Aoc2021Day09 {

    companion object {
        private const val MAX_HEIGHT = '9'
        private const val OUTSIDE = '_'
        private const val BASIN = '~'
    }

    data class Position(val row: Int, val col: Int, val height: Long)

    class Heightmap(
        private val rows: Int,
        private val columns: Int,
        private var heights: List<CharArray>
    ) {
        private fun heightAt(row: Int, col: Int): Char {
            if (row < 0 || row >= rows || col < 0 || col >= columns) return OUTSIDE
            return heights[row][col]
        }

        private fun fillAt(row: Int, col: Int): Int {
            if (row < 0 || row >= rows || col < 0 || col >= columns) return 0
            if (heights[row][col] >= MAX_HEIGHT) return 0
            heights[row][col] = BASIN
            return 1
        }

        fun lowPoints(): List<Position> {
            val lowPoints = mutableListOf<Position>()
            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    val current = heightAt(row, col)
                    if ((current != MAX_HEIGHT) // never a low-point
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

        private fun fillBasin(row: Int, col: Int): Int {
            if (heightAt(row, col) >= MAX_HEIGHT) return 0
            return fillAt(row, col) + // fill and count current position
                    fillBasin(row - 1, col) + // up
                    fillBasin(row, col + 1) + // right
                    fillBasin(row + 1, col) + // down
                    fillBasin(row, col - 1) // left
        }

        fun basinSizes(max: Int): List<Long> {
            val basinSizes = mutableListOf<Long>()
            lowPoints().forEach { position ->
                basinSizes.add(fillBasin(position.row, position.col).toLong())

                basinSizes.sortDescending()
                if (basinSizes.size > max) basinSizes.removeAt(max)
            }
            return basinSizes.toList()
        }
    }

    private fun Stream<String>.asHeightmap(): Heightmap {
        val heights = this.map { it.toCharArray() }.toList()
        val rows = heights.count()
        val columns = heights.first().size
        return Heightmap(rows, columns, heights)
    }

    fun part1(input: Stream<String>): Long {
        return input
            .asHeightmap()
            .lowPoints()
            .sumOf { it.height + 1 }
    }

    fun part2(input: Stream<String>): Long {
        return input
            .asHeightmap()
            .basinSizes(3)
            .reduce { totalBasinSize, basinSize -> totalBasinSize.times(basinSize) }
    }

}

