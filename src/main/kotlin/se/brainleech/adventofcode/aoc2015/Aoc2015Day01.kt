package se.brainleech.adventofcode.aoc2015

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.verify

class Aoc2015Day01 {

    fun part1(input: String): Int {
        var floor = 0
        input.onEach {
            when (it) {
                '(' -> floor++
                ')' -> floor--
            }
        }
        return floor
    }

    fun part2(input: String): Int {
        var floor = 0
        input.onEachIndexed { index, char ->
            when (char) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor == -1) {
                return index + 1
            }
        }
        return 0
    }

}

fun main() {
    val solver = Aoc2015Day01()
    val prefix = "aoc2015/aoc2015day01"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(-1, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(5, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}