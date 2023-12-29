package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.Location
import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2023Day03 {

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

    private fun Char.isVoid() = this == '.'
    private fun Char.isGear() = this == '*'
    private fun Char.isPotentialPartNumber() = this.isDigit()
    private fun Char.isEnginePart() = !(this.isPotentialPartNumber() || this.isVoid())
    private fun String.toPartNumberAt(index: Int) = this.substring(index).takeWhile { it.isPotentialPartNumber() }.toInt()

    data class NumberOrSymbol(val symbol: Char? = null, val number: Int? = null, var adjacentLocations: List<Location>? = null, var connectedNumberLocations: MutableSet<Location>? = null) {
        val isPartNumber = number != null
        val isEnginePart = symbol != null
        val isGear = symbol != null && symbol == '*'
    }

    private fun List<String>.toSchematics(): Map<Location, NumberOrSymbol> {
        val schematics = mutableMapOf<Location, NumberOrSymbol>()
        val width = this.first().length
        val height = this.size
        this.forEachIndexed { y, line ->
            var skip = 0
            line.forEachIndexed { x, ch ->
                if (skip > 0) {
                    skip--
                } else {
                    val location = Location(x, y)
                    if (ch.isGear()) {
                        schematics[location] = NumberOrSymbol(symbol = ch, connectedNumberLocations = mutableSetOf())
                    } else if (ch.isEnginePart()) {
                        schematics[location] = NumberOrSymbol(symbol = ch)
                    } else if (ch.isPotentialPartNumber()) {
                        val number = line.toPartNumberAt(x)
                        val numberLength = "$number".length - 1
                        val adjacentLocations = mutableSetOf<Location>()
                        var digitLocation = location
                        for (i in 0..numberLength) {
                            adjacentLocations.addAll(digitLocation.neighboursAndDiagonals()
                                .filter { it.inside(width, height) }
                                .filterNot { it.y == y && it.x in x..x + numberLength }
                            )
                            digitLocation = digitLocation.right()
                        }
                        schematics[location] = NumberOrSymbol(number = number, adjacentLocations = adjacentLocations.toList())
                        skip = numberLength
                    }
                }
            }
        }

        val gearLocations = schematics.asSequence().filter { it.value.isGear }.map { it.key }.toList()
        schematics.filter { it.value.isPartNumber }.forEach {
            it.value.adjacentLocations!!
                .filter { loc -> gearLocations.contains(loc) }
                .forEach { loc ->
                    schematics[loc]!!.connectedNumberLocations!!.add(it.key)
                }
        }

        return schematics
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        val schematics = input
            .filter { it.isNotEmpty() }
            .toSchematics()

        return schematics
            .asSequence()
            .filter { it.value.isPartNumber }
            .filter { entry -> entry.value.adjacentLocations!!.filter { schematics.contains(it) }.map { schematics[it]!!.isEnginePart }.any() }
            .sumOf { it.value.number!! }
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        val schematics = input
            .filter { it.isNotEmpty() }
            .toSchematics()

        return schematics
            .asSequence()
            .filter { it.value.isGear }
            .map { it.value.connectedNumberLocations!!.toList() }
            .filter { it.size == 2 }
            .sumOf { locations ->
                val first = schematics[locations.first()]!!.number!!.toLong()
                val second = schematics[locations.last()]!!.number!!.toLong()
                first.times(second)
            }
    }

}

fun main() {
    val solver = Aoc2023Day03()
    val prefix = "aoc2023/aoc2023day03"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(4361, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(467835, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}