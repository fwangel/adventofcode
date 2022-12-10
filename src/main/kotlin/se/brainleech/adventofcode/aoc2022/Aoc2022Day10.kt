package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day10 {

    companion object {
        const val DEBUG = true
        const val TRACE = true

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }
    }

    data class Cpu(private var x: Int = 1) {
        private var cycle = 1
        private var strength = 0
        private var justMeasured = false

        private fun then(block: () -> Unit) : Cpu {
            block()
            return this
        }

        private fun step() : Cpu {
            justMeasured = false
            cycle++
            if (cycle == 20 || ((cycle - 20) % 40 == 0)) {
                strength += cycle.times(x)
                justMeasured = true
            }
            return this
        }

        fun process(instruction: String, argument: String) {
            trace { println("cycle:$cycle, $instruction $argument, current x: $x") }

            val strengthBefore = strength
            when (instruction) {
                "addx" -> step().then { x += argument.toInt() }.then { step() }
                "noop" -> step()
            }
            if (justMeasured) {
                debug { println("cycle:$cycle, $instruction $argument => x: $x, sig:$strength (+${strength - strengthBefore})") }
            }
        }

        fun signalStrength() = strength
    }

    fun part1(input: List<String>) : Int {
        val cpu = Cpu(x = 1)
        input.forEach {
            val instruction = it.substringBefore(' ')
            val argument = it.substringAfter(' ', "")
            cpu.process(instruction, argument)
        }
        return cpu.signalStrength()
    }

    fun part2(input: List<String>) : Int {
        return input
            .count()
    }

}

fun main() {
    val solver = Aoc2022Day10()
    val prefix = "aoc2022/aoc2022day10"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(0, solver.part1(listOf("noop", "addx 3", "addx -5")))
    verify(13140, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

//    verify(-1, solver.part2(testData))
//    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}