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
    val testData = readLines("$prefix.test.txt").first()
    val realData = readLines("$prefix.real.txt").first()

    verify(7, solver.part1(testData))
    verify(5, solver.part1("bvwbjplbgvbhsrlpgdmjqwftvncz"))
    verify(6, solver.part1("nppdvjthqldpwncqszvftbrmjlhg"))
    verify(10, solver.part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"))
    verify(11, solver.part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(19, solver.part2(testData))
    verify(23, solver.part2("bvwbjplbgvbhsrlpgdmjqwftvncz"))
    verify(23, solver.part2("nppdvjthqldpwncqszvftbrmjlhg"))
    verify(29, solver.part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"))
    verify(26, solver.part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}