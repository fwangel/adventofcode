package se.brainleech.adventofcode.aoc2016

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify
import kotlin.math.abs

class Aoc2016Day01 {

    enum class Facing {
        NORTH, EAST, SOUTH, WEST;

        fun turn(direction: Char) : Facing {
            if (direction == 'R') {
                return when (this) { NORTH -> EAST; EAST -> SOUTH; SOUTH -> WEST; WEST -> NORTH }
            }
            return when (this) { NORTH -> WEST; WEST -> SOUTH; SOUTH -> EAST; EAST -> NORTH }
        }
    }

    class Location(var x: Int, var y: Int, private var facing: Facing = Facing.NORTH) {
        fun moveBy(instruction: String) : Location {
            facing = facing.turn(instruction.first())
            val steps = instruction.substring(1).toInt()
            when (facing) {
                Facing.NORTH -> y -= steps
                Facing.EAST -> x += steps
                Facing.SOUTH -> y += steps
                Facing.WEST -> x -= steps
            }
            return this
        }

        fun pathBy(instruction: String) : List<Location> {
            val path = mutableListOf<Location>()
            val facingNext = facing.turn(instruction.first())
            val steps = instruction.substring(1).toInt()
            var xNext = x
            var yNext = y
            when (facingNext) {
                Facing.NORTH -> repeat(steps) { path.add(Location(x, --yNext, facingNext)) }
                Facing.EAST -> repeat(steps) { path.add(Location(++xNext, y, facingNext)) }
                Facing.SOUTH -> repeat(steps) { path.add(Location(x, ++yNext, facingNext)) }
                Facing.WEST -> repeat(steps) { path.add(Location(--xNext, y, facingNext)) }
            }
            return path
        }

        fun id() = x.times(100_000_000).plus(y)
        fun distance() = abs(x) + abs(y)
    }

    fun part1(input: String) : Int {
        val location = Location(0, 0)
        val instructions = input
            .replace(" ", "")
            .split(",")
            .toList()

        instructions.forEach { location.moveBy(it) }

        return location.distance()
    }

    fun part2(input: String) : Int {
        var location = Location(0, 0)
        val instructions = input
            .replace(" ", "")
            .split(",")
            .toList()

        val visited = mutableSetOf(location.id())
        instructions.forEach { instruction ->
            val path = location.pathBy(instruction).also { location = it.last() }
            val revisited = path.firstOrNull { visited.contains(it.id()) }
            if (revisited != null) {
                return revisited.distance()
            }
            visited.addAll(path.map { it.id() })
        }

        // no revisited location!
        return -1
    }

}

fun main() {
    val solver = Aoc2016Day01()
    val prefix = "aoc2016/aoc2016day01"
    val testData = readLines("$prefix.test.txt").filter { it.isNotBlank() }
    val realData = readLines("$prefix.real.txt").filter { it.isNotBlank() }

    listOf(5, 2, 12).forEachIndexed { index, expected -> verify(expected, solver.part1(testData[index])) }
    compute({ solver.part1(realData.first()) }, "$prefix.part1 = ")

    verify(4, solver.part2(testData.last()))
    compute({ solver.part2(realData.first()) }, "$prefix.part2 = ")
}