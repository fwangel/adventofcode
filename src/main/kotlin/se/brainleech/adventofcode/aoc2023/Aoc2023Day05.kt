package se.brainleech.adventofcode.aoc2023

import se.brainleech.adventofcode.*

class Aoc2023Day05 {

    companion object {
        const val DEBUG = false
        const val TRACE = false

        inline fun <T> T.debug(block: (T) -> Unit): T {
            if (DEBUG) block(this)
            return this
        }

        inline fun <T> T.trace(block: (T) -> Unit): T {
            if (TRACE) block(this)
            return this
        }
    }

    private data class Almanac(val seeds: List<LongRange>,
                               val seedToSoilMap: List<SourceTarget>,
                               val soilToFertilizerMap: List<SourceTarget>,
                               val fertilizerToWaterMap: List<SourceTarget>,
                               val waterToLightMap: List<SourceTarget>,
                               val lightToTemperatureMap: List<SourceTarget>,
                               val temperatureToHumidityMap: List<SourceTarget>,
                               val humidityToLocationMap: List<SourceTarget>) {
        private fun List<SourceTarget>.from(value: Long): Long {
            this.forEach { range ->
                if (range.source.contains(value)) {
                    val offset = value - range.source.first
                    return range.target.first + offset
                }
            }
            return value
        }

        fun lowestLocation(): Long {
            var minLocation = Long.MAX_VALUE
            val rangeIterator = seeds.iterator()
            while (rangeIterator.hasNext()) {
                val range = rangeIterator.next()
                var seed = range.first
                debug { println("range: $range") }
                while (seed <= range.last) {
                    trace { print("seed $seed => ") }
                    val soil = seedToSoilMap.from(seed).trace { print("soil $it => ") }
                    val fertilizer = soilToFertilizerMap.from(soil).trace { print("fertilizer $it => ") }
                    val water = fertilizerToWaterMap.from(fertilizer).trace { print("water $it => ") }
                    val light = waterToLightMap.from(water).trace { print("light $it => ") }
                    val temperature = lightToTemperatureMap.from(light).trace { print("temperature $it => ") }
                    val humidity = temperatureToHumidityMap.from(temperature).trace { print("humidity $it => ") }
                    val location = humidityToLocationMap.from(humidity).trace { print("location $it => ") }
                    trace { println("done!") }
                    if (location < minLocation) {
                        debug { println("Found new min. location: $location") }
                        minLocation = location
                    }
                    // FIXME skip as much as possible.. somehow
                    //       I expect my min.: 57451709
                    seed ++
                }
            }
            return minLocation
        }
    }

    private data class SourceTarget(val source: LongRange, val target: LongRange)

    private fun Map<Long, Long>.maxValue(): Long = maxOf(this.keys.max(), this.values.max())

    private fun String.toSourceTarget(): SourceTarget {
        val values = this.toListOfLongs(" ")
        return SourceTarget(
            LongRange(values[1], values[1] + values[2] - 1),
            LongRange(values[0], values[0] + values[2] - 1)
        )
    }

    private fun solve(input: List<String>, seedsAsRanges: Boolean = false): Long {
        val seeds = mutableListOf<LongRange>()
        val seedToSoilRules = mutableListOf<SourceTarget>()
        val soilToFertilizerRules = mutableListOf<SourceTarget>()
        val fertilizerToWaterRules = mutableListOf<SourceTarget>()
        val waterToLightRules = mutableListOf<SourceTarget>()
        val lightToTemperatureRules = mutableListOf<SourceTarget>()
        val temperatureToHumidityRules = mutableListOf<SourceTarget>()
        val humidityToLocationRules = mutableListOf<SourceTarget>()
        var currentList: MutableList<SourceTarget> = seedToSoilRules

        input
            .asSequence()
            .filter { it.isNotEmpty() }
            .forEach {
                       if (it.startsWith("seeds: ") && !seedsAsRanges) { seeds.addAll(it.replace("seeds: ", "").toListOfLongs(" ").map { start -> LongRange(start, start) })
                } else if (it.startsWith("seeds: ") && seedsAsRanges) { seeds.addAll(it.replace("seeds: ", "").toListOfLongs(" ").chunked(2) { startAndLength ->
                           LongRange(
                               startAndLength.first(),
                               startAndLength.first() + startAndLength.last()
                           )
                       })
                } else if (it.startsWith("seed-to-soil-map")) { currentList = seedToSoilRules
                } else if (it.startsWith("soil-to-fertilizer")) { currentList = soilToFertilizerRules
                } else if (it.startsWith("fertilizer-to-water")) { currentList = fertilizerToWaterRules
                } else if (it.startsWith("water-to-light")) { currentList = waterToLightRules
                } else if (it.startsWith("light-to-temperature")) { currentList = lightToTemperatureRules
                } else if (it.startsWith("temperature-to-humidity")) { currentList = temperatureToHumidityRules
                } else if (it.startsWith("humidity-to-location")) { currentList = humidityToLocationRules
                } else if (it.first().isDigit()) { currentList.add(it.toSourceTarget())
                }
            }

        val almanac = Almanac(
            seeds.sortedBy { it.first },
            seedToSoilRules.sortedBy { it.source.first },
            soilToFertilizerRules.sortedBy { it.source.first },
            fertilizerToWaterRules.sortedBy { it.source.first },
            waterToLightRules.sortedBy { it.source.first },
            lightToTemperatureRules.sortedBy { it.source.first },
            temperatureToHumidityRules.sortedBy { it.source.first },
            humidityToLocationRules.sortedBy { it.source.first }
        )

        debug { println(almanac) }

        return almanac.lowestLocation()
    }

    fun part1(input: List<String>) : Long {
        return solve(input)
    }

    fun part2(input: List<String>) : Long {
        return solve(input, true)
    }

}

fun main() {
    val solver = Aoc2023Day05()
    val prefix = "aoc2023/aoc2023day05"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(35L, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(46, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}