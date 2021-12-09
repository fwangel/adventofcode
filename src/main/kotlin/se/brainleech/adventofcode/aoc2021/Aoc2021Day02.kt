package se.brainleech.adventofcode.aoc2021

import java.util.stream.Stream

class Aoc2021Day02 {

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
        val (direction, amount) = this.split(" ")
        return Command(direction, amount.toLong())
    }

    fun part1(input: Stream<String>): Long {
        val p = Position()
        input.forEach { p.apply(it.asCommand()) }
        return p.horizontal * p.depth
    }

    fun part2(input: Stream<String>): Long {
        val p = Position()
        input.forEach { p.applyWithAim(it.asCommand()) }
        return p.horizontal * p.depth
    }

}
