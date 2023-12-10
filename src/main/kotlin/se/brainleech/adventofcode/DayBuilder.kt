package se.brainleech.adventofcode

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

fun createDay(year: Int, day: Int) {
    val basePackage = "package se.brainleech.adventofcode"
    val codeParent = "src/main/kotlin/se/brainleech/adventofcode"
    val testParent = "src/main/resources/aoc${year}"
    val codeTemplate = File(codeParent, "DayTemplate.kt").readText()

    val codeFolder = String.format("%s/aoc%d", codeParent, year)
    val packageName = String.format("%s.aoc%d", basePackage, year)
    val className = String.format("Aoc%dDay%02d", year, day)
    val classFilename = "$className.kt"
    val classFile = File(codeFolder, classFilename)
    if (!classFile.exists()) {
        println("Creating ${codeFolder}/${classFilename} ...")
        val classCode = codeTemplate
            .replace("<year>", year.toString())
            .replace("<day>", day.toString().padStart(2, '0'))
            .replace("DayTemplate", className)
            .replace(basePackage, packageName)
        classFile.toPath().parent.createDirectories()
        classFile.writeText(classCode)
    }

    Path(testParent).createDirectories()
    val realDataFile = File(testParent, className.lowercase() + ".real.txt")
    if (!realDataFile.exists()) {
        println("   and ${testParent}/${className.lowercase()}.real.txt ...")
        realDataFile.writeText("")
    }

    val testDataFile = File(testParent, className.lowercase() + ".test.txt")
    if (!testDataFile.exists()) {
        println("   and ${testParent}/${className.lowercase()}.test.txt ...")
        testDataFile.writeText("")
    }
}

fun main() {
    val year = 2023
    for (day in 1..25) {
        createDay(year, day)
    }
    println("Done!")
}