package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2021Day05 {

    companion object {
        private const val POINT_SEPARATOR = " -> "
        private const val NUMBER_SEPARATOR = ","
    }

    data class Point(val x: Int, val y: Int) : Comparable<Point> {
        override operator fun compareTo(other: Point): Int {
            return when {
                this === other -> 0
                this.x > other.x -> 1
                this.x < other.x -> -1
                this.y > other.y -> 1
                this.y < other.y -> -1
                else -> 0
            }
        }

        override fun toString(): String {
            return "($x, $y)"
        }
    }

    data class Space(var points: MutableMap<Point, Int> = mutableMapOf()) {
        private fun addPoint(point: Point) {
            points.merge(point, 1) { a, b -> a + b }
        }

        fun addPoints(newPoints: List<Point>) {
            newPoints.forEach { point -> addPoint(point) }
        }

        fun countDangerousPoints(): Int {
            return points.count { it.value > 1 }
        }
    }

    data class Line(val from: Point, val to: Point) {
        fun isStraight(): Boolean {
            return (from.x == to.x || from.y == to.y)
        }

        fun toPoints(): List<Point> {
            val points: MutableSet<Point> = mutableSetOf(from, to)
            if (from.x == to.x) {
                for (y in from.y until to.y) {
                    points.add(Point(from.x, y))
                }
            } else if (from.y == to.y) {
                for (x in from.x until to.x) {
                    points.add(Point(x, from.y))
                }
            } else {
                // 45 degrees
                var x = from.x
                var y = from.y
                val dx = if (from.x < to.x) 1 else -1
                val dy = if (from.y < to.y) 1 else -1
                while (x != to.x) {
                    points.add(Point(x, y))
                    x += dx
                    y += dy
                }
            }
            return points.toSortedSet().toList()
        }

        override fun toString(): String {
            return "Line{$from - $to}"
        }
    }

    private fun String.asLine(): Line {
        val (x1, y1, x2, y2) = this
            .replace(POINT_SEPARATOR, NUMBER_SEPARATOR)
            .split(Regex(NUMBER_SEPARATOR))
            .map { it.toInt() }
        val points = listOf(Point(x1, y1), Point(x2, y2)).sorted()
        return Line(points.first(), points.last())
    }

    fun part1(input: List<String>): Int {
        val space = Space()
        input
            .map { it.asLine() }
            .filter { it.isStraight() }
            .map { it.toPoints() }
            .forEach { space.addPoints(it) }

        return space.countDangerousPoints()
    }

    fun part2(input: List<String>): Int {
        val space = Space()
        input
            .map { it.asLine() }
            .map { it.toPoints() }
            .forEach { space.addPoints(it) }

        return space.countDangerousPoints()
    }

}

fun main() {
    val solver = Aoc2021Day05()
    val prefix = "aoc2021/aoc2021day05"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(5, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(12, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}