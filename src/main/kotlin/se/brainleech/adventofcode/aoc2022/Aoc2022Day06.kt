package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day06 {

    private fun process(input: String, packetSize: Int) : Int {
        if (input.length < packetSize) return -1
        return input
            .asSequence()
            .windowed(size = packetSize,  partialWindows = false)
            .withIndex()
            .filter { it.value.distinct().size == packetSize }
            .map { it.index + packetSize }
            .first()
    }

    fun part1(input: String) : Int { return process(input, packetSize =  4) }
    fun part2(input: String) : Int { return process(input, packetSize = 14) }
}

fun main() {
    val solver = Aoc2022Day06()
    val prefix = "aoc2022/aoc2022day06"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    listOf(7, 5, 6, 10, 11).forEachIndexed { index, expected -> verify(expected, solver.part1(testData[index])) }
    compute({ solver.part1(realData.first()) }, "$prefix.part1 = ")

    listOf(19, 23, 23, 29, 26).forEachIndexed { index, expected -> verify(expected, solver.part2(testData[index])) }
    compute({ solver.part2(realData.first()) }, "$prefix.part2 = ")
}