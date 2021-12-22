package se.brainleech.adventofcode.aoc2021

import java.util.stream.Stream
import kotlin.streams.toList

class Aoc2021Day18 {

    // [[1,2],[3,[4,[5,[6,7]]]]]
    // ==> [[1,2],[3,[4,[11,0]]]]
    // ==> [[1,2],[3,[4,[[5,6]],0]]]]
    // ==> [[1,2],[3,[[9,6]]]]
    //
    //    /\               /\               /\             /\
    //   /  \             /  \             /  \           /  \
    //  /\   \           /\   \           /\   \         /\   \
    // 1  2  /\    ==>  1  2  /\    ==>  1  2  /\   ==> 1  2  /\   ==> done?
    //      3 /\             3 /\             3 /\           3 /\
    //       4 /\             4 /\             4 /\           9  6
    //        5 /\             11 0             /\ 0
    //         6  7                            5  6

    data class SnailfishNumber(
        var regularNumber: Int? = null,
        var leftChild: SnailfishNumber? = null,
        var rightChild: SnailfishNumber? = null,
        var parentNode: SnailfishNumber? = null
    ) {
        override fun toString(): String {
            return buildString {
                if (hasLeftChild) append("[" + leftChild!!.toString())
                if (hasRightChild) append("," + rightChild!!.toString() + "]")
                if (hasRegularNumber) append(regularNumber!!)
            }
        }

        private val hasRegularNumber get() = regularNumber != null
        private val hasLeftChild get() = leftChild != null
        private val hasRightChild get() = rightChild != null
        private val hasParentNode get() = parentNode != null

        fun magnitude(): Int {
            return regularNumber ?: 3.times(leftChild!!.magnitude()).plus(2.times(rightChild!!.magnitude()))
        }

        private fun disconnectChildren() {
            leftChild = null
            rightChild = null
        }

        fun reduce(maxSteps: Int = Int.MAX_VALUE, allowExplode: Boolean = true, allowSplit: Boolean = true): SnailfishNumber {
            val context = this
            var stepsCompleted = 0
            do {
                var actionPerformed = false
                if (allowExplode) firstExplodableNode()?.apply {
                    explodeRegularNumberPair()
                    actionPerformed = true
                    if (++stepsCompleted >= maxSteps) return context
                }

                if (allowSplit && !actionPerformed) firstSplittableNode()?.apply {
                    splitRegularNumber()
                    actionPerformed = true
                    if (++stepsCompleted >= maxSteps) return context
                }

            } while (actionPerformed)

            return this
        }

        /**
         * The pair's _left_ value is added to the _first regular number_ to the left of the exploding pair (if any),
         * the pair's _right_ value is added to the first regular number to the right of the exploding pair (if any).
         * Exploding pairs will always consist of two regular numbers.
         * Then, the entire exploding pair is replaced with the regular number 0.
         */
        private fun explodeRegularNumberPair() {
            val leftRegularNumber = leftChild?.regularNumber ?: 0
            val rightRegularNumber = rightChild?.regularNumber ?: 0
            disconnectChildren()

            firstRegularNumberOnLeft()?.increaseRegularNumberWith(leftRegularNumber)
            firstRegularNumberOnRight()?.increaseRegularNumberWith(rightRegularNumber)

            regularNumber = 0
        }

        private fun increaseRegularNumberWith(other: Int) {
            regularNumber = regularNumber?.plus(other)
        }

        private val halfDown get() = regularNumber?.div(2)
        private val halfUp get() = regularNumber?.plus(1)?.div(2)

        private fun splitRegularNumber() {
            leftChild = SnailfishNumber(regularNumber = halfDown, parentNode = this)
            rightChild = SnailfishNumber(regularNumber = halfUp, parentNode = this)
            regularNumber = null
        }

        private fun firstRegularNumberOnLeft(): SnailfishNumber? {
            if (hasRegularNumber) return this
            if (hasParentNode) {
                if (this === parentNode?.leftChild) return parentNode!!.firstRegularNumberOnLeft()
                if (this === parentNode?.rightChild) return parentNode!!.leftChild?.rightMostNode()
            }
            return null
        }

        private fun firstRegularNumberOnRight(): SnailfishNumber? {
            if (hasRegularNumber) return this
            if (hasParentNode) {
                if (this === parentNode?.leftChild) return parentNode!!.rightChild?.leftMostNode()
                if (this === parentNode?.rightChild) return parentNode!!.firstRegularNumberOnRight()
            }
            return null
        }

        private fun leftMostNode(): SnailfishNumber? {
            return if (hasRegularNumber) this else leftChild?.leftMostNode()
        }

        private fun rightMostNode(): SnailfishNumber? {
            return if (hasRegularNumber) this else rightChild?.rightMostNode()
        }

        private fun firstExplodableNode(depth: Int = 0): SnailfishNumber? {
            return if (depth >= 4 && !hasRegularNumber) this
            else leftChild?.firstExplodableNode(depth + 1) ?: rightChild?.firstExplodableNode(depth + 1)
        }

        private fun firstSplittableNode(): SnailfishNumber? {
            return if (hasRegularNumber && regularNumber!! >= 10) this
            else leftChild?.firstSplittableNode() ?: rightChild?.firstSplittableNode()
        }

        fun plus(other: SnailfishNumber): SnailfishNumber {
            val newNode = SnailfishNumber(leftChild = this, rightChild = other)
            newNode.leftChild?.parentNode = newNode
            newNode.rightChild?.parentNode = newNode
            newNode.reduce()
            return newNode
        }
    }

    private fun String.asTreeNode(): SnailfishNumber {
        // [[1,2],[3,[4,5]]]

        val input = this
        var index = 0
        var nodeCount = 0

        fun asTreeNodeWithParent(parent: SnailfishNumber? = null): SnailfishNumber {
            while (input[index] == ',' || input[index] == ']') {
                index++
            }
            val newNode = SnailfishNumber(parentNode = parent)
            when (input[index++]) {
                in '0'..'9' -> {
                    val startIndex = index - 1
                    while (input[index] in '0'..'9') {
                        index++
                    }
                    newNode.regularNumber = input.substring(startIndex, index).toInt()
                }
                '[' -> {
                    newNode.leftChild = asTreeNodeWithParent(newNode)
                    newNode.rightChild = asTreeNodeWithParent(newNode)
                }
            }
            nodeCount++
            return newNode
        }

        return asTreeNodeWithParent()
    }

    fun testExplodeOnce(input: String): String {
        return input.asTreeNode().reduce(maxSteps = 1, allowSplit = false).toString()
    }

    fun testSplitOnce(input: String): String {
        return input.asTreeNode().reduce(maxSteps = 1, allowExplode = false).toString()
    }

    fun testReduce(input: String): String {
        return input.asTreeNode().reduce().toString()
    }

    fun testPlus(first: String, second: String): String {
        return first.asTreeNode().plus(second.asTreeNode()).toString()
    }

    fun part1(input: Stream<String>): Int {
        return input.toList()
            .map { it.asTreeNode() }
            .reduce(SnailfishNumber::plus)
            .magnitude()
    }

    fun part2(input: Stream<String>): Int {
        val list = input.toList()
        return list.indices.flatMap { i -> list.indices.map { j -> i to j } }
            .filter { (i, j) -> i != j }
            .maxOf { (i, j) ->
                val first = list[i].asTreeNode()
                val second = list[j].asTreeNode()
                first.plus(second).magnitude()
            }
    }

}
