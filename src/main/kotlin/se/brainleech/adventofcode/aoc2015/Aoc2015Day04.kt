package se.brainleech.adventofcode.aoc2015

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readText
import se.brainleech.adventofcode.verify
import java.security.MessageDigest
import java.util.stream.IntStream
import kotlin.streams.asSequence
import kotlin.text.Charsets.UTF_8

class Aoc2015Day04 {
    private val md = MessageDigest.getInstance("MD5")

    private fun String.md5(): String {
        return md.digest(this.toByteArray(UTF_8))
            .joinToString(separator = "") { byte -> "%02x".format(byte) }
    }

    fun part1(input: String, from: Int = 1): Int {
        return IntStream.range(from, Int.MAX_VALUE).asSequence().first { (input + it).md5().startsWith("00000") }
    }

    fun part2(input: String, from: Int = 1): Int {
        return IntStream.range(from, Int.MAX_VALUE).asSequence().first { (input + it).md5().startsWith("000000") }
    }

}

fun main() {
    val solver = Aoc2015Day04()
    val prefix = "aoc2015/aoc2015day04"
    val testData = readText("$prefix.test.txt")
    val realData = readText("$prefix.real.txt")

    verify(1_048_970, solver.part1(testData, 1_048_000))
    compute({ solver.part1(realData, 254_000) }, "$prefix.part1 = ")

    verify(5_714_438, solver.part2(testData, 5_714_000))
    compute({ solver.part2(realData, 1_038_000) }, "$prefix.part2 = ")
}