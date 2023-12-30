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

fun List<String>.toListOfCharArrays(): List<CharArray> = this.map { it.toCharArray() }

fun String.toListOfInts(delimiter: String = ","): List<Int> {
    if (this.isBlank()) return emptyList()
    return this.split(delimiter).map { it.trim().toInt() }
}

fun String.toListOfLongs(delimiter: String = ","): List<Long> {
    if (this.isBlank()) return emptyList()
    return this.split(delimiter).map { it.trim().toLong() }
}

fun String.sortedByChar(): String {
    return this
        .toCharArray()
        .sorted()
        .joinToString("")
}

inline fun String.substringOrElse(startIndex: Int, defaultValue: (Int) -> String): String {
    return if (startIndex in 0..lastIndex) substring(startIndex) else defaultValue(startIndex)
}

fun greatestCommonDenominator(x: Long, y: Long): Long = if (y == 0L) x else greatestCommonDenominator(y, x % y)

fun List<Long>.greatestCommonDenominator(): Long = this.fold(0L) { x, y -> greatestCommonDenominator(x, y) }

fun List<Long>.leastCommonMultiple(): Long = this.fold(1L) { x, y -> x * (y / greatestCommonDenominator(x, y))}

// NOTE: Kotlin does not allow inheriting a data class,
//       so the base Location class must implement at
//       least the equals() and hashCode() methods.
open class Location(val x: Int = 0, val y: Int = 0) {
    fun up(): Location = Location(x, y - 1)
    fun right(): Location = Location(x + 1, y)
    fun down(): Location = Location(x, y + 1)
    fun left(): Location = Location(x - 1, y)
    fun upRight(): Location = Location(x + 1, y - 1)
    fun downRight(): Location = Location(x + 1, y + 1)
    fun downLeft(): Location = Location(x - 1, y + 1)
    fun upLeft(): Location = Location(x - 1, y - 1)
    fun neighbours(): List<Location> = listOf(up(), right(), down(), left())
    fun diagonals(): List<Location> = listOf(upRight(), downRight(), downLeft(), upLeft())
    fun neighboursAndDiagonals(): List<Location> = neighbours().plus(diagonals())

    fun inside(width: Int, height: Int): Boolean = x >= 0 && y >= 0 && x < width && y < height

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        other as Location
        return (x == other.x && y == other.y)
    }

    override fun hashCode(): Int = 31 * x + y
    override fun toString(): String = "($x,$y)"
}
