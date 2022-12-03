package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import kotlin.math.max

class Aoc2022Day01 {

    fun part1(input: List<String>): Long {
        var maxCalories = 0L
        var currentCalories = 0L
        input.forEach {
            if (it.isBlank()) maxCalories = max(maxCalories, currentCalories).also { currentCalories = 0L }
            else currentCalories += it.toLong()
        }
        return max(maxCalories, currentCalories)
    }

    fun part2(input: List<String>): Long {
        val maxCalories = MutableList(4) { 0L }
        input.forEach {
            if (it.isBlank()) maxCalories.sortDescending().also { maxCalories[3] = 0L }
            else maxCalories[3] += it.toLong()
        }
        return maxCalories.sortedDescending().take(3).sum()
    }

}

fun main() {
    val solver = Aoc2022Day01()
    val prefix = "aoc2022/aoc2022day01"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(24000L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(45000L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}
