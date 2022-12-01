package se.brainleech.adventofcode.aoc2022

import java.util.stream.Stream
import kotlin.math.max

class Aoc2022Day01 {

    fun part1(input: Stream<String>): Long {
        var maxCalories = Long.MIN_VALUE
        var currentCalories = 0L
        input.forEach {
            if (it.isBlank()) currentCalories = 0L
            else currentCalories += it.toLong()
            maxCalories = max(maxCalories, currentCalories)
        }
        return maxCalories
    }

    fun part2(input: Stream<String>): Long {
        val maxCalories = MutableList(4) { 0L }
        var currentCalories = 0L
        input.forEach {
            if (it.isBlank()) maxCalories.sortDescending().also { currentCalories = 0L }
            else currentCalories += it.toLong()
            maxCalories[3] = currentCalories
        }
        return maxCalories.sortedDescending().take(3).sum()
    }

}
