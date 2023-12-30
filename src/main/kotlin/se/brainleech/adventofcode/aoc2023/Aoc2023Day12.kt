package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.*

class Aoc2023Day12 {

    companion object {
        const val DEBUG = false
        const val TRACE = false

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }

        const val OPERATIONAL = '.'
        const val DAMAGED = '#'
        const val UNKNOWN = '?'
    }

    private data class Condition(val patternWithWildcards: String, val expectedGroups: List<Int>) {
        private val knownArrangements = mutableMapOf<Pair<String, List<Int>>, Long>()

        fun numberOfArrangements(remainingPattern: String, remainingGroups: List<Int>): Long {
            if (remainingPattern.isEmpty()) {
                // no more wells to check; we expect to have exhausted all groups too
                return if (remainingGroups.isEmpty()) 1 else 0
            }

            if (remainingGroups.isEmpty()) {
                // no more groups; we do not expect to find any more damaged wells
                return if (remainingPattern.contains(DAMAGED)) 0 else 1
            }

            val key = remainingPattern to remainingGroups
            if (knownArrangements.containsKey(key)) {
                return knownArrangements[key]!!
            }

            var numberOfArrangements = 0L
            if (remainingPattern.startsWith(UNKNOWN) || remainingPattern.startsWith(DAMAGED)) {
                // expect a series of damaged/unknown wells matching the number in the current group

                val lengthOfCurrentGroup = remainingGroups.first()
                val remainingPatternLength = remainingPattern.length
                if (lengthOfCurrentGroup <= remainingPatternLength) {
                    // if there are any operational wells within the group, it is not a complete group
                    val brokenGroup = remainingPattern.subSequence(0, lengthOfCurrentGroup).contains(OPERATIONAL)
                    if (!brokenGroup) {
                        // there must be an operational well after the current group (otherwise it is not a group)
                        val groupIsProperlyEnded = remainingPattern.getOrElse(lengthOfCurrentGroup) { UNKNOWN } != DAMAGED
                        if (lengthOfCurrentGroup == remainingPatternLength || groupIsProperlyEnded) {
                            val nextPattern = remainingPattern.substringOrElse(lengthOfCurrentGroup + 1) { "" }
                            numberOfArrangements += numberOfArrangements(nextPattern, remainingGroups.drop(1))
                        }
                    }
                }
            }
            if (remainingPattern.startsWith(UNKNOWN) || remainingPattern.startsWith(OPERATIONAL)) {
                numberOfArrangements += numberOfArrangements(remainingPattern.substring(1), remainingGroups)
            }

            knownArrangements[key] = numberOfArrangements
            return numberOfArrangements
        }

        fun numberOfArrangements(): Long {
            debug { print("patternsWithWildcards=$patternWithWildcards, expectedGroups=$expectedGroups") }
            return numberOfArrangements(patternWithWildcards, expectedGroups)
                .debug { println(" => $it") }
        }
    }

    private fun String.toCondition(repeated: Int = 1): Condition {
        val parts = this.split(" ")
        // possibly repeat the pattern and group definition five times, separated by an unknown
        // ...but still optimize the pattern (ignore leading/trailing/repeated operational wells)
        val pattern = (parts[0] + "?").repeat(repeated).dropLast(1).trim(OPERATIONAL).replace("[.]{2,}".toRegex(), OPERATIONAL.toString())
        val groups = (parts[1] + ",").repeat(repeated).trim(',').toListOfInts()
        return Condition(pattern, groups)
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .filter { it.isNotEmpty() }
            .sumOf { it.toCondition().numberOfArrangements() }
            .debug { println("total arrangements=$it\n\n") }
    }

    fun part2(input: List<String>) : Long {
        if (input.isEmpty()) return -1L
        return input
            .filter { it.isNotEmpty() }
            .sumOf { it.toCondition(repeated = 5).numberOfArrangements() }
            .debug { println("total arrangements=$it\n\n") }
    }

}

fun main() {
    val solver = Aoc2023Day12()
    val prefix = "aoc2023/aoc2023day12"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(21L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(525152L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}