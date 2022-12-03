package se.brainleech.adventofcode.aoc2020

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2020Day02 {

    open class PasswordPolicy(
        val limitRule: String,
        val character: Char,
        val password: String
    ) {
        open fun valid(): Boolean {
            val (minOccurrence, maxOccurrence) = limitRule.split("-").map { it.toInt() }
            return password.count { it == character } in (minOccurrence..maxOccurrence)
        }
    }

    class PositionBasedPasswordPolicy(
        positionRule: String,
        character: Char,
        password: String
    ) : PasswordPolicy(positionRule, character, password) {
        override fun valid(): Boolean {
            val (firstPosition, secondPosition) = limitRule.split("-").map { it.toInt() - 1 }
            val excerpt = password[firstPosition] + "" + password[secondPosition]
            return excerpt.count { it == character } == 1
        }
    }

    private fun String.asPasswordPolicy(): PasswordPolicy {
        val (limitRule, character, password) = this.split(" ")
        return PasswordPolicy(limitRule, character.first(), password)
    }

    private fun String.asPositionBasedPasswordPolicy(): PasswordPolicy {
        val (limitRule, character, password) = this.split(" ")
        return PositionBasedPasswordPolicy(limitRule, character.first(), password)
    }

    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.asPasswordPolicy() }
            .count { it.valid() }
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .map { it.asPositionBasedPasswordPolicy() }
            .count { it.valid() }
    }

}

fun main() {
    val solver = Aoc2020Day02()
    val prefix = "aoc2020/aoc2020day02"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(2, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(1, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}