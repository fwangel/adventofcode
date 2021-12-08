package se.brainleech.adventofcode.aoc2015

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
