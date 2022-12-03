package se.brainleech.adventofcode.aoc2022

import java.util.stream.Stream
import kotlin.streams.asSequence

class Aoc2022Day03 {

    private fun String.firstHalf() = this.substring(0, (this.length / 2))
    private fun String.secondHalf() = this.substring(this.length / 2)
    private fun String.prioritiesBy(other: String) : Int {
        return prioritiesBy(other, "")
    }

    private fun String.prioritiesBy(second: String, third: String) : Int {
        val secondChars = second.chars().sorted().toArray()
        val thirdChars = third.chars().sorted().toArray()
        return this.chars().sorted().toArray()
            .filter { ch -> secondChars.contains(ch) && (thirdChars.isEmpty() || thirdChars.contains(ch)) }
            .map { ch ->
                if (ch in 65..90) ch - 'A'.code + 27
                else ch - 'a'.code + 1
            }
            .distinct()
            .sum()
    }

    fun part1(input: Stream<String>): Int {
        return input
            .asSequence()
            .map { it.firstHalf().prioritiesBy(it.secondHalf()) }
            .sum()
    }

    fun part2(input: Stream<String>): Int {
        return input
            .asSequence()
            .chunked(size = 3)
            .map { (first, second, third) -> first.prioritiesBy(second, third) }
            .asSequence()
            .sum()
    }

}
