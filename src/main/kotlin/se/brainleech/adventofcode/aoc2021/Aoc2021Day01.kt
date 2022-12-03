package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readIntegers
import se.brainleech.adventofcode.verify

class Aoc2021Day01 {

    fun part1(input: List<Int>) = input
        .asSequence()
        .windowed(size = 2)
        .count { (current, next) -> current < next }

    fun part2(input: List<Int>) = input
        .asSequence()
        .windowed(size = 4)
        .count { (first, _, _, last) -> first < last }

}

fun main() {
    val solver = Aoc2021Day01()
    val prefix = "aoc2021/aoc2021day01"
    val testData = readIntegers("$prefix.test.txt")
    val realData = readIntegers("$prefix.real.txt")

    verify(7, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(5, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}