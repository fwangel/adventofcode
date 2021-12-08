package se.brainleech.adventofcode.aoc2021

import java.util.stream.IntStream
import kotlin.math.abs

class Aoc2021Day07 {

    fun part1(input: List<Int>): Int {
        val sorted = input.sorted()
        val minPosition = sorted.first()
        val maxPosition = sorted.last()

        return IntStream.rangeClosed(minPosition, maxPosition)
            .map { position ->
                sorted.sumOf { abs(position - it) }
            }
            .min()
            .orElse(-1)
    }

    fun part2(input: List<Int>): Int {
        val sorted = input.sorted()
        val minPosition = sorted.first()
        val maxPosition = sorted.last()

        return IntStream.rangeClosed(minPosition, maxPosition)
            .map { position ->
                sorted.sumOf {
                    IntStream.rangeClosed(1, abs(position - it)).sum()
                }
            }
            .min()
            .orElse(-1)
    }

}