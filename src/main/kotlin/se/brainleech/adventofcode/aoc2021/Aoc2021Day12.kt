package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2021Day12 {

    companion object {
        private const val CAVE_SEPARATOR = "-"

        private val CAVE_START = Cave("start")
        private val CAVE_END = Cave("end")
    }

    data class Cave(val name: String) {
        fun isStart(): Boolean = (name == CAVE_START.name)
        fun isEnd(): Boolean = (name == CAVE_END.name)
        fun isSmall(): Boolean = (name.lowercase() == name)

        override fun toString(): String = name
    }

    class CaveSystem(private val maxSmallCaveVisits: Int, private val exitsByCave: Map<Cave, List<Cave>>) {

        private fun List<Cave>.smallCaves(): List<Cave> {
            return this.filter { cave -> cave.isSmall() }
        }

        private fun List<Cave>.visitCountByName(): Map<String, Int> {
            return this.groupingBy { it.name }.eachCount()
        }

        private fun List<Cave>.withOccurrenceCount(matcher: (Int) -> Boolean): Boolean {
            return this.visitCountByName().values.any { matcher(it) }
        }

        fun findAllPathsFrom(newLocation: Cave, previousPath: List<Cave> = listOf()): List<List<Cave>> {
            val newPath = previousPath.plus(newLocation)
            if (newLocation.isEnd()) {
                return listOf(newPath)
            }

            // identify all exits that ...
            val exits = exitsByCave.getOrDefault(newLocation, setOf())
                // ... do not return to start
                .filterNot { exit -> exit.isStart() }

                // ... or is a small cave previously visited less than N times
                .filterNot { exit ->
                    exit.isSmall() && exit in newPath && newPath.smallCaves()
                        .withOccurrenceCount { it >= maxSmallCaveVisits }
                }

            // traverse the relevant exits to find all remaining paths
            return exits.flatMap { exit -> findAllPathsFrom(exit, newPath) }
        }

    }

    private fun List<String>.asCaveSystem(maxSmallCaveVisits: Int = 1): CaveSystem {
        return CaveSystem(maxSmallCaveVisits, this
            .asSequence()
            .flatMap {
                listOf(
                    it.split(CAVE_SEPARATOR),
                    it.split(CAVE_SEPARATOR).reversed()
                )
            }
            .groupBy({ Cave(it.first()) }, { Cave(it.last()) })
        )
    }

    fun part1(input: List<String>): Int {
        return input.asCaveSystem().findAllPathsFrom(CAVE_START).size
    }

    fun part2(input: List<String>): Int {
        return input.asCaveSystem(maxSmallCaveVisits = 2).findAllPathsFrom(CAVE_START).size
    }

}

fun main() {
    val solver = Aoc2021Day12()
    val prefix = "aoc2021/aoc2021day12"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(226, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(3_509, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}