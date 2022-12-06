package se.brainleech.adventofcode

import java.io.File
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

fun <T> verify(expected: T, actual: T) {
    check(expected == actual) { "Expected: `$expected`, found: `$actual`" }
}

inline fun compute(block: () -> Any, prefix: String = ""): Any {
    val start = System.nanoTime()
    val value = block()
    val elapsed = String.format(Locale.ENGLISH, "%.3f", (System.nanoTime() - start) / 1_000_000.0).padStart(9)
    println("(${elapsed}ms) $prefix$value")
    return value
}

fun readLines(filename: String): List<String> {
    return File("src/main/resources", filename).readLines()
}

fun readIntegers(filename: String): List<Int> {
    return readLines(filename).stream().mapToInt(String::toInt).toList()
}

fun readText(filename: String): String {
    return readLines(filename).stream().collect(Collectors.joining("\n"))
}

fun String.toListOfInts(): List<Int> {
    if (this.isBlank()) return emptyList()
    return this.split(",").map { it.toInt() }
}

fun String.sortedByChar(): String {
    return this
        .toCharArray()
        .sorted()
        .joinToString("")
}
