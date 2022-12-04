package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day03 {

    private fun String.firstHalf() = this.substring(0, (this.length / 2))
    private fun String.secondHalf() = this.substring(this.length / 2)

    private fun String.prioritiesBy(second: String, third: String = "") : Int {
        val secondChars = second.toCharArray()
        val thirdChars = third.toCharArray()
        return this.toCharArray()
            .filter { ch -> secondChars.contains(ch) && (thirdChars.isEmpty() || thirdChars.contains(ch)) }
            .map { ch ->
                if (ch in 'A'..'Z') ch - 'A' + 27
                else ch - 'a' + 1
            }
            .distinct()
            .sum()
    }

    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.firstHalf().prioritiesBy(it.secondHalf()) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .chunked(size = 3)
            .map { (first, second, third) -> first.prioritiesBy(second, third) }
            .asSequence()
            .sum()
    }

}

fun main() {
    val solver = Aoc2022Day03()
    val prefix = "aoc2022/aoc2022day03"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(157, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(70, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}