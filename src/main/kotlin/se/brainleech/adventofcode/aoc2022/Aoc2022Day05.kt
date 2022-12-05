package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.aoc2022.Aoc2022Day05.CraneModel.CRATE_MOVER_9000
import se.brainleech.adventofcode.aoc2022.Aoc2022Day05.CraneModel.CRATE_MOVER_9001
import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day05 {

    enum class CraneModel {
        CRATE_MOVER_9000,
        CRATE_MOVER_9001
    }

    data class Action(val quantity: Int, val from: Int, val to: Int) {
        fun applyOn(stacks: MutableMap<Int, String>, craneModel: CraneModel): MutableMap<Int, String> {
            val steps = if (craneModel == CRATE_MOVER_9001) 1 else quantity
            val perStep = if (craneModel == CRATE_MOVER_9001) quantity else 1
            for (step in 1..steps) {
                stacks[to] = stacks[from]!!.take(perStep) + stacks[to]
                stacks[from] = stacks[from]!!.drop(perStep)
            }
            return stacks
        }
    }

    private fun String.asRowOfCrates() : MutableMap<Int, String> {
        val crates = mutableMapOf<Int, String>()
        this
            .trimEnd().plus(" ")
            .replace("] ", ",")
            .replace("[", "")
            .replace("    ", ",") // mind the gap :)
            .trimEnd(',')
            .split(",")
            .forEachIndexed { index, crate -> if (crate.isNotBlank()) crates[index + 1] = crate }
        return crates
    }

    private fun String.asAction() : Action {
        val operands = this.replace(Regex("(move |from |to )"), "")
        val (quantity, from, to) = operands.split(" ").map { it.toInt() }
        return Action(quantity, from, to)
    }

    private fun List<String>.toStacks(): MutableMap<Int, String> {
        return this
            .filter { it.contains("[") }
            .flatMap { it.asRowOfCrates().asSequence() }
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.joinToString("") }
            .toSortedMap()
            .toMutableMap()
    }

    private fun List<String>.toRules(): List<Action> {
        return this
            .filter { it.contains("move") }
            .map { it.asAction() }
    }

    private fun process(input: List<String>, craneModel: CraneModel): String {
        val stacks = input.toStacks()

        input.toRules().forEach { it.applyOn(stacks, craneModel) }

        return stacks.values
            .map { it.first() }
            .joinToString("")
    }

    fun part1(input: List<String>) : String { return process(input, CRATE_MOVER_9000) }

    fun part2(input: List<String>) : String { return process(input, CRATE_MOVER_9001) }

}

fun main() {
    val solver = Aoc2022Day05()
    val prefix = "aoc2022/aoc2022day05"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify("CMZ", solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify("MCD", solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}