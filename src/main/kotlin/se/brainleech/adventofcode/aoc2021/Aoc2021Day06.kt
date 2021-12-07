package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.toListOfInts

class Aoc2021Day06 {
    companion object {
        private const val SPAWN_AGE = 8
        private const val SPAWN_RATE = 6
    }

    private fun List<Int>.toSchool(): MutableMap<Int, Long> {
        return this
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMutableMap()
            .withDefault { 0L }
    }

    private fun MutableMap<Int, Long>.ageAll() {
        for (age in 0 until (SPAWN_AGE + 1)) {
            this[age - 1] = this.getValue(age)
        }
    }

    private fun MutableMap<Int, Long>.spawnNew() {
        this[SPAWN_AGE] = this.getValue(-1)
        this[SPAWN_RATE] = this.getValue(SPAWN_RATE) + this.getValue(-1)
    }

    private fun MutableMap<Int, Long>.population(): Long {
        return this.filter { it.key >= 0 }.values.sum()
    }

    private fun solve(fishAges: List<Int>, days: Int): Long {
        val fishSchool = fishAges.toSchool()

        for (day in 0 until days) {
            fishSchool.ageAll()
            fishSchool.spawnNew()
        }

        return fishSchool.population()
    }

    fun part1(input: String, days: Int): Long {
        return solve(input.toListOfInts(), days)
    }

    fun part2(input: String, days: Int): Long {
        return solve(input.toListOfInts(), days)
    }

}
