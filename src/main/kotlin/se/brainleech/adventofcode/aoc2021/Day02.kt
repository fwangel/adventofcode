package se.brainleech.adventofcode.aoc2021

import java.util.stream.Stream

class Day02 {

    data class Command(val direction: String, val amount: Int)

    class Position(var horizontal: Int = 0, var depth: Int = 0, private var aim: Int = 0) {
        fun apply(command: Command): Position {
            when (command.direction) {
                "up" -> this.depth -= command.amount
                "down" -> this.depth += command.amount
                "forward" -> this.horizontal += command.amount
            }
            return this
        }

        fun applyWithAim(command: Command): Position {
            when (command.direction) {
                "up" -> this.aim -= command.amount
                "down" -> this.aim += command.amount
                "forward" -> {
                    this.horizontal += command.amount
                    this.depth += this.aim * command.amount
                }
            }
            return this
        }
    }

    fun part1(input: Stream<String>): Int {
        val p = Position()
        input
            .map { it.split(' ') }
            .map { Command(it[0], it[1].toInt()) }
            .forEach { p.apply(it) }
        return p.horizontal * p.depth
    }

    fun part2(input: Stream<String>): Int {
        val p = Position()
        input
            .map { it.split(' ') }
            .map { Command(it[0], it[1].toInt()) }
            .forEach { p.applyWithAim(it) }
        return p.horizontal * p.depth
    }

}
