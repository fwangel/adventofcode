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
    data class DivOperation(val value: Long) : Operation() {
        override fun newValue(old: Long): Long = old / value.orOther(OLD_VALUE to old)
    }
    data class AddOperation(val value: Long) : Operation() {
        override fun newValue(old: Long): Long = old + value.orOther(OLD_VALUE to old)
    }
    data class SubOperation(val value: Long) : Operation() {
        override fun newValue(old: Long): Long = old - value.orOther(OLD_VALUE to old)
    }

    sealed class Test {
        abstract fun test(value: Long) : Boolean
    }
    data class DivisibleByConst(val constant: Long): Test() {
        override fun test(value: Long): Boolean = (value % constant) == 0L
    }

    data class Monkey(val monkeyIndex: Int,
                      private val worryLevels: MutableList<Long> = mutableListOf(),
                      private val operation: Operation,
                      val worryTest: Test,
                      private val monkeyIndexOnTrue: Int,
                      private val monkeyIndexOnFalse: Int,
                      private var inspections: Long = 0) {

        private lateinit var worryManager: WorryManager
        private lateinit var monkeyOnTrue: Monkey
        private lateinit var monkeyOnFalse: Monkey

        fun setMonkeyBuddies(monkeys: List<Monkey>) {
            this.monkeyOnTrue = monkeys[monkeyIndexOnTrue]
            this.monkeyOnFalse = monkeys[monkeyIndexOnFalse]
        }

        fun setWorryManager(worryManager: WorryManager) {
            this.worryManager = worryManager
        }

        fun act(): Monkey {
            if (worryLevels.isEmpty()) return this
            inspections += worryLevels.size

            worryLevels.onEach {
                val newValue = worryManager.manage(operation.newValue(it))
                if (worryTest.test(newValue)) monkeyOnTrue.worryLevels.add(newValue)
                else monkeyOnFalse.worryLevels.add(newValue)
            }
            worryLevels.clear()
            return this
        }

        fun inspections(): Long = inspections

        fun worryLevels(): List<Long> = worryLevels.toList()

        companion object {
            fun from(data: List<String>): Monkey {
                val monkeyIndex = data[0].substringAfter(' ').substringBefore(':').toInt()
                val worryLevels = data[1].substringAfter(": ").split(", ").map { it.toLong() }.toMutableList()

                val operationFormula = data[2].substringAfter(": new = ")
                    .replace(" ", "")
                    .replace("old", OLD_VALUE.toString())
                val operationOperator = operationFormula[operationFormula.indexOfFirst { it in listOf('*', '+', '/', '-') }]
                val operationOperand = operationFormula.substringAfter(operationOperator).toLong()

                val operation = when (operationOperator) {
                    '*' -> MulOperation(operationOperand)
                    '/' -> DivOperation(operationOperand)
                    '+' -> AddOperation(operationOperand)
                    '-' -> SubOperation(operationOperand)
                    else -> error("Invalid operator '$operationOperator'!")
                }

                val divisibleBy = data[3].substringAfter(": divisible by ")
                    .replace("old", OLD_VALUE.toString())
                    .toLong()
                val worryTest = DivisibleByConst(divisibleBy)

                val monkeyIndexOnTrue = data[4].substringAfterLast(' ').toInt()
                val monkeyIndexOnFalse = data[5].substringAfterLast(' ').toInt()

                return Monkey(monkeyIndex, worryLevels, operation, worryTest, monkeyIndexOnTrue, monkeyIndexOnFalse)
            }
        }
    }

    private fun List<Monkey>.monkeyBusinessLevel(): Long {
        return this
            .sortedByDescending { it.inspections() }
            .chunked(2)
            .map { it.first().inspections().times(it.last().inspections()) }
            .first()
    }

    private fun List<String>.asMonkeys(): List<Monkey> {
        val monkeys = this
            .filter { it.isNotEmpty() }
            .chunked(6)
            .map { Monkey.from(it) }

        monkeys.onEach { monkey -> monkey.setMonkeyBuddies(monkeys) }

        return monkeys
    }

    fun part1(input: List<String>) : Long {
        if (input.isEmpty()) return -1
        val monkeys = input.asMonkeys()

        monkeys.onEach { monkey -> monkey.setWorryManager(WorryDivider(3)) }

        repeat(20) { index ->
            monkeys.onEach { monkey -> monkey.act() }

            debug {
                println("== After round ${index + 1} ==")
                monkeys.onEach { monkey -> println("Monkey ${monkey.monkeyIndex}: ${monkey.worryLevels().joinToString(", ")}") }
                monkeys.monkeyBusinessLevel().debug { println("Monkey Business Level = $it") }
            }
        }

        return monkeys.monkeyBusinessLevel()
            .debug { println("Monkey Business Level = $it") }
    }

    fun part2(input: List<String>) : Long {
        val monkeys = input.asMonkeys()

        val lowestCommonDenominator = monkeys
            .map { monkey -> (monkey.worryTest as DivisibleByConst).constant }
            .reduce { acc, value -> acc * value }

        monkeys.onEach { monkey ->
            monkey.setWorryManager(WorryModulo(lowestCommonDenominator))
        }

        repeat(10_000) { index ->
            monkeys.onEach { monkey -> monkey.act() }

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