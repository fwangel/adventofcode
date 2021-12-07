package se.brainleech.adventofcode.aoc2015

class Aoc2015Day01 {

    fun part1(input: String): Int {
        var floor = 0
        input.onEach {
            when (it) {
                '(' -> floor++
                ')' -> floor--
            }
        }
        return floor
    }

    fun part2(input: String): Int {
        var floor = 0
        input.onEachIndexed { index, char ->
            when (char) {
                '(' -> floor++
                ')' -> floor--
            }
            if (floor == -1) {
                return index + 1
            }
        }
        return 0
    }

}
