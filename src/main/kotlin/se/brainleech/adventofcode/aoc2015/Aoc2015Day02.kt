package se.brainleech.adventofcode.aoc2015

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.verify

class Aoc2015Day02 {

    data class Box(val l: Long, val w: Long, val h: Long) {
        private fun surfaceArea(): Long = 0
            .plus(2.times(l).times(w))
            .plus(2.times(w).times(h))
            .plus(2.times(h).times(l))

        private fun smallestArea(): Long = minOf(
            l.times(w),
            w.times(h),
            h.times(l)
        )

        fun wrappingPaperArea(): Long = surfaceArea().plus(smallestArea())

        private fun ribbonBowLength() = 1
            .times(l)
            .times(w)
            .times(h)

        private fun shortestDistance(): Long {
            val first = minOf(l, w, h)
            val second = listOf(l, w, h).sorted().drop(1).first()
            return 0.plus(first).plus(first).plus(second).plus(second)
        }

        fun ribbonLength(): Long {
            return shortestDistance().plus(ribbonBowLength())
        }

    }

    private fun String.asBox(): Box {
        val (l, w, h) = this.split("x").map { it.toLong() }
        return Box(l, w, h)
    }

    fun part1(input: String): Long {
        return input.split("\n")
            .sumOf { it.asBox().wrappingPaperArea() }
    }

    fun part2(input: String): Long {
        return input.split("\n")
            .sumOf { it.asBox().ribbonLength() }
    }

}

fun main() {
    val solver = Aoc2015Day02()
    val prefix = "aoc2015/aoc2015day02"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(101L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(48L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}