package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.Location
import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import java.util.*
import kotlin.math.sign

class Aoc2021Day15 {

    companion object {
        private const val OUTSIDE_RISK = Int.MAX_VALUE
        private const val INITIAL_RISK = 0
        private const val MAX_RISK = 9
    }

    private class LocationWithRisk(location: Location, val accumulatedRisk: Int) :
        Location(location.x, location.y), Comparable<LocationWithRisk> {
        override fun compareTo(other: LocationWithRisk): Int {
            return accumulatedRisk.minus(other.accumulatedRisk).sign
        }
    }

    class Cavern(
        private var riskLevels: List<IntArray>,
        private val exitLocation: Location
    ) {
        private val startLocation = Location()
        private val height = riskLevels.count()
        private val width = riskLevels.first().size

        // extend Location with limits
        private fun Location.isOutside(): Boolean = (y < 0 || y > exitLocation.y || x < 0 || x > exitLocation.x)
        private fun Location.isAtStart(): Boolean = this == startLocation
        private fun Location.isAtEnd(): Boolean = this == exitLocation

        private fun Location.withRisk(risk: Int): LocationWithRisk = LocationWithRisk(this, risk)
        private fun LocationWithRisk.moveTo(newLocation: Location): LocationWithRisk {
            return LocationWithRisk(newLocation, accumulatedRisk.plus(riskAt(newLocation)))
        }

        private fun riskAt(location: Location): Int {
            if (location.isOutside()) return OUTSIDE_RISK
            // "[in part 2] each time the tile repeats to the right or downward,
            //  all of its risk levels are 1 higher than the tile immediately up
            //  or left of it. However, risk levels above 9 wrap back around to 1"
            val x = location.x
            val y = location.y
            val dx = x / width
            val dy = y / height
            val risk = riskLevels[x % width][y % height] + dx + dy
            return if (risk <= MAX_RISK) risk else risk - MAX_RISK
        }

        fun minimumRisk(): Int {
            // println("Looking for the least risky path between $startLocation and $exitLocation ...")

            val pendingLocations = PriorityQueue(listOf(startLocation.withRisk(INITIAL_RISK)))
            val visitedLocations = mutableSetOf<Location>()

            while (pendingLocations.isNotEmpty()) {
                val location = pendingLocations.poll()
                if (location.isAtEnd()) {
                    // println("arrived at exit $location with total risk of ${location.accumulatedRisk}")
                    return location.accumulatedRisk
                }
                if (location !in visitedLocations) {
                    visitedLocations.add(location)
                    location
                        .neighbours()
                        .filterNot { it.isOutside() || it.isAtStart() }
                        .forEach { adjacentLocation ->
                            pendingLocations.offer(location.moveTo(adjacentLocation))
                        }
                }
            }

            return Int.MAX_VALUE
        }

    }

    private fun List<String>.asCavern(expanded: Boolean = false): Cavern {
        this.map { line -> line.toCharArray().map { char -> char.digitToInt() }.toIntArray() }.toList()
            .let { riskLevels ->
                val height = riskLevels.count()
                val width = riskLevels.first().size
                val sizeFactor = if (expanded) 5 else 1
                return Cavern(
                    riskLevels,
                    Location(sizeFactor * width - 1, sizeFactor * height - 1)
                )
            }
    }

    fun part1(input: List<String>): Int {
        return input
            .asCavern()
            .minimumRisk()
    }

    fun part2(input: List<String>): Int {
        return input
            .asCavern(expanded = true)
            .minimumRisk()
    }

}

fun main() {
    val solver = Aoc2021Day15()
    val prefix = "aoc2021/aoc2021day15"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(40, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(315, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}