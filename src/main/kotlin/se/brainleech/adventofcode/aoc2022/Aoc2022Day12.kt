package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

typealias Cost = Int

class Aoc2022Day12 {

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

    data class Position(val y: Int, val x: Int) {
        fun up(): Position = Position(y - 1, x)
        fun right(): Position = Position(y, x + 1)
        fun down(): Position = Position(y + 1, x)
        fun left(): Position = Position(y, x - 1)
        override fun toString() = "($x,$y)"
    }

    data class Step(val position: Position, val cost: Cost = 0, val parent: Step? = null) {
        val dir: Char get() {
            if (parent == null) return 'S'
            if (parent.position.y < position.y) return '↓'
            if (parent.position.x < position.x) return '→'
            if (parent.position.x > position.x) return '←'
            if (parent.position.y > position.y) return '↑'
            return '.'
        }
        fun toPath(): Path {
            var step = this
            val path = Path(mutableListOf(step))
            while (step.parent != null) {
                step = step.parent!!
                path.add(0, step)
            }
            return path
        }
        override fun toString() = "$dir(${position.x},${position.y})"
    }

    data class Path(val steps: MutableList<Step> = mutableListOf()) {
        private var completed: Boolean = false
        fun add(step: Step) = steps.add(step)
        fun add(index: Int, step: Step) = steps.add(index, step)
        fun asCompleted(): Path = this.apply{ completed = true }
        fun isCompleted() = completed
        fun cost(): Cost = steps.size - 1
        override fun toString() = "completed=${completed}, cost=${cost()}, steps=$steps"
    }

    data class Hill(
        val start: Position,
        val goal: Position,
        private var elevations: List<List<Int>>
    ) {
        private val height = elevations.size
        private val width = elevations.first().size
        private var bestCost = Cost.MAX_VALUE
        private var bestPath = Path()

        private fun outsideMap(pos: Position) = (pos.y < 0 || pos.y >= height || pos.x < 0 || pos.x >= width)
        private fun heightAt(pos: Position): Int = if (outsideMap(pos)) -1 else elevations[pos.y][pos.x]

        private fun canMoveBetween(source: Position, target: Position): Boolean {
            if (outsideMap(target)) return false
            val sourceHeight = heightAt(source)
            val targetHeight = heightAt(target)
            return targetHeight <= sourceHeight + 1
        }

        private fun lowPoints(): List<Position> {
            return elevations.flatMapIndexed { y, line ->
                line.mapIndexed { x, elevation -> if (elevation == 0) Position(y, x) else null }
            }.filterNotNull()
        }

        private fun validPositionsFrom(pos: Position): List<Position> {
            val targets = listOf(pos.down(), pos.right(), pos.up(), pos.left())
            return targets.filter { target -> canMoveBetween(pos, target) }
        }

        fun findFastestPathFromAnyLowPoint(to: Position): Path {
            lowPoints().onEach { start ->
                findFastestPathBetween(start, to)
            }
            return bestPath
        }

        fun findFastestPathBetween(from: Position, to: Position): Path {
            val explored = mutableSetOf(from)
            val stepsToExplore = arrayListOf(Step(from))
            while (stepsToExplore.isNotEmpty()) {
                val step = stepsToExplore.first()
                val currentCost = step.cost
                if (step.position == to) {
                    val path = step.toPath().asCompleted()
                    if (step.cost < bestCost) {
                        bestPath = path
                        bestCost = step.cost
                        trace { println("Found a path with cost ${step.cost}") }
                    }
                    return path
                }
                stepsToExplore.removeFirst()
                validPositionsFrom(step.position).filterNot { it in explored }.onEach { nextPosition ->
                    explored.add(nextPosition)
                    stepsToExplore.add(Step(nextPosition, currentCost + 1, step))
                }
            }

            return Path()
        }

        override fun toString(): String {
            val prefix = "=== MAP ===\n"
            val done = bestPath.isCompleted()
            val flatMap = elevations.joinToString("") { row ->
                row.map { elevation -> if (done) '.' else ('a'.code + elevation).toChar() }.joinToString("")
            }.toCharArray()
            flatMap[(start.y * width) + start.x] = 'S'
            flatMap[(goal.y * width) + goal.x] = 'E'

            if (done) {
                val path = bestPath.steps
                path.onEachIndexed { index, step ->
                    val pos = step.position
                    // shift the direction indicators to show path
                    flatMap[(pos.y * width) + pos.x] = if (index == path.size - 1) 'E' else path[index + 1].dir
                }
            }

            return prefix + flatMap.joinToString("").chunked(width).joinToString("\n")
        }
    }

    private val Char.toElevation: Int get() = this.code - 'a'.code

    private fun List<String>.asHill(): Hill {
        var start = Position(0, 0)
        var goal  = Position(0, 0)
        val elevations = this.mapIndexed { y, line ->
            if (line.contains('S')) { start = Position(y, line.indexOf('S')) }
            if (line.contains('E')) { goal = Position(y, line.indexOf('E')) }
            line.toCharArray().map { char ->
                when (char) {
                    'S' -> 'a'.toElevation
                    'E' -> 'z'.toElevation
                    in 'a'..'z' -> char.toElevation
                    else -> error("Bad input.")
                }
            }
        }

        return Hill(start, goal, elevations)
    }

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1

        val map = input.asHill()
            .debug { println(it) }
        val bestPath = map.findFastestPathBetween(map.start, map.goal)
            .debug { println("Best path: $it") }
        return bestPath.cost()
            .debug { println(map) }
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1

        val map = input.asHill()
            .debug { println(it) }
        val bestPath = map.findFastestPathFromAnyLowPoint(map.goal)
            .debug { println("Best path: $it") }
        return bestPath.cost()
            .debug { println(map) }
    }

}

fun main() {
    val solver = Aoc2022Day12()
    val prefix = "aoc2022/aoc2022day12"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(31, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(29, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}