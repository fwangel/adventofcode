package se.brainleech.adventofcode.aoc2021

import java.util.regex.Pattern
import java.util.stream.Stream
import kotlin.streams.toList

class Aoc2021Day04 {

    data class Marker(val number: Int, var played: Boolean = false)

    class Board(
        private val columns: Int,
        private val rows: Int,
        private var markers: Array<Marker>,
        private var won: Boolean = false,
        var winningNumber: Int = 0,
        var score: Int = 0
    ) {
        init {
            score = markers.sumOf { it.number }
        }

        fun completed(): Boolean {
            if (won) {
                return true
            }

            for (y in 0 until rows) {
                var rowPlayed = true
                for (x in 0 until columns) {
                    rowPlayed = rowPlayed && markers[y * rows + x].played
                }
                if (rowPlayed) {
                    return true
                }
            }

            for (x in 0 until columns) {
                var columnPlayed = true
                for (y in 0 until rows) {
                    columnPlayed = columnPlayed && markers[y * rows + x].played
                }
                if (columnPlayed) {
                    return true
                }
            }

            return false
        }

        fun play(number: Int) {
            val marker = markers.find { it.number == number }
            if (marker != null && !marker.played) {
                marker.played = true
                if (!won) {
                    score -= number
                }
            }
            if (!won && completed()) {
                won = true
                winningNumber = number
            }
        }
    }

    class Game(
        var numbers: IntArray = intArrayOf(),
        private var boards: MutableList<Board> = mutableListOf(),
        var lastCompleted: Board? = null
    ) {
        fun addBoard(board: Board) {
            boards.add(board)
        }

        fun play(number: Int) {
            boards.forEach {
                it.play(number)

                if (it.completed() && it.winningNumber == number) {
                    lastCompleted = it
                }
            }
        }
    }

    private fun String.toIntArray(): IntArray {
        return this.split(",").map { it.toInt() }.toIntArray()
    }

    private fun parse(lines: List<String>): Game {
        val game = Game()
        var boardColumns = 0
        var boardRows = 0
        var currentBoard: MutableList<Int> = mutableListOf()

        lines.forEachIndexed { index, line ->

            when {
                index == 0 -> {
                    game.numbers = lines[0].toIntArray()
                }
                line.isBlank() -> {
                    if (currentBoard.isNotEmpty()) {
                        // convert and add to game
                        game.addBoard(
                            Board(
                                boardColumns,
                                boardRows,
                                currentBoard.map { Marker(it) }.toTypedArray()
                            )
                        )
                    }
                    currentBoard = mutableListOf()
                    boardRows = 0
                    boardColumns = 0

                }
                else -> {
                    val row =
                        line.trim().split(Pattern.compile("\\s+")).map { it.trim().toInt() }
                            .toList()
                    if (boardColumns == 0) {
                        boardColumns = row.size
                    }
                    currentBoard.addAll(row)
                    boardRows++
                }
            }
        }

        if (currentBoard.isNotEmpty()) {
            // convert and add to game
            game.addBoard(
                Board(
                    boardColumns,
                    boardRows,
                    currentBoard.map { Marker(it) }.toTypedArray()
                )
            )
        }

        return game
    }

    fun part1(input: Stream<String>): Int {
        val game = parse(input.toList())

        game.numbers.forEach {
            game.play(it)

            val lastCompleted = game.lastCompleted
            if (lastCompleted != null) {
                return lastCompleted.score * lastCompleted.winningNumber
            }
        }

        return -1
    }

    fun part2(input: Stream<String>): Int {
        val game = parse(input.toList())

        game.numbers.forEach {
            game.play(it)
        }

        val lastCompleted = game.lastCompleted
        if (lastCompleted != null) {
            return lastCompleted.score * lastCompleted.winningNumber
        }

        return -1
    }

}
