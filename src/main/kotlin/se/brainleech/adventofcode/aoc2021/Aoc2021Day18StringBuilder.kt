package se.brainleech.adventofcode.aoc2021

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2021Day18StringBuilder {

    companion object {
        private val REGULAR_NUMBER_PAIR = Regex("\\[(\\d+),(\\d+)]")
    }

    class SnailfishNumber(input: String) {
        val value = StringBuilder(input)

        fun plus(other: SnailfishNumber): SnailfishNumber {
            value.insert(0, '[')
            value.append(',')
            value.append(other.value)
            value.append(']')
            return this.reduce()
        }

        fun magnitude(): Int {
            var minimized = value.toString()
            do {
                minimized = REGULAR_NUMBER_PAIR.replace(minimized) {
                    3.times(it.groupValues[1].toInt())
                        .plus(2.times(it.groupValues[2].toInt()))
                        .toString()
                }
            } while (minimized.contains('['))
            return minimized.toInt()
        }

        fun reduce(
            maxSteps: Int = Int.MAX_VALUE,
            allowExplode: Boolean = true,
            allowSplit: Boolean = true
        ): SnailfishNumber {
            val context = this
            var stepsCompleted = 0
            do {
                var actionPerformed = false
                if (allowExplode) findIndexOfFirstExplodablePair()?.apply {
                    explodeRegularNumberPairAt(this)
                    actionPerformed = true
                    if (++stepsCompleted >= maxSteps) return context
                }

                if (allowSplit && !actionPerformed) findIndexOfFirstSplittableNumber()?.apply {
                    splitRegularNumberAt(this)
                    actionPerformed = true
                    if (++stepsCompleted >= maxSteps) return context
                }

            } while (actionPerformed)

            return this
        }

        private fun findIndexOfFirstExplodablePair(): IntRange? {
            var depth = 0
            var index = 0
            while (index < value.length) {
                when (value[index]) {
                    '[' -> depth++
                    ']' -> depth--
                    in '0'..'9' -> {
                        if (depth > 4) {
                            var subIndex = index
                            while (subIndex < value.length && value[subIndex] == ',' || value[subIndex] in '0'..'9') {
                                subIndex++
                            }
                            if (value[subIndex] == ']') {
                                return index - 1..subIndex
                            }
                        }
                    }
                }
                index++
            }
            return null
        }

        private fun findIndexOfFirstRegularNumberToTheLeftOf(startIndex: Int): IntRange? {
            var index = startIndex
            while (index >= 0) {
                when (value[index]) {
                    in '0'..'9' -> {
                        var subIndex = index
                        while (subIndex >= 0 && value[subIndex] in '0'..'9') {
                            subIndex--
                        }
                        return subIndex + 1..index
                    }
                }
                index--
            }
            return null
        }

        private fun findIndexOfFirstRegularNumberToTheRightOf(startIndex: Int): IntRange? {
            var index = startIndex
            while (index < value.length) {
                when (value[index]) {
                    in '0'..'9' -> {
                        var subIndex = index
                        while (subIndex < value.length && value[subIndex] in '0'..'9') {
                            subIndex++
                        }
                        return index until subIndex
                    }
                }
                index++
            }
            return null
        }

        private fun explodeRegularNumberPairAt(pairRange: IntRange) {
            val (leftValue, rightValue) = value.substring(pairRange).trim('[', ']').split(',').map { it.toInt() }

            // "the pair's left value is added to the first regular number to the left of the exploding pair (if any)"
            findIndexOfFirstRegularNumberToTheRightOf(pairRange.last)?.apply {
                value.replaceRangeInline(this, value.substring(this).toInt().plus(rightValue).toString())
            }

            // "the entire exploding pair is replaced with the regular number 0."
            value.replaceRangeInline(pairRange, "0")

            // "the pair's right value is added to the first regular number to the right of the exploding pair (if any)"
            findIndexOfFirstRegularNumberToTheLeftOf(pairRange.first - 1)?.apply {
                value.replaceRangeInline(this, value.substring(this).toInt().plus(leftValue).toString())
            }
        }

        private fun findIndexOfFirstSplittableNumber(): IntRange? {
            var index = 0
            while (index < value.length) {
                when (value[index]) {
                    in '0'..'9' -> {
                        var subIndex = index
                        while (subIndex < value.length && value[subIndex] in '0'..'9') {
                            subIndex++
                        }
                        if (subIndex.minus(index) > 1) {
                            // any number with more than one digit is a match
                            return index until subIndex
                        }
                    }
                }
                index++
            }
            return null
        }

        private fun splitRegularNumberAt(range: IntRange) {
            val regularNumber = value.substring(range).toInt()
            val halfDown = regularNumber.div(2)
            val halfUp = regularNumber.plus(1).div(2)
            value.replaceRangeInline(range, "[$halfDown,$halfUp]")
        }

        private fun StringBuilder.replaceRangeInline(range: IntRange, replacement: String): StringBuilder {
            this.deleteRange(range.first, range.last + 1)
            this.insert(range.first, replacement)
            return this
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    fun testExplodeOnce(a: String): String {
        return a.asSnailfishNumber().reduce(maxSteps = 1, allowSplit = false).toString()
    }

    fun testSplitOnce(a: String): String {
        return a.asSnailfishNumber().reduce(maxSteps = 1, allowExplode = false).toString()
    }

    fun testReduce(a: String): String {
        return a.asSnailfishNumber().reduce().toString()
    }

    fun testPlus(a: String, b: String): String {
        return a.asSnailfishNumber().plus(b.asSnailfishNumber()).toString()
    }

    private fun String.asSnailfishNumber(): SnailfishNumber {
        return SnailfishNumber(this)
    }

    fun part1(input: List<String>): Int {
        return input
            .map { it.asSnailfishNumber() }
            .reduce(SnailfishNumber::plus)
            .magnitude()
    }

    fun part2(list: List<String>): Int {
        return list.indices.flatMap { i -> list.indices.map { j -> i to j } }
            .filter { (i, j) -> i != j }
            .maxOf { (i, j) ->
                val first = list[i].asSnailfishNumber()
                val second = list[j].asSnailfishNumber()
                first.plus(second).magnitude()
            }
    }

}

fun main() {
    val solver = Aoc2021Day18StringBuilder()
    val prefix = "aoc2021/aoc2021day18"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    // test logic
    verify("[[[[0,9],2],3],4]", solver.testExplodeOnce("[[[[[9,8],1],2],3],4]"))
    verify("[7,[6,[5,[7,0]]]]", solver.testExplodeOnce("[7,[6,[5,[4,[3,2]]]]]"))
    verify("[[6,[5,[7,0]]],3]", solver.testExplodeOnce("[[6,[5,[4,[3,2]]]],1]"))
    verify("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", solver.testExplodeOnce("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"))
    verify("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", solver.testExplodeOnce("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"))
    verify("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", solver.testSplitOnce("[[[[0,7],4],[15,[0,13]]],[1,1]]"))
    verify("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", solver.testSplitOnce("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"))
    verify("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", solver.testExplodeOnce("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"))
    verify("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", solver.testReduce("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"))
    verify("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", solver.testPlus("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]"))

    verify(4140, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(3993, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}