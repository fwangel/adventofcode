package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2021Day02 {

    companion object {
        private const val COMMAND_DATA_SEPARATOR = " "
    }

    data class Command(val direction: String, val amount: Long)

    class Position(var horizontal: Long = 0, var depth: Long = 0) {
        private var aim: Long = 0

        fun apply(command: Command) {
            when (command.direction) {
                "up" -> this.depth -= command.amount
                "down" -> this.depth += command.amount
                "forward" -> this.horizontal += command.amount
            }
        }

        fun applyWithAim(command: Command) {
            when (command.direction) {
                "up" -> this.aim -= command.amount
                "down" -> this.aim += command.amount
                "forward" -> {
                    this.horizontal += command.amount
                    this.depth += this.aim * command.amount
                }
            }
        }
    }

    private fun String.asCommand(): Command {
        val (direction, amount) = this.split(COMMAND_DATA_SEPARATOR)
        return Command(direction, amount.toLong())
    }

    fun part1(input: List<String>): Long {
        val p = Position()
        input.forEach { p.apply(it.asCommand()) }
        return p.horizontal * p.depth
    }

    fun part2(input: List<String>): Long {
        val p = Position()
        input.forEach { p.applyWithAim(it.asCommand()) }
        return p.horizontal * p.depth
    }

}

fun main() {
    val solver = Aoc2021Day02()
    val prefix = "aoc2021/aoc2021day02"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(150L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(900L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}
