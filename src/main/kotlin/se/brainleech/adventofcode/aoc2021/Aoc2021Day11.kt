package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2021Day11 {

    companion object {
        private const val LINE_FEED = "\n"
        private const val EMPTY = ""
    }

    class Octopus(
        var energy: Int,
        var justFlashed: Boolean = false,
        private var neighbors: MutableList<Octopus> = mutableListOf()
    ) {
        fun allNeighbors(): List<Octopus> {
            return neighbors.toList()
        }

        fun addNeighbor(octopus: Octopus?) {
            if (octopus != null) {
                neighbors.add(octopus)
            }
        }
    }

    class Shoal(
        private val rows: Int,
        private val columns: Int,
        private var octopi: List<List<Octopus>>
    ) {
        private var totalFlashes: Long = 0L

        init {
            // identify neighbors for each octopus
            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    val octopus = octopi[row][col]
                    octopus.addNeighbor(octopusAt(row - 1, col - 1)) // top-left
                    octopus.addNeighbor(octopusAt(row - 1, col - 0)) // top
                    octopus.addNeighbor(octopusAt(row - 1, col + 1)) // top-right
                    octopus.addNeighbor(octopusAt(row - 0, col + 1)) // right
                    octopus.addNeighbor(octopusAt(row + 1, col + 1)) // bottom-right
                    octopus.addNeighbor(octopusAt(row + 1, col - 0)) // bottom
                    octopus.addNeighbor(octopusAt(row + 1, col - 1)) // bottom-left
                    octopus.addNeighbor(octopusAt(row - 0, col - 1)) // left
                }
            }
        }

        private fun octopusAt(row: Int, col: Int): Octopus? {
            if (row < 0 || row >= rows || col < 0 || col >= columns) return null
            return octopi[row][col]
        }

        // boosts each and returns all just-flashed octopi
        private fun boostAll(sourceOctopi: List<Octopus>): List<Octopus> {
            val flashed = mutableListOf<Octopus>()
            for (octopus in sourceOctopi) {
                if (++octopus.energy > 9 && !octopus.justFlashed) {
                    octopus.justFlashed = true
                    totalFlashes++
                    flashed.add(octopus)
                }
            }
            return flashed
        }

        // restore the just-flashed ones
        private fun drainEnergyOfJustFlashed() {
            for (octopus in octopi.flatten()) {
                if (octopus.justFlashed) {
                    octopus.justFlashed = false
                    octopus.energy = 0
                }
            }
        }

        private fun affectNearbyOctopi(sourceOctopi: List<Octopus>) {
            if (sourceOctopi.isEmpty()) return

            sourceOctopi.forEach { octopus ->
                affectNearbyOctopi(boostAll(octopus.allNeighbors()))
            }
        }

        private fun totalEnergy(): Int {
            return octopi.flatten().sumOf { it.energy }
        }

        fun simulate(steps: Int): Long {
            repeat(steps) {
                val justFlashed = boostAll(this.octopi.flatten())
                affectNearbyOctopi(justFlashed)
                drainEnergyOfJustFlashed()
            }

            // println("After step %d:\n%s\n\n".format(steps, this))
            return totalFlashes
        }

        fun simulateUntilAllFlashes(): Int {
            repeat(Int.MAX_VALUE) { step ->
                val justFlashed = boostAll(this.octopi.flatten())
                affectNearbyOctopi(justFlashed)
                drainEnergyOfJustFlashed()
                if (totalEnergy() == 0) {
                    return step + 1
                }
            }
            return 0
        }

        override fun toString(): String {
            return octopi.joinToString(LINE_FEED) { row ->
                row.joinToString(EMPTY) { col -> if (col.justFlashed) "!" else col.energy.toString() }
            }
        }
    }

    private fun List<String>.asShoal(): Shoal {
        val octopi = this.map { line -> line.toCharArray().map { char -> Octopus(char.digitToInt()) } }.toList()
        val rows = octopi.count()
        val columns = octopi.first().size
        return Shoal(rows, columns, octopi)
    }

    fun part1(input: List<String>): Long {
        return input
            .asShoal()
            .simulate(100)
    }

    fun part2(input: List<String>): Long {
        return input
            .asShoal()
            .simulateUntilAllFlashes()
            .toLong()
    }

}

fun main() {
    val solver = Aoc2021Day11()
    val prefix = "aoc2021/aoc2021day11"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(1_656L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(195L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}