package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.toListOfLongs
import se.brainleech.adventofcode.verify

class Aoc2023Day06 {

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

    private data class Race(val raceTimeLimit: Long, val recordDistance: Long) {
        fun distance(boostTime: Long): Long {
            if (boostTime >= raceTimeLimit) return 0
            return boostTime * (raceTimeLimit - boostTime)
        }

        fun numberOfWinningWays(): Int {
            return (1 until raceTimeLimit)
                .map { distance(it) }
                .count { dist -> dist > recordDistance }
                .debug { println("Race: $this => Winning ways: $it") }
        }
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty() || input.size < 2) return -1
        val raceTimes = input[0].replace("Time:",     "").trim().replace(" +".toRegex(), ",").toListOfLongs()
        val records   = input[1].replace("Distance:", "").trim().replace(" +".toRegex(), ",").toListOfLongs()
        val races = raceTimes.zip(records).map { (time, dist) -> Race(time, dist) }
        return races
            .map { race -> race.numberOfWinningWays().toLong() }
            .reduce { total, value -> total.times(value) }
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty() || input.size < 2) return -1
        val raceTime = input[0].replace("Time:",     "").trim().replace(" ", "").toLong()
        val record   = input[1].replace("Distance:", "").trim().replace(" ", "").toLong()
        return Race(raceTime, record).numberOfWinningWays().toLong()
    }

}

fun main() {
    val solver = Aoc2023Day06()
    val prefix = "aoc2023/aoc2023day06"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(288L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(71503L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}