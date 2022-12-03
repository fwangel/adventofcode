package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.toListOfInts
import se.brainleech.adventofcode.verify
import java.util.stream.IntStream
import kotlin.math.abs

class Aoc2021Day07 {

    fun part1(input: List<Int>): Int {
        val sorted = input.sorted()
        val minPosition = sorted.first()
        val maxPosition = sorted.last()

        return IntStream.rangeClosed(minPosition, maxPosition)
            .map { position -> sorted.sumOf { abs(position - it) } }
            .min()
            .orElse(-1)
    }

    fun part2(input: List<Int>): Int {
        val sorted = input.sorted()
        val minPosition = sorted.first()
        val maxPosition = sorted.last()

        return IntStream.rangeClosed(minPosition, maxPosition)
            .map { position -> sorted.sumOf { IntStream.rangeClosed(1, abs(position - it)).sum() } }
            .min()
            .orElse(-1)
    }

}

fun main() {
    val solver = Aoc2021Day07()
    val prefix = "aoc2021/aoc2021day07"
    val testData = readText("$prefix.test.txt").toListOfInts()
    val realData = readText("$prefix.real.txt").toListOfInts()

    verify(37, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(168, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}