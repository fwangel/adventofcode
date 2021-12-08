package se.brainleech.adventofcode

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.IntStream
import java.util.stream.Stream

fun out(value: Any, prefix: String = ""): Any {
    print(prefix)
    println(value)
    return value
}

fun out(value: IntArray, prefix: String = ""): IntArray {
    print(prefix)
    println(value.asList())
    return value
}

fun out(value: List<IntArray>, prefix: String = ""): List<IntArray> {
    print(prefix)
    println(value.map { it.asList() })
    return value
}

fun readLines(filename: String): Stream<String> {
    val resource = checkNotNull(ClassLoader.getSystemClassLoader().getResource(filename))
    val path = Paths.get(resource.toURI())
    return Files.lines(path)
}

fun readIntegers(filename: String): IntStream {
    return readLines(filename).mapToInt(String::toInt)
}

fun readText(filename: String): String {
    return readLines(filename).collect(Collectors.joining("\n"))
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
