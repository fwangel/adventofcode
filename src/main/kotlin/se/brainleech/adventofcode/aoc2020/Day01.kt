package se.brainleech.adventofcode.aoc2020

import java.util.stream.IntStream
import kotlin.streams.toList

class Day01 {

    fun part1(input: IntStream): Int {
        val entries = input.toList()
        return entries
            .filter { entries.contains(2020 - it) }
            .take(1)
            .sumOf { it.times(2020 - it) }
    }

    fun part2(input: IntStream): Int {
        val entries = input.toList()
        for (first in entries) {
            for (second in entries.filter { it != first }) {
                val third = entries
                    .filter { third -> third != first && third != second }
                    .firstOrNull { third -> first.plus(second).plus(third) == 2020 }
                if (third != null) {
                    return first.times(second).times(third)
                }
            }
        }
        return -1
    }

}
