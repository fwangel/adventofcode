package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day11 {

    companion object {
        const val DEBUG = false
        const val OLD_VALUE = Int.MAX_VALUE

        fun Int.orOther(rule: Pair<Int, Int>): Int = if (this == rule.first) rule.second else this
    }

    private inline fun <T> T.debug(block: (T) -> Unit): T {
        if (DEBUG) block(this)
        return this
    }

    sealed class Operation {
        abstract fun newValue(old: Int): Int
    }
    data class MulOperation(val value: Int) : Operation() {
        override fun newValue(old: Int): Int = old * value.orOther(OLD_VALUE to old)
    }
    data class DivOperation(val value: Int) : Operation() {
        override fun newValue(old: Int): Int = old / value.orOther(OLD_VALUE to old)
    }
    data class AddOperation(val value: Int) : Operation() {
        override fun newValue(old: Int): Int = old + value.orOther(OLD_VALUE to old)
    }
    data class SubOperation(val value: Int) : Operation() {
        override fun newValue(old: Int): Int = old - value.orOther(OLD_VALUE to old)
    }

    sealed class Test {
        abstract fun test(value: Int) : Boolean
    }
    data class DivisibleByConst(val constant: Int): Test() {
        override fun test(value: Int): Boolean = (value % constant) == 0
    }

    data class Monkey(val monkeyIndex: Int,
                      private val worryLevels: MutableList<Int> = mutableListOf(),
                      private val operation: Operation,
                      private val worryTest: Test,
                      private val monkeyIndexOnTrue: Int,
                      private val monkeyIndexOnFalse: Int,
                      private var inspections: Int = 0) {

        private lateinit var monkeyOnTrue: Monkey
        private lateinit var monkeyOnFalse: Monkey

        fun setMonkeyBuddies(monkeys: List<Monkey>) {
            this.monkeyOnTrue = monkeys[monkeyIndexOnTrue]
            this.monkeyOnFalse = monkeys[monkeyIndexOnFalse]
        }

        fun act() {
            if (worryLevels.isEmpty()) return
            inspections += worryLevels.size

            worryLevels.onEach {
                val newValue = operation.newValue(it).div(3)
                if (worryTest.test(newValue)) monkeyOnTrue.worryLevels.add(newValue)
                else monkeyOnFalse.worryLevels.add(newValue)
            }
            worryLevels.clear()
        }

        fun inspections(): Int = inspections

        fun worryLevels(): List<Int> = worryLevels.toList()

        companion object {
            fun from(data: List<String>): Monkey {
                val monkeyIndex = data[0].substringAfter(' ').substringBefore(':').toInt()
                val worryLevels = data[1].substringAfter(": ").split(", ").map { it.toInt() }.toMutableList()

                val operationFormula = data[2].substringAfter(": new = ")
                    .replace(" ", "")
                    .replace("old", OLD_VALUE.toString())
                val operationOperator = operationFormula[operationFormula.indexOfFirst { it in listOf('*', '+', '/', '-') }]
                val operationOperand = operationFormula.substringAfter(operationOperator).toInt()

                val operation = when (operationOperator) {
                    '*' -> MulOperation(operationOperand)
                    '/' -> DivOperation(operationOperand)
                    '+' -> AddOperation(operationOperand)
                    '-' -> SubOperation(operationOperand)
                    else -> error("Invalid operator '$operationOperator'!")
                }

                val divisibleBy = data[3].substringAfter(": divisible by ")
                    .replace("old", OLD_VALUE.toString())
                    .toInt()
                val worryTest = DivisibleByConst(divisibleBy)

                val monkeyIndexOnTrue = data[4].substringAfterLast(' ').toInt()
                val monkeyIndexOnFalse = data[5].substringAfterLast(' ').toInt()

                return Monkey(monkeyIndex, worryLevels, operation, worryTest, monkeyIndexOnTrue, monkeyIndexOnFalse)
            }
        }
    }

    private fun List<Monkey>.monkeyBusinessLevel(): Int {
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

    fun part1(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        val monkeys = input.asMonkeys()

        repeat(20) { index ->
            monkeys.onEach { monkey -> monkey.act() }

            debug {
                println("== After round ${index + 1} ==")
                monkeys.onEach { monkey -> println("Monkey ${monkey.monkeyIndex}: ${monkey.worryLevels().joinToString(", ")}") }
            }
        }

        return monkeys.monkeyBusinessLevel()
            .debug { println("Monkey Business Level = $it") }
    }

    fun part2(input: List<String>) : Int {
        if (input.isEmpty()) return -1
        return -1
    }

}

fun main() {
    val solver = Aoc2022Day11()
    val prefix = "aoc2022/aoc2022day11"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(10605, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(Int.MIN_VALUE, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}