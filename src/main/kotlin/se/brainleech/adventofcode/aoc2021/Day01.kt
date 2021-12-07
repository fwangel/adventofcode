package se.brainleech.adventofcode.aoc2021

import java.util.stream.IntStream
import kotlin.streams.asSequence

class Day01 {

    fun part1(input: IntStream) = input
        .asSequence()
        .windowed(2)
        .count { (current, next) -> current < next }

    fun part2(input: IntStream) = input
        .asSequence()
        .windowed(4)
        .count { (a, _, _, d) -> a < d }

}
