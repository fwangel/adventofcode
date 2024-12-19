package se.brainleech.adventofcode.aoc2024

import se.brainleech.adventofcode.Location
import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2024Day04 {

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
    }

    class WordMap(private val word: String, val map: List<CharArray>) {
        private val reverseWord = word.reversed()
        private val height = map.count()
        private val width = map.first().size

        fun numberOfWords() : Long {
            var count = 0L
            for (y in 0 ..< height) {
                for (x in 0 ..< width) {
                    count += numberOfWordsAt(Location(x, y))
                }
            }
            return count / 2
        }

        fun numberOfXWords() : Long {
            var count = 0L
            for (y in 0 ..< height) {
                for (x in 0 ..< width) {
                    count += numberOfXWordsAt(Location(x, y))
                }
            }
            return count
        }

        private fun numberOfWordsAt(pos: Location) : Int {
            var count = 0
            count += countWordInRange(pos) { p -> p.up() }
            count += countWordInRange(pos) { p -> p.upRight() }
            count += countWordInRange(pos) { p -> p.right() }
            count += countWordInRange(pos) { p -> p.downRight() }
            count += countWordInRange(pos) { p -> p.down() }
            count += countWordInRange(pos) { p -> p.downLeft() }
            count += countWordInRange(pos) { p -> p.left() }
            count += countWordInRange(pos) { p -> p.upLeft() }
            return count
        }

        private fun numberOfXWordsAt(pos: Location) : Int {
            val tlToDr = countWordInRange(pos.upLeft()) { p -> p.downRight() } > 0
            val dlToTr = countWordInRange(pos.downLeft()) { p -> p.upRight() } > 0
            return if (tlToDr && dlToTr) 1 else 0
        }

        private fun countWordInRange(pos: Location, generator: (Location) -> Location): Int {
            return if (isWordInRange(toRangeFrom(pos, generator))) 1 else 0
        }

        private fun isWordInRange(locations: Array<Location>) : Boolean {
            val data = CharArray(word.length) { _ -> ' ' }
            for ((index, pos) in locations.withIndex()) {
                if (!pos.inside(width, height)) {
                    return false
                }
                data[index] = map[pos.y][pos.x]
            }
            val foundWord = String(data)
            return (foundWord == word || foundWord == reverseWord)
        }

        private fun toRangeFrom(pos: Location, generator: (Location) -> Location): Array<Location> {
            val locations = Array(word.length) { _ -> pos}
            for ((index, _) in locations.withIndex()) {
                if (index > 0) {
                    locations[index] = generator(locations[index - 1])
                }
            }
            return locations
        }
    }

    private fun List<String>.asCharMap(word: String): WordMap {
        return WordMap(word, this.map { line -> line.toCharArray() }.toList())
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input.asCharMap("XMAS").numberOfWords()
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input.asCharMap("MAS").numberOfXWords()
    }

}

fun main() {
    val solver = Aoc2024Day04()
    val prefix = "aoc2024/aoc2024day04"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(18L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(9L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}