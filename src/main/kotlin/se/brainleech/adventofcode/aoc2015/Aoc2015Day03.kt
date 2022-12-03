package se.brainleech.adventofcode.aoc2015

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.verify

class Aoc2015Day03 {
    data class Position(val x: Int, val y: Int) {
        fun move(direction: Char): Position {
            return when (direction) {
                '<' -> Position(x - 1, y)
                '>' -> Position(x + 1, y)
                '^' -> Position(x, y - 1)
                'v' -> Position(x, y + 1)
                else -> this
            }
        }
    }

    fun part1(input: String): Int {
        var position = Position(0, 0)
        val visited = mutableMapOf(position to 1)
        input.onEach { direction ->
            position = position.move(direction)
            visited.merge(position, 1) { a, b -> a + b }
        }
        return visited.values.count()
    }

    fun part2(input: String): Int {
        var santaPosition = Position(0, 0)
        var robotPosition = Position(0, 0)
        val visited = mutableMapOf(santaPosition to 2)
        input.onEachIndexed { index, direction ->
            if (index.mod(2) == 0) {
                santaPosition = santaPosition.move(direction)
                visited.merge(santaPosition, 1) { a, b -> a + b }
            } else {
                robotPosition = robotPosition.move(direction)
                visited.merge(robotPosition, 1) { a, b -> a + b }
            }
        }
        return visited.values.count()
    }

}

fun main() {
    val solver = Aoc2015Day03()
    val prefix = "aoc2015/aoc2015day03"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(4, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(3, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}