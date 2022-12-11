package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day10 {

    companion object {
        const val DEBUG = false
        const val TRACE = false

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }
    }

    data class Crt(val width: Int = 40, val height: Int = 6) {
        private val pixels = ".".repeat(width * height).toCharArray()
        private var horizontal = 0
        private var vertical = 0
        private var spritePosition = 0

        fun updateBeam(cycle: Int) {
            horizontal = cycle % width
            vertical = cycle / width
            drawSprite()

            trace { println(this) }
        }

        fun updateSpritePosition(spritePosition: Int) {
            this.spritePosition = spritePosition
        }

        private fun drawSprite() {
            trace { println("h:$horizontal, v:$vertical, sprite pos:$spritePosition") }

            val pixelValue = if (horizontal in listOf(spritePosition - 1, spritePosition, spritePosition + 1)) '#' else '.'
            pixels[(vertical * width) + horizontal] = pixelValue
        }

        override fun toString(): String {
            return pixels.asSequence().chunked(width).map { it.joinToString("") }.joinToString("\n")
        }
    }

    data class Cpu(private var x: Int = 1, private val crt: Crt? = null) {
        private var clock = 0
        private var strength = 0
        private var justMeasured = false

        private fun then(block: () -> Unit) : Cpu {
            block()
            return this
        }

        private fun tick() : Cpu {
            justMeasured = false

            crt?.updateBeam(clock)

            clock++
            if (clock % 40 == 20) {
                strength += clock.times(x)
                justMeasured = true
            }
            return this
        }

        fun invoke(instruction: String, argument: String) {
            trace { println("cycle:$clock, $instruction $argument, current x: $x") }

            val strengthBefore = strength
            when (instruction) {
                "addx" -> tick().tick().then { x += argument.toInt() }.then { crt?.updateSpritePosition(x) }
                "noop" -> tick()
            }
            if (justMeasured) {
                debug { println("cycle:$clock, $instruction $argument => x: $x, sig:$strength (+${strength - strengthBefore})") }
            }
        }

        fun signalStrength() = strength

        fun currentImage() = crt.toString()
            .debug { println(crt) }

    }

    private fun process(input: List<String>, crt: Crt? = null) : Cpu {
        val cpu = Cpu(x = 1, crt)
        input.forEach {
            val instruction = it.substringBefore(' ')
            val argument = it.substringAfter(' ', "")
            cpu.invoke(instruction, argument)
        }
        return cpu
    }

    fun part1(input: List<String>) : Int {
        return process(input).signalStrength()
    }

    fun part2(input: List<String>) : String {
        return process(input, Crt(width = 40, height = 6)).currentImage()
    }

}

fun main() {
    val solver = Aoc2022Day10()
    val prefix = "aoc2022/aoc2022day10"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")
    val expectedImage = """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()

    verify(0, solver.part1(listOf("noop", "addx 3", "addx -5")))
    verify(13140, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(expectedImage, solver.part2(testData))
    compute({ solver.part2(realData).replace('#', '█') }, "$prefix.part2 = \n")
}