package se.brainleech.adventofcode.aoc2021

import kotlin.math.abs

class Day07 {

    fun part1(input: List<Int>): Int {
        val sorted = input.sorted()
        val minPosition = sorted.first()
        val maxPosition = sorted.last()
        println("$minPosition .. $maxPosition")

        var bestPosition = Int.MAX_VALUE
        var bestFuelUsage = Int.MAX_VALUE
        for (position in minPosition..maxPosition) {
            val fuelUsage = sorted.sumOf { abs(position - it) }
            if (fuelUsage < bestFuelUsage) {
                bestFuelUsage = fuelUsage
                bestPosition = position
            }
        }

        println("bestFuelUsage=$bestFuelUsage, bestPosition=$bestPosition")
        return bestFuelUsage
    }

    fun part2(input: List<Int>): Int {
        return -1
    }

}
