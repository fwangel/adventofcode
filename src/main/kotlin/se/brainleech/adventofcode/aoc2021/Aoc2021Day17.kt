package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.verify
import kotlin.math.abs
import kotlin.math.sign

class Aoc2021Day17 {

    companion object {
        private const val debug = false

        private const val PARSE_EXPRESSION = "target area: x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)"
    }

    data class Area(val xAxis: IntRange, val yAxis: IntRange)

    data class Trajectory(
        var x: Int = 0,
        var y: Int = 0,
        var dx: Int = 0,
        var dy: Int = 0
    ) {
        var steps: Int = 0
        var maxY: Int = Int.MIN_VALUE

        fun step(): Trajectory {
            x += dx
            y += dy
            dx += -dx.sign
            dy -= 1
            maxY = maxOf(maxY, y)
            steps++
            return this
        }

        fun isSuccess(targetArea: Area): Boolean = targetArea.xAxis.contains(x) && targetArea.yAxis.contains(y)
    }

    data class Solution(val dx: Int, val dy: Int, val maxY: Int, val steps: Int)

    data class Result(
        val solutions: List<Solution>,
        val maxY: Int,
        val minDx: Int,
        val maxDx: Int,
        val minDy: Int,
        val maxDy: Int,
        val maxSteps: Int
    )

    data class Probe(val targetArea: Area) {
        private fun List<Solution>.asResult(): Result {
            val maxY = this.maxOf { it.maxY }
            val minDx = this.minOf { it.dx }
            val maxDx = this.maxOf { it.dx }
            val minDy = this.minOf { it.dy }
            val maxDy = this.maxOf { it.dy }
            val maxSteps = this.maxOf { it.steps }
            if (debug) println("Found ${this.size} solution(s):\n  $this")
            if (debug) println("  max y = $maxY, min dx=$minDx, max dx=$maxDx, min dy=$minDy, max dy=$maxDy, max steps=$maxSteps")
            return Result(this, maxY, minDx, maxDx, minDy, maxDy, maxSteps)
        }

        fun solve(): Result {
            if (debug) println("Target area=$targetArea")
            val validSolutions = mutableListOf<Solution>()
            for (dx in 0..targetArea.xAxis.last) {
                for (dy in -abs(targetArea.yAxis.first * 2)..abs(targetArea.yAxis.last * 2)) {
                    val trajectory = Trajectory(0, 0, dx, dy)
                    for (attempt in 1..500) {
                        trajectory.step()
                        if (trajectory.isSuccess(targetArea)) {
                            validSolutions.add(Solution(dx, dy, trajectory.maxY, trajectory.steps))
                            break
                        }
                    }
                }
            }

            return validSolutions.asResult()
        }
    }

    private fun IntRange.toSortedRange(): IntRange {
        return minOf(this.first, this.last)..maxOf(this.first, this.last)
    }

    private fun String.toProbe(): Probe {
        val (x1, x2, y1, y2) = Regex(PARSE_EXPRESSION).find(this)!!.destructured.toList().map { it.toInt() }
        return Probe(targetArea = Area((x1..x2).toSortedRange(), (y1..y2).toSortedRange()))
    }

    fun part1(input: String): Int {
        return input.toProbe().solve().maxY
    }

    fun part2(input: String): Int {
        return input.toProbe().solve().solutions.size
    }

}

fun main() {
    val solver = Aoc2021Day17()
    val prefix = "aoc2021/aoc2021day17"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(45, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(112, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}