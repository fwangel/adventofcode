package se.brainleech.adventofcode.aoc2020

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readIntegers
import se.brainleech.adventofcode.verify

class Aoc2020Day01 {

    fun part1(entries: List<Int>): Int {
        return entries
            .filter { entries.contains(2020 - it) }
            .take(1)
            .sumOf { it.times(2020 - it) }
    }

    fun part2(entries: List<Int>): Int {
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

fun main() {
    val solver = Aoc2020Day01()
    val prefix = "aoc2020/aoc2020day01"
    val testData = readIntegers("$prefix.test.txt")
    val realData = readIntegers("$prefix.real.txt")

    verify(514_579, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(241_861_950, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}
