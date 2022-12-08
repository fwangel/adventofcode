package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import java.util.stream.IntStream
import kotlin.streams.toList

class Aoc2022Day08 {

    companion object {
        const val DEBUG = false
    }

    private inline fun <T> T.debug(block: (T) -> Unit): T {
        if (DEBUG) block(this)
        return this
    }

    private fun List<List<Int>>.heightAt(x: Int, y: Int) = this[y][x]

    private fun List<List<Int>>.heightsNorthOf(x: Int, y: Int) : List<Int> {
        if (y < 1) return emptyList()
        return this.asSequence().filterIndexed { row, _ -> row < y }.map { it[x] }.toList().reversed()
            .debug { println("NORTH OF (x:$x, y:$y) => $it") }
    }

    private fun List<List<Int>>.heightsEastOf(x: Int, y: Int) : List<Int> {
        if (x > this.first().size - 1) return emptyList()
        return this[y].asSequence().filterIndexed { column, _ -> column > x }.toList()
            .debug { println("EAST OF (x:$x, y:$y) => $it") }
    }

    private fun List<List<Int>>.heightsSouthOf(x: Int, y: Int) : List<Int> {
        if (y > this.size - 1) return emptyList()
        return this.asSequence().filterIndexed { row, _ -> row > y }.map { it[x] }.toList()
            .debug { println("SOUTH OF (x:$x, y:$y) => $it") }
    }

    private fun List<List<Int>>.heightsWestOf(x: Int, y: Int) : List<Int> {
        if (x < 1) return emptyList()
        return this[y].asSequence().filterIndexed { column, _ -> column < x }.toList().reversed()
            .debug { println("WEST OF (x:$x, y:$y) => $it") }
    }

    private fun List<List<Int>>.isTallestTreeInTheLineOfSightFrom(x: Int, y: Int) : Boolean {
        val treeHeight = this.heightAt(x, y)
        return (this.heightsNorthOf(x, y).all { it < treeHeight } || this.heightsWestOf(x, y).all { it < treeHeight })
            || (this.heightsSouthOf(x, y).all { it < treeHeight } || this.heightsEastOf(x, y).all { it < treeHeight })
    }

    private fun List<Int>.takeWhileShorterOrFirstOfSameHeightOrHigherThan(height: Int) : List<Int> {
        val heights = this.takeWhile { it < height }.toMutableList()
        if (heights.size < this.size && this[heights.size] >= height) {
            heights.add(this[heights.size])
        }
        return heights
            .debug { println("   SCENIC PATH: $it") }
    }

    private fun List<List<Int>>.scenicScoreAt(x: Int, y: Int) : Int {
        val treeHeight = this.heightAt(x, y)
        val northScore = this.heightsNorthOf(x, y).takeWhileShorterOrFirstOfSameHeightOrHigherThan(treeHeight).count()
        val eastScore = this.heightsEastOf(x, y).takeWhileShorterOrFirstOfSameHeightOrHigherThan(treeHeight).count()
        val southScore = this.heightsSouthOf(x, y).takeWhileShorterOrFirstOfSameHeightOrHigherThan(treeHeight).count()
        val westScore = this.heightsWestOf(x, y).takeWhileShorterOrFirstOfSameHeightOrHigherThan(treeHeight).count()
        return (northScore * eastScore * southScore * westScore)
            .debug { println("   SCORE AT (x:$x, y:$y) = $it") }
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        val forest = input.map { it.toCharArray().map { height -> height.digitToInt() } }.toList()
        val rows = IntStream.range(1, forest.size - 1).toList()
        val columns = IntStream.range(1, forest.first().size - 1).toList()

        var visibleTrees = forest.size.times(2).plus(forest.first().size.times(2)).minus(4)
        rows.forEach { row ->
            columns.forEach { column ->
                if (forest.isTallestTreeInTheLineOfSightFrom(column, row)) {
                    visibleTrees ++
                }
            }
        }

        return visibleTrees
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        val forest = input.map { it.toCharArray().map { height -> height.digitToInt() } }.toList()
        val rows = IntStream.range(1, forest.size - 1).toList()
        val columns = IntStream.range(1, forest.first().size - 1).toList()

        val scenicScores = mutableMapOf<Pair<Int, Int>, Int>()
        rows.forEach { y ->
            columns.forEach { x ->
                scenicScores[x to y] = forest.scenicScoreAt(x, y)
            }
        }

        return scenicScores.values.maxOf { it }
    }

}

fun main() {
    val solver = Aoc2022Day08()
    val prefix = "aoc2022/aoc2022day08"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(21, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(8, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}