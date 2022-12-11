package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day11 {

    companion object {
        const val DEBUG = false
        const val OLD_VALUE = Long.MAX_VALUE

        fun Long.orOther(rule: Pair<Long, Long>): Long = if (this == rule.first) rule.second else this
    }

    private inline fun <T> T.debug(block: (T) -> Unit): T {
        if (DEBUG) block(this)
        return this
    }

    sealed class WorryManager {
        abstract fun manage(value: Long): Long
    }
    data class WorryDivider(val factor: Int): WorryManager() {
        override fun manage(value: Long) = value.div(factor)
    }
    data class WorryModulo(val factor: Long): WorryManager() {
        override fun manage(value: Long) = value.mod(factor)
    }

    sealed class Operation {
        abstract fun newValue(old: Long): Long
    }
    data class MulOperation(val value: Long) : Operation() {
        override fun newValue(old: Long): Long = old * value.orOther(OLD_VALUE to old)
    }
    data class AddOperation(val value: Long) : Operation() {
        override fun newValue(old: Long): Long = old + value.orOther(OLD_VALUE to old)
    }

    private fun String.asMonkeyIndex(): Int {
        return this.substringAfterLast(' ').substringBefore(':').toInt()
    }

    private fun String.asStartingItems(): MutableList<Long> {
        return this
            .replace(" ", "")
            .substringAfter(':')
            .split(',')
            .map { worryLevel -> worryLevel.toLong() }
            .toMutableList()
    }

    private fun String.asOperation(): Operation {
        val operationFormula = this
            .replace(" ", "")
            .substringAfter('=')
            .replace("old", OLD_VALUE.toString())
        val operationOperator = operationFormula[operationFormula.indexOfFirst { it in listOf('*', '+') }]
        val operationOperand = operationFormula.substringAfter(operationOperator).toLong()
        return when (operationOperator) {
            '*' -> MulOperation(operationOperand)
            '+' -> AddOperation(operationOperand)
            else -> error("Invalid operator '$operationOperator'!")
        }
    }

    private fun String.asDecidingFactor(): Long {
        return this.substringAfterLast(' ').toLong()
    }

    private fun String.asMonkeyIndexReference(): Int {
        return this.substringAfterLast(' ').toInt()
    }

    private fun List<String>.asMonkey(): Monkey {
        val monkeyIndex = this[1].asMonkeyIndex()
        val worryLevels = this[1].asStartingItems()
        val operation = this[2].asOperation()
        val decidingFactor = this[3].asDecidingFactor()
        val monkeyIndexOnTrue = this[4].asMonkeyIndexReference()
        val monkeyIndexOnFalse = this[5].asMonkeyIndexReference()
        return Monkey(monkeyIndex, decidingFactor, worryLevels, operation, monkeyIndexOnTrue, monkeyIndexOnFalse)
    }

    private fun List<String>.asMonkeys(): List<Monkey> {
        return this
            .filter { it.isNotEmpty() }
            .chunked(6)
            .map { it.asMonkey() }
    }

    private fun List<Monkey>.monkeyBusinessLevel(): Long {
        return this.asSequence()
            .map { it.inspections() }
            .sortedDescending()
            .chunked(2)
            .map { it.first().times(it.last()) }
            .first()
    }

    data class Monkey(val monkeyIndex: Int,
                      val decidingFactor: Long,
                      private val worryLevels: MutableList<Long>,
                      private val operation: Operation,
                      private val throwToMonkeyOnTrue: Int,
                      private val throwToMonkeyOnFalse: Int,
                      private var inspections: Long = 0) {

        fun inspections(): Long = inspections
        fun worryLevels(): List<Long> = worryLevels.toList()

        private fun catchItem(item: Long) = worryLevels.add(item)
        private fun throwTo(otherMonkey: Monkey, what: () -> Long) = otherMonkey.catchItem(what())

        fun playWith(otherMonkeys: List<Monkey>, worryManager: WorryManager) {
            if (worryLevels.isEmpty()) return
            val trueMonkey = otherMonkeys[throwToMonkeyOnTrue]
            val falseMonkey = otherMonkeys[throwToMonkeyOnFalse]
            while (worryLevels.isNotEmpty()) {
                val newValue = worryManager.manage(operation.newValue(worryLevels.removeFirst()))
                if ((newValue % decidingFactor) == 0L) throwTo(trueMonkey) { newValue }
                else throwTo(falseMonkey) { newValue }
                inspections++
            }
        }
    }

    fun part1(input: List<String>): Long {
        if (input.isEmpty()) return -1
        val monkeys = input.asMonkeys()
        val worryManager = WorryDivider(3)
        repeat(20) { index ->
            monkeys.onEach { monkey -> monkey.playWith(monkeys, worryManager) }

            debug {
                println("== After round ${index + 1} ==")
                monkeys.onEach { monkey -> println("Monkey ${monkey.monkeyIndex}: ${monkey.worryLevels().joinToString(", ")}") }
                monkeys.monkeyBusinessLevel().debug { println("Monkey Business Level = $it") }
            }
        }

        return monkeys.monkeyBusinessLevel()
            .debug { println("Monkey Business Level = $it") }
    }

    fun part2(input: List<String>): Long {
        if (input.isEmpty()) return -1
        val monkeys = input.asMonkeys()
        val commonDenominator = monkeys.fold(1L) { acc, monkey -> acc * monkey.decidingFactor }
        val worryManager = WorryModulo(commonDenominator)
        repeat(10_000) { index ->
            monkeys.onEach { monkey -> monkey.playWith(monkeys, worryManager) }

            debug {
                if ((index + 1) in listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000)) {
                    println("== After round ${index + 1} ==")
                    monkeys.onEach { monkey -> println("Monkey ${monkey.monkeyIndex} inspected items ${monkey.inspections()} times: ${monkey.worryLevels().joinToString(", ")}") }
                    monkeys.monkeyBusinessLevel().debug { println("Monkey Business Level = $it") }
                }
            }
        }

        return monkeys.monkeyBusinessLevel()
            .debug { println("Monkey Business Level = $it") }
    }

}

fun main() {
    val solver = Aoc2022Day11()
    val prefix = "aoc2022/aoc2022day11"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(10605L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(2713310158L, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}