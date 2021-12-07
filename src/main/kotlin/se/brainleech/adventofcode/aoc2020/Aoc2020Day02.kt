package se.brainleech.adventofcode.aoc2020

import java.util.stream.Stream
import kotlin.streams.asSequence

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

    fun part1(input: Stream<String>): Int {
        return input
            .asSequence()
            .map { it.asPasswordPolicy() }
            .count { it.valid() }
    }

    fun part2(input: Stream<String>): Int {
        return input
            .asSequence()
            .map { it.asPositionBasedPasswordPolicy() }
            .count { it.valid() }
    }

}
