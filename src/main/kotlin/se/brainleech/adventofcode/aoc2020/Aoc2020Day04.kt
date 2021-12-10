package se.brainleech.adventofcode.aoc2020

class Aoc2020Day04 {

    companion object {
        private const val LINE_FEED = "\n"
        private const val PAIR_SEPARATOR = " "
        private const val KEY_VALUE_SEPARATOR = ":"
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

    enum class EyeColor {
        AMB,
        BLU,
        BRN,
        GRY,
        GRN,
        HZL,
        OTH;

        companion object {
            fun accepts(value: String): Boolean {
                return values().map { it.name }.contains(value)
            }
        }
    }

    data class Passport(private val data: Map<Field, String>) {
        private fun acceptedByHeight(height: String): Boolean {
            return when (height.takeLast(2)) {
                "cm" -> height.dropLast(2).toInt() in 150..193
                "in" -> height.dropLast(2).toInt() in 59..76
                else -> false
            }
        }

        fun acceptedByFieldKeys(): Boolean {
            // verify all fields are present, ignoring "Country ID"
            return data.filter { it.key != Field.CID }.size == Field.values().size - 1
        }

        fun acceptedByFieldValues(): Boolean {
            // verify all fields, ignoring "Country ID"
            var accepted = true
            accepted = accepted && (data.getValue(Field.BYR).toInt() in 1920..2002)
            accepted = accepted && (data.getValue(Field.IYR).toInt() in 2010..2020)
            accepted = accepted && (data.getValue(Field.EYR).toInt() in 2020..2030)
            accepted = accepted && (acceptedByHeight(data.getValue(Field.HGT)))
            accepted = accepted && (data.getValue(Field.HCL).matches(Regex("#[a-f0-9]{6}")))
            accepted = accepted && (EyeColor.accepts(data.getValue(Field.ECL).uppercase()))
            accepted = accepted && (data.getValue(Field.PID).matches(Regex("[0-9]{9}")))

            return accepted
        }
    }

    private fun String.asPassport(): Passport {
        val data = mutableMapOf<Field, String>()
        this.replace(LINE_FEED, PAIR_SEPARATOR).split(PAIR_SEPARATOR).forEach { pair ->
            val (key, value) = pair.split(KEY_VALUE_SEPARATOR)
            data[Field.valueOf(key.uppercase())] = value
        }
        return Passport(data.toMap())
    }

    fun part1(input: String): Long {
        return input
            .split("\n\n")
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.asPassport() }
            .filter { it.acceptedByFieldKeys() }
            .count()
            .toLong()
    }

    fun part2(input: String): Long {
        return input
            .split("\n\n")
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.asPassport() }
            .filter { it.acceptedByFieldKeys() }
            .filter { it.acceptedByFieldValues() }
            .count()
            .toLong()
    }

}
