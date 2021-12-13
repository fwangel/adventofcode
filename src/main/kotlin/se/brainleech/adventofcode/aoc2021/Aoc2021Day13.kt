package se.brainleech.adventofcode.aoc2021

class Aoc2021Day13 {

    companion object {
        private const val DOUBLE_LINE = "\n\n"
        private const val NUMBER_SEPARATOR = ","
        private const val FOLD_ALONG = "fold along "
        private const val EMPTY = ""
        private const val EQUAL_SIGN = "="
        private const val GRID_DOT = "#"
        private const val GRID_NOT = "."
    }

    data class Dot(var x: Int, var y: Int) : Comparable<Dot> {
        companion object {
            fun toKey(x: Int, y: Int): String = "$x,$y"
        }

        override operator fun compareTo(other: Dot): Int {
            return when {
                this === other -> 0
                this.y > other.y -> 1
                this.y < other.y -> -1
                this.x > other.x -> 1
                this.x < other.x -> -1
                else -> 0
            }
        }

        fun toKey(): String = toKey(x, y)
    }

    enum class Axis {
        X, Y
    }

    data class Fold(val axis: Axis, val pointOnAxis: Int)

    class Paper(private val dots: List<Dot>, private val folds: List<Fold>) {
        private var width: Int = maxX()
        private var height: Int = maxY()

        private fun maxX(): Int {
            return dots.maxOf { it.x }
        }

        private fun maxY(): Int {
            return dots.maxOf { it.y }
        }

        private fun foldAlong(fold: Fold) {
            when (fold.axis) {
                Axis.X -> {
                    dots.filter { dot -> dot.x > fold.pointOnAxis }.forEach { it.x = width - it.x }
                    width = fold.pointOnAxis - 1
                }
                Axis.Y -> {
                    dots.filter { dot -> dot.y > fold.pointOnAxis }.forEach { it.y = height - it.y }
                    height = fold.pointOnAxis - 1
                }
            }
        }

        fun distinctDots(): List<Dot> {
            return dots.sorted().distinct()
        }

        fun foldOnce(): Paper {
            return foldAtMost(1)
        }

        fun foldAll(): Paper {
            return foldAtMost(Int.MAX_VALUE)
        }

        private fun foldAtMost(maxSteps: Int): Paper {
            folds
                .take(maxSteps)
                .forEach { fold -> foldAlong(fold) }

            return this
        }

        fun toAsciiArt(): String {
            return buildString {
                val grid = distinctDots()
                    .groupBy(keySelector = { it.toKey() }, valueTransform = { it })
                    .mapValues { GRID_DOT }
                    .withDefault { GRID_NOT }
                for (y in 0..height) {
                    for (x in 0..width) {
                        append(grid.getValue(Dot.toKey(x, y)))
                    }
                    appendLine()
                }
            }.trim()
        }

    }

    private fun String.asPaper(): Paper {
        val (dotPositions, foldingInstructions) = this.split(DOUBLE_LINE)

        val paper = Paper(
            dotPositions.lines().map { line ->
                val (x, y) = line.split(NUMBER_SEPARATOR).map { it.toInt() }
                Dot(x, y)
            }.sorted().toList(),

            foldingInstructions.lines()
                .map { it.replace(FOLD_ALONG, EMPTY).split(EQUAL_SIGN) }
                .map { fold -> Fold(Axis.valueOf(fold.first().uppercase()), fold.last().toInt()) }
                .toList()
        )

        return paper
    }

    fun part1(input: String): Int {
        return input.asPaper().foldOnce().distinctDots().size
    }

    fun part2(input: String): String {
        return input.asPaper().foldAll().toAsciiArt()
    }

}
