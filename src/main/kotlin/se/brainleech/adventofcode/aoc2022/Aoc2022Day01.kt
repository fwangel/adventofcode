package se.brainleech.adventofcode.aoc2022

import java.util.stream.Stream
import kotlin.math.max

class Aoc2022Day01 {

    fun part1(input: Stream<String>): Long {
        var maxCalories = 0L
        var currentCalories = 0L
        input.forEach {
            if (it.isBlank()) maxCalories = max(maxCalories, currentCalories).also { currentCalories = 0L }
            else currentCalories += it.toLong()
        }
        return max(maxCalories, currentCalories)
    }

    fun part2(input: Stream<String>): Long {
        val maxCalories = MutableList(4) { 0L }
        input.forEach {
            if (it.isBlank()) maxCalories.sortDescending().also { maxCalories[3] = 0L }
            else maxCalories[3] += it.toLong()
        }
        return maxCalories.sortedDescending().take(3).sum()
    }

}
