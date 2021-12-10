package se.brainleech.adventofcode.aoc2020

class Aoc2020Day04 {

    companion object {
    }

    enum class Field {
        BYR,
        IYR,
        EYR,
        HGT,
        HCL,
        ECL,
        PID,
        CID
    }

    fun part1(input: String): Long {
        return input
            .split("\n\n")
            .asSequence()
            .filter { it.isNotBlank() }
            .map { passport ->
                passport
                    .replace('\n', ' ')
                    .split(' ')
                    .map { pair ->
                        val (key, value) = pair.split(':')
                        mapOf(Field.valueOf(key.uppercase()) to value)
                    }
                    .filterNot { it.containsKey(Field.CID) } // ignore "Country ID"
            }
            .filter { passport ->
                // verify all fields are present, ignoring "Country ID"
                passport.count() == Field.values().size - 1
            }
            .count()
            .toLong()
    }

    fun part2(input: String): Long {
        return -1L
    }

}
