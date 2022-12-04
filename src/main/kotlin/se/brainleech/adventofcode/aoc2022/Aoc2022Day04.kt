package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day04 {

    private fun String.idleElves(intersects: Boolean = false): Int {
        return this.split(",")
            .asSequence()
            .chunked(2)
            .map { (elf, buddy) ->
                val (elfFirst, elfLast) = elf.split("-").map { it.toInt() }
                val (buddyFirst, buddyLast) = buddy.split("-").map { it.toInt() }

                if (elfFirst <= buddyFirst && elfLast >= buddyLast) 1
                else if (buddyFirst <= elfFirst && buddyLast >= elfLast) 1
                else if (intersects && (elfFirst in buddyFirst..buddyLast)) 1
                else if (intersects && (elfLast in buddyFirst..buddyLast)) 1
                else 0
            }
            .sum()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { it.idleElves() }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { it.idleElves(true) }
    }

}

fun main() {
    val solver = Aoc2022Day04()
    val prefix = "aoc2022/aoc2022day04"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(2, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(4, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}