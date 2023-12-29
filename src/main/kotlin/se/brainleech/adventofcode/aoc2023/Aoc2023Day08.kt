package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.*

class Aoc2023Day08 {

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

    private data class Crossroad(val id: String, val left: String, val right: String) {
        private fun toStartNode() = id.endsWith('A')
        private fun toEndNode() = id.endsWith('Z')
        val startNode = toStartNode()
        val endNode = toEndNode()
        override fun toString(): String {
            return "{$id = ($left, $right)}"
        }
    }

    private fun String.toNodeMapEntry(): Pair<String, Crossroad> {
        val parts = this.replace("[ ()]".toRegex(), "").replace("=",",").split(",")
        val id = parts[0]
        val left = parts[1]
        val right = parts[2]
        return Pair(id, Crossroad(id, left, right))
    }

    private fun Map<String, Crossroad>.stepsToZzz(path: String): Long {
        var steps = 0L
        var current = this["AAA"]
        var pathIndex = 0
        while (current!!.id != "ZZZ") {
            steps++
            current = when (path[pathIndex]) {
                'L' -> this[current.left]
                'R' -> this[current.right]
                else -> throw IllegalStateException("What is ${path[pathIndex]}??")
            }
            pathIndex = (pathIndex + 1) % path.length
        }
        return steps
    }

    private fun Map<String, Crossroad>.stepsToAnyEndNode(path: String, startNode: Crossroad): Long {
        var steps = 0L
        var current = startNode
        var pathIndex = 0
        while (!current.endNode) {
            steps++
            current = when (path[pathIndex]) {
                'L' -> this[current.left]!!
                'R' -> this[current.right]!!
                else -> throw IllegalStateException("What is ${path[pathIndex]}??")
            }
            pathIndex = (pathIndex + 1) % path.length
        }
        return steps
    }

    private fun Map<String, Crossroad>.stepsToAllEndNodes(path: String): Long {
        val currentNodes = this.values.filter { it.startNode }.toList()
        debug { println("Starting with: $currentNodes") }

        val stepsByStartNode = currentNodes.associateWith { current ->
            this.stepsToAnyEndNode(path, current)
        }
        debug { println(stepsByStartNode) }
        return stepsByStartNode.values.toList().leastCommonMultiple()
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        val path = input
            .asSequence()
            .filter { it.isNotEmpty() }
            .first()
        val map = input
            .asSequence()
            .filter { it.isNotEmpty() }
            .drop(1)
            .map { it.toNodeMapEntry() }
            .toMap()
        return map.stepsToZzz(path)
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        val path = input
            .asSequence()
            .filter { it.isNotEmpty() }
            .first()
        val map = input
            .asSequence()
            .filter { it.isNotEmpty() }
            .drop(1)
            .map { it.toNodeMapEntry() }
            .toMap()
        return map.stepsToAllEndNodes(path)
    }

}

fun main() {
    val solver = Aoc2023Day08()
    val prefix = "aoc2023/aoc2023day08"
    val testDataP1a = readLines("$prefix.test.p1-a.txt")
    val testDataP1b = readLines("$prefix.test.p1-b.txt")
    val testDataP2a = readLines("$prefix.test.p2-a.txt")
    val realData = readLines("$prefix.real.txt")

    verify(2L, solver.part1(testDataP1a))
    verify(6L, solver.part1(testDataP1b))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(6L, solver.part2(testDataP2a))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}