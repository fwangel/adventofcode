package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class Aoc2023Day11 {

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

    class Galaxy(val id: Int, val x: Long, val y: Long) : Comparable<Galaxy> {
        override fun compareTo(other: Galaxy): Int {
            return id.minus(other.id).sign
        }

        override fun toString(): String = "($id: $x,$y)"
    }

    private data class GalaxyMap(val galaxies: List<Galaxy>) {
        private var map = buildMap()

        private fun buildMap(): List<Pair<Galaxy, Galaxy>> {
            val used = mutableSetOf<String>()
            val map = mutableListOf<Pair<Galaxy, Galaxy>>()
            galaxies.forEach { galaxy ->
                galaxies.forEach innerLoop@ { otherGalaxy ->
                    if (galaxy == otherGalaxy) return@innerLoop
                    val identity = "${min(galaxy.id, otherGalaxy.id)}-${max(galaxy.id, otherGalaxy.id)}"
                    if (used.contains(identity)) return@innerLoop
                    map.add(galaxy to otherGalaxy)
                    used.add(identity)
                }
            }
            return map
        }

        fun shortestTotalPath(): Long {
            val distances = map.map { pair ->
                (max(pair.first.x, pair.second.x) - min(pair.first.x, pair.second.x)) +
                (max(pair.first.y, pair.second.y) - min(pair.first.y, pair.second.y))
            }
            return distances.sum()
        }
    }

    private fun List<CharArray>.toGalaxies(expansion: Long): GalaxyMap {
        val grid = this
        val width = grid.first().size
        val height = grid.size
        val emptyRows = grid.mapIndexed { index, line -> if (line.all { it == '.' }) index else -1 }.filter { it > 0 }.toList()
        val emptyCols = (0 until width).filter { col -> grid.map { line -> line[col] }.all { it == '.' } }.toList()

        debug { println("emptyRows=$emptyRows") }
        debug { println("emptyCols=$emptyCols") }

        val galaxies = mutableListOf<Galaxy>()
        var id = 0
        var y = 0L
        for (row in 0 until height) {
            var x = 0L
            for (col in 0 until width) {
                if (grid[row][col] == '#') galaxies.add(Galaxy(++id, x, y))
                if (col in emptyCols) x += expansion else x++
            }
            if (row in emptyRows) y += expansion else y++
        }

        return GalaxyMap(galaxies)
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .toListOfCharArrays()
            .toGalaxies(expansion = 2)
            .shortestTotalPath()
    }

    fun part2(input: List<String>, expansion: Long = 1_000_000L) : Long {
        if (input.isEmpty()) return -1L
        return input
            .toListOfCharArrays()
            .toGalaxies(expansion)
            .shortestTotalPath()
    }

}

fun main() {
    val solver = Aoc2023Day11()
    val prefix = "aoc2023/aoc2023day11"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(374L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(1030L, solver.part2(testData, 10L))
    verify(8410L, solver.part2(testData, 100L))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}