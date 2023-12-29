package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.*

class Aoc2023Day10 {

    companion object {
        const val DEBUG = true
        const val TRACE = false

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }

        const val UP    = 'U'
        const val RIGHT = 'R'
        const val DOWN  = 'D'
        const val LEFT  = 'L'

        private const val PIPE_TL = 'F'
        private const val PIPE_TR = '7'
        private const val PIPE_BR = 'J'
        private const val PIPE_BL = 'L'
        private const val PIPE_V  = '|'
        private const val PIPE_H  = '-'
        private const val PIPE_START  = 'S'

        private const val EMPTY = '.'
        private const val FILLER = ','
        private const val OUTSIDE = 'o'
        private val EMPTY_OR_FILLER = listOf(EMPTY, FILLER)

        private val LINE_DRAWING_BY_PART = mapOf(
            PIPE_TL to '╭',
            PIPE_TR to '╮',
            PIPE_BR to '╯',
            PIPE_BL to '╰',
            PIPE_V to '│',
            PIPE_H to '━',
        )

        private val CONNECTORS_BY_PART_AND_DIRECTION: Map<Char, Map<Char, List<Char>>> = mapOf(
            PIPE_TL to mapOf(
                UP to listOf(),
                RIGHT to listOf(PIPE_H, PIPE_BR, PIPE_TR),
                DOWN to listOf(PIPE_V, PIPE_BR, PIPE_BL),
                LEFT to listOf(),
            ),
            PIPE_TR to mapOf(
                UP to listOf(),
                RIGHT to listOf(),
                DOWN to listOf(PIPE_V, PIPE_BR, PIPE_BL),
                LEFT to listOf(PIPE_H, PIPE_BL, PIPE_TL),
            ),
            PIPE_BR to mapOf(
                UP to listOf(PIPE_V, PIPE_TL, PIPE_TR),
                RIGHT to listOf(),
                DOWN to listOf(),
                LEFT to listOf(PIPE_H, PIPE_BL, PIPE_TL),
            ),
            PIPE_BL to mapOf(
                UP to listOf(PIPE_V, PIPE_TL, PIPE_TR),
                RIGHT to listOf(PIPE_H, PIPE_BR, PIPE_TR),
                DOWN to listOf(),
                LEFT to listOf(),
            ),
            PIPE_V to mapOf(
                UP to listOf(PIPE_V, PIPE_TL, PIPE_TR),
                RIGHT to listOf(),
                DOWN to listOf(PIPE_V, PIPE_BL, PIPE_BR),
                LEFT to listOf(),
            ),
            PIPE_H to mapOf(
                UP to listOf(),
                RIGHT to listOf(PIPE_H, PIPE_BR, PIPE_TR),
                DOWN to listOf(),
                LEFT to listOf(PIPE_H, PIPE_TL, PIPE_BL),
            ),
            PIPE_START to mapOf(
                UP to listOf(PIPE_V, PIPE_TL, PIPE_TR),
                RIGHT to listOf(PIPE_H, PIPE_BR, PIPE_TR),
                DOWN to listOf(PIPE_V, PIPE_BL, PIPE_BR),
                LEFT to listOf(PIPE_H, PIPE_TL, PIPE_BL),
            ),
        )

        fun Char.inverseDirection(): Char {
            return when(this) {
                UP -> DOWN
                DOWN -> UP
                LEFT -> RIGHT
                RIGHT -> LEFT
                else -> throw IllegalStateException("Invalid direction: $this")
            }
        }

        fun Char.canConnectWith(other: Char, direction: Char): Boolean {
            return CONNECTORS_BY_PART_AND_DIRECTION[this]?.get(direction)?.contains(other) ?: false
        }

        private fun expandedHorizontally(c: Char): Char {
            if (c == PIPE_TL || c == PIPE_BL || c == PIPE_H) return PIPE_H
            if (c == PIPE_START) return PIPE_START
            return FILLER
        }

        private fun expandedVertically(c: Char): Char {
            if (c == PIPE_TL || c == PIPE_TR || c == PIPE_V) return PIPE_V
            return FILLER
        }

