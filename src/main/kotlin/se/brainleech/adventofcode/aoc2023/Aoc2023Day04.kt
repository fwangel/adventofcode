package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.toListOfInts
import se.brainleech.adventofcode.verify
import kotlin.math.min

class Aoc2023Day04 {

    companion object {
        const val DEBUG = false
        const val TRACE = false

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }
    }

    private data class ScratchCard(val id: Int, val winningNumbers: List<Int>, val scratchedNumbers: List<Int>) {
        val score = scratchedNumbers
            .asSequence()
            .filter { winningNumbers.contains(it) }
            .mapIndexed { index, _ -> min(2, index + 1) }
            .reduceOrNull { total, point -> total * point }

        val numberOfMatches = scratchedNumbers.asSequence().filter { winningNumbers.contains(it) }.count()
    }

    private fun String.toScratchCard(): ScratchCard {
        val m = """^Card +(?<id>\d+): +(?<winningNumbers>.*) +\| +(?<scratchedNumbers>.*)$""".toRegex().matchEntire(this)!!
        val id = m.groups["id"]!!.value.toInt()
        val winningNumbers = m.groups["winningNumbers"]!!.value.replace(" +".toRegex(), ",").toListOfInts()
        val scratchedNumbers = m.groups["scratchedNumbers"]!!.value.replace(" +".toRegex(), ",").toListOfInts()
        return ScratchCard(id, winningNumbers, scratchedNumbers)
    }

    private fun process(cardIds: List<Int>, wonCardIdsById: Map<Int, IntRange>, countById: MutableMap<Int, Int>): List<Int> {
        return cardIds
            .filter { id -> wonCardIdsById.contains(id) }
            .onEach { id ->
                val copies = wonCardIdsById[id]!!.toList()
                process(copies, wonCardIdsById, countById)
                copies.forEach { countById[it] = countById[it]!!.inc() }
            }
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toScratchCard() }
            .sumOf { it.score ?: 0 }
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        val allCards = input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toScratchCard() }
            .toList()

        val wonCardIdsById = allCards
            .asSequence()
            .filter { it.numberOfMatches > 0 }
            .map { it.id to IntRange(it.id + 1, it.id + 1 + it.numberOfMatches - 1) }
            .toMap()

        val countById = allCards.map { it.id }.associateWith { 1 }.toMutableMap()
        process(wonCardIdsById.keys.toList(), wonCardIdsById, countById)
        return countById.values.sum()
    }

}

fun main() {
    val solver = Aoc2023Day04()
    val prefix = "aoc2023/aoc2023day04"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(13, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(30, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}