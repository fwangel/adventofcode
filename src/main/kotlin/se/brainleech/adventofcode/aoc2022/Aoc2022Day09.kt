package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class Aoc2022Day09 {

    companion object {
        const val DEBUG = false
        const val TRACE = false

        const val UP    = "U"
        const val RIGHT = "R"
        const val DOWN  = "D"
        const val LEFT  = "L"

        fun List<Position>.offset(dx: Int, dy: Int) : List<Position> {
            return this.map { Position(it.x + dx, it.y + dy) }.toList()
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }
    }

    data class Rule(val direction: String, val steps: Int) {
        companion object {
            fun parse(instruction: String) : Rule {
                val (direction, steps) = instruction.split(" ")
                return Rule(direction, steps.toInt())
            }
        }
    }

    data class Position(var x: Int, var y: Int) {

        fun move(direction: String) : Position {
            when (direction) {
                UP    -> y --
                RIGHT -> x ++
                DOWN  -> y ++
                LEFT  -> x --
            }
            return this
        }

        private val distance: Int get() = (x * x) + (y * y)

        fun follow(leader: Position) : Position {
            val offset = Position(leader.x - x, leader.y - y)
            if (offset.distance > 2) {
                x += offset.x.sign
                y += offset.y.sign
            }
            return this
        }

        fun cloned() : Position {
            return Position(x, y)
        }

        override fun toString(): String {
            return "(x:${x}, y:${y})"
        }
    }

    data class KnotsSpace(private val ropeLength: Int) {

        private val rope: MutableList<Position> = mutableListOf()
        private val tailVisited: LinkedHashSet<Position> = linkedSetOf()

        init {
            repeat(ropeLength) { rope.add(Position(0,0)) }
            tailVisited.add(rope.last().cloned())
        }

        fun process(input: List<String>) : Int {
            if (input.isEmpty()) return -1

            input.forEach { instruction -> move(Rule.parse(instruction)) }

            return tailVisits().count()
        }

        private fun move(rule: Rule) {
            repeat(rule.steps) {
                rope.first().move(rule.direction)
                rope.drop(1).forEachIndexed { index, knot ->
                    knot.follow(rope[index])
                }
                tailVisited.add(rope.last().cloned())
            }

            trace { println("$this\n") }
        }

        private fun tailVisits(): List<Position> {
            return tailVisited.toList()
                .debug { println(this); println("Locations visited by the tail: ${tailVisited.count()}\n") }
        }

        override fun toString(): String {
            val minX = min(tailVisited.minOf { it.x }, rope.minOf { it.x })
            val minY = min(-5, min(tailVisited.minOf { it.y }, rope.minOf { it.y }))
            val visited = tailVisited.map { it.cloned() }.toList().offset(-minX, -minY)
            val knots = rope.map { it.cloned() }.toList().offset(-minX, -minY)

            val width  = max(10, max(visited.maxOf { it.x }, knots.maxOf { it.x })).plus(1)
            val height = max(visited.maxOf { it.y }, knots.maxOf { it.y }).plus(1)
            val world  = ".".repeat(width * height).toCharArray()
            visited.onEach {
                world[(width * it.y) + it.x] = '#'
            }
            knots.drop(1).onEachIndexed { index, it ->
                world[(width * it.y) + it.x] = (index + 1).digitToChar()
            }
            world[(width * knots.last().y) + knots.last().x] = 'T'
            world[(width * knots.first().y) + knots.first().x] = 'H'
            world[(width * visited.first().y) + visited.first().x] = 's'

            return world.asSequence().chunked(width).map { it.joinToString("") }.joinToString("\n")
        }
    }

    fun part1(input: List<String>) : Int {
        return KnotsSpace(2).process(input)
    }

    fun part2(input: List<String>) : Int {
        return KnotsSpace(10).process(input)
    }

}

fun main() {
    val solver = Aoc2022Day09()
    val prefix = "aoc2022/aoc2022day09"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(13, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(1, solver.part2(testData))
    verify(9, solver.part2(listOf("R 10", "U 10", "L 10", "D 10")))
    verify(36, solver.part2(listOf("R 5", "U 8", "L 8", "D 3", "R 17", "D 10", "L 25", "U 20")))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}