        fun List<CharArray>.expanded(): List<CharArray> {
            val grid = this
            val width = grid.first().size
            val height = grid.size
            val expandedGrid = mutableListOf<CharArray>()
            for (row in 0 until height) {
                val expandedRow = "$FILLER".repeat(width * 2).toCharArray()
                for (col in 0 until width) {
                    val c = grid[row][col]
                    expandedRow[(col * 2)] = c
                    expandedRow[(col * 2) + 1] = expandedHorizontally(c)
                }
                expandedGrid.add(expandedRow.clone())

                for (col in 0 until width) {
                    val c = grid[row][col]
                    expandedRow[(col * 2)] = expandedVertically(c)
                    expandedRow[(col * 2) + 1] = FILLER
                }
                expandedGrid.add(expandedRow.clone())
            }
            return expandedGrid.toList()
        }

    }

    private data class Pipes(var grid: List<CharArray>) {
        private var width = grid.first().size
        private var height = grid.size
        private var expanded = false

        private var start: Location = findStart()
        private var previous: Location? = null
        private var steps = 0L
        private var path: MutableList<Pair<Location, Long>> = mutableListOf()

        fun at(pos: Location): Char {
            if ((pos.y in 0 until height) && (pos.x in 0 until width)) {
                return grid[pos.y][pos.x]
            }
            return OUTSIDE
        }

        fun set(pos: Location, symbol: Char) {
            if ((pos.y in 0 until height) && (pos.x in 0 until width)) {
                grid[pos.y][pos.x] = symbol
            }
        }

        fun maxSteps() : Long {
            return steps / 2
        }

        fun numberOfEnclosed() : Long {
            var inside = 0L
            for (row in 0 until height) {
                for (col in 0 until width) {
                    if (grid[row][col] == EMPTY) inside++
                }
            }
            return inside
        }

        fun expand() {
            grid = grid.expanded()
            width = grid.first().size
            height = grid.size
            start = Location(x = start.x * 2, y = start.y * 2)
            previous = null
            steps = 0L
            path = mutableListOf()
            expanded = true
        }

        fun show() {
            debug { println() }
            for (row in 0 until height) {
                if (!expanded || ((row % 2) == 0)) {
                    for (col in 0 until width) {
                        if (!expanded || ((col % 2) == 0)) {
                            val ch = grid[row][col]
                            debug { print(LINE_DRAWING_BY_PART[ch] ?: ch) }
                        }
                    }
                    debug { println() }
                }
            }
        }

        fun clearNonPipeLocations() {
            if (expanded) throw IllegalStateException("Must be done before grid is expanded!")

            val pipeLocations = path.map { it.first }
            for (row in 0 until height) {
                for (col in 0 until width) {
                    val pos = Location(x = col, y = row)
                    if (!pipeLocations.contains(pos)) {
                        set(pos, EMPTY)
                    }
                }
            }
        }

        fun floodFill() {
            if (!expanded) throw IllegalStateException("Must be expanded!")

            // Flood-fill (node) algorithm from Wikipedia
            //   1. Set Q to the empty queue or stack.
            val q = mutableListOf<Location>()
            //   2. Add node(s) to the end of Q.
            for (col in 0 until width) {
                if (grid[0][col] in EMPTY_OR_FILLER) q.add(Location(x = col, y = 0))
                if (grid[height - 1][col] in EMPTY_OR_FILLER) q.add(Location(x = col, y = height - 1))
            }
            for (row in 0 until height) {
                if (grid[row][0] in EMPTY_OR_FILLER) q.add(Location(x = 0, y = row))
                if (grid[row][width - 1] in EMPTY_OR_FILLER) q.add(Location(x = width - 1, y = row))
            }
            //   3. While Q is not empty:
            while (q.isNotEmpty()) {
                val n = q.removeAt(0)
                val c = at(n)
                //   4.   Set n equal to the first element of Q.
                //   5.   Remove first element from Q.
                //   6.   If n is Inside:
                if (c == EMPTY || c == FILLER) {
                    set(n, OUTSIDE)
                    val l = n.left()
                    val r = n.right()
                    val u = n.up()
                    val d = n.down()
                    //          Add the node to the west of n to the end of Q.
                    if (at(l) in EMPTY_OR_FILLER) q.add(l)
                    //          Add the node to the east of n to the end of Q.
                    if (at(r) in EMPTY_OR_FILLER) q.add(r)
                    //          Add the node to the north of n to the end of Q.
                    if (at(u) in EMPTY_OR_FILLER) q.add(u)
                    //          Add the node to the south of n to the end of Q.
                    if (at(d) in EMPTY_OR_FILLER) q.add(d)
                }

                //   7. Continue looping until Q is exhausted.
            }
            //   8. Return.
        }

        fun walk() : List<Pair<Location, Long>> {
            var current = start
            while (true) {
                val from = at(current)

                val up = current.up() to at(current.up())
                val right = current.right() to at(current.right())
                val down = current.down() to at(current.down())
                val left = current.left() to at(current.left())

                val possibles = mutableMapOf<Char, Pair<Location, Char?>>()
                if ((previous == null || up.first    != previous) && from.canConnectWith(up.second,    UP   )) possibles[UP   ] = up
                if ((previous == null || right.first != previous) && from.canConnectWith(right.second, RIGHT)) possibles[RIGHT] = right
                if ((previous == null || down.first  != previous) && from.canConnectWith(down.second,  DOWN )) possibles[DOWN ] = down
                if ((previous == null || left.first  != previous) && from.canConnectWith(left.second,  LEFT )) possibles[LEFT ] = left

                // if previous == null, determine the tile for the start position
                if (previous == null) {
                    path.add(current to 0)

                    if (!expanded) {
                        // can only be two possible ways from the start!
                        val a = possibles.values.first()
                        val b = possibles.values.last()

                        val aDir = possibles.keys.first().inverseDirection()
                        val bDir = possibles.keys.last().inverseDirection()

                        val aTiles = CONNECTORS_BY_PART_AND_DIRECTION[a.second]?.get(aDir) ?: emptyList()
                        val bTiles = CONNECTORS_BY_PART_AND_DIRECTION[b.second]?.get(bDir) ?: emptyList()
                        val commonTiles = aTiles.intersect(bTiles.toSet())
                        val row = current.y
                        val col = current.x
                        grid[row][col] = commonTiles.first()

                        trace { println("Changing the 'S' into ${commonTiles.first()} (via $a and $b)") }
                    }
                }
                steps++

                val next: Location? = if (possibles.isEmpty()) null else possibles.iterator().next().value.first
                if (next == null || next == path[0].first) {
                    trace { println("Reached the end! total steps = $steps, max distance: ${steps / 2}") }
                    return path.toList()
                }

                previous = current
                current = next
                path.add(current to steps)
            }
        }

        fun findStart(): Location {
            for (row in 0 until height) {
                for (col in 0 until width) {
                    if (grid[row][col] == PIPE_START) {
                        return Location(x = col, y = row)
                    }
                }
            }
            throw IllegalStateException("No starting point found!")
        }
    }

    fun canConnect(current: Char, next: Char, direction: Char): Boolean {
        return current.canConnectWith(next, direction)
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        val pipes = Pipes(input.toListOfCharArrays())
        with(pipes) {
            walk()
        }
        return pipes.maxSteps()
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        val pipes = Pipes(input.toListOfCharArrays())
        with(pipes) {
            walk()
            clearNonPipeLocations()
            expand()
            walk()
            floodFill()
            debug { show() }
        }
        return pipes.numberOfEnclosed()
    }

}

fun main() {
    val solver = Aoc2023Day10()
    val prefix = "aoc2023/aoc2023day10"
    val testData = readLines("$prefix.test.p1-a.txt")
    val testDataP2a = readLines("$prefix.test.p2-a.txt")
    val testDataP2b = readLines("$prefix.test.p2-b.txt")
    val realData = readLines("$prefix.real.txt")

    verify(false, solver.canConnect('F', '.', 'R'))
    verify(true, solver.canConnect('F', '-', 'R'))
    verify(true, solver.canConnect('F', 'J', 'R'))
    verify(true, solver.canConnect('F', '7', 'R'))
    verify(true, solver.canConnect('F', '|', 'D'))
    verify(false, solver.canConnect('F', '-', 'L'))
    verify(false, solver.canConnect('F', '|', 'U'))
    verify(false, solver.canConnect('F', '|', 'R'))

    verify(8L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(8L, solver.part2(testDataP2a))
    verify(10L, solver.part2(testDataP2b))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}