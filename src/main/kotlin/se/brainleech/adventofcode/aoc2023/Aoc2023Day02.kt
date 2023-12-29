package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2023Day02 {

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

    data class Cube(val r: Int, val g: Int, val b: Int) {
        val power = this.r * this.g * this.b
    }

    data class Game(val id: Int, val cubes: List<Cube>) {
        fun allowedBy(rule: Cube) = cubes.all { it.r <= rule.r && it.g <= rule.g && it.b <= rule.b }
        fun maxCube() = Cube(cubes.maxOf { it.r }, cubes.maxOf { it.g }, cubes.maxOf { it.b })
    }

    private fun String.toCube(): Cube {
        val m = """(?<level>\d+) (?<component>([rgb]))""".toRegex().findAll(this)
        var r = 0; var g = 0; var b = 0
        m.forEach {
            val level = it.groups["level"]!!.value.toInt()
            val component = it.groups["component"]!!.value
            when (component) { "r" -> r = level; "g" -> g = level; "b" -> b = level }
        }
        return Cube(r, g, b)
    }

    private fun String.toCubes(): List<Cube> {
        return this.split(";").map { it.trim().toCube() }.toList()
    }

    private fun String.toGame(): Game {
        val m = """^Game (?<id>\d+): (?<rawCubes>.*)$""".toRegex().matchEntire(this)!!
        val id = m.groups["id"]!!.value.toInt()
        val cubes = m.groups["rawCubes"]!!.value.toCubes()
        return Game(id, cubes)
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toGame() }
            .filter { it.allowedBy(Cube(12, 13, 14)) }
            .sumOf { it.id }
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return input
            .asSequence()
            .filter { it.isNotEmpty() }
            .map { it.toGame() }
            .map { it.maxCube() }
            .sumOf { it.power }
    }

}

fun main() {
    val solver = Aoc2023Day02()
    val prefix = "aoc2023/aoc2023day02"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(8, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(2286, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}