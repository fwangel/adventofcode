package se.brainleech.adventofcode.aoc2022

import se.brainleech.adventofcode.compute
import se.brainleech.adventofcode.readLines
import se.brainleech.adventofcode.verify

class Aoc2022Day07 {
    interface FileOrDirectory {
        val name: String
        val directory: Boolean
        fun size() : Int
    }

    data class File(override val name: String,
                    val size: Int,
                    override val directory: Boolean = false) : FileOrDirectory {
        override fun size() = size
    }

    data class Directory(override val name: String,
                         private var contents: MutableList<FileOrDirectory> = mutableListOf(),
                         override val directory: Boolean = true) : FileOrDirectory {
        fun addEntry(entry: String) {
            val (info, name) = entry.split(" ")
            when (info) {
                "dir" -> contents.add(Directory(name))
                else -> contents.add(File(name, info.toInt()))
            }
        }

        fun getDirectory(name: String) : Directory {
            return contents
                .filter { entry -> entry.directory }
                .filter { entry -> entry.name == name }
                .map { entry -> entry as Directory }
                .firstOrNull() ?: Directory(name)
        }

        override fun size() = contents.sumOf { entry -> entry.size() }
    }

    private infix fun String.invokes(command: String) = this.startsWith("$ $command")

    private fun String.listsAnything() = !this.startsWith("$ ")

    private fun String.argument() = this.substringAfterLast(' ')

    private fun MutableList<String>.root() = this.clear().also { this.add("/") }

    private fun MutableList<String>.parent() = this.removeLast()

    private fun MutableList<String>.cd(directory: String) = this.add(directory)

    private fun MutableList<String>.path() = "/" + this.joinToString("/").trimStart('/')

    private fun List<String>.toFileSystem() : MutableMap<String, Directory> {
        if (this.isEmpty()) return mutableMapOf()
        val lines = mutableListOf<String>().also { it.addAll(this) }.also { it.add("$ exit") }
        val fileSystem = mutableMapOf("/" to Directory("/"))
        val currentPath = mutableListOf("/")
        while (lines.isNotEmpty()) {
            val line = lines.removeFirst()
            if (line invokes "cd /") {
                currentPath.root()
            } else if (line invokes "cd ..") {
                currentPath.parent()
            } else if (line invokes "cd ") {
                val directoryName = line.argument()
                val subDirectory = fileSystem[currentPath.path()]!!.getDirectory(directoryName)
                currentPath.cd(directoryName)
                fileSystem.putIfAbsent(currentPath.path(), subDirectory)
            } else if (line invokes "ls") {
                val path = currentPath.path()
                while (lines.first().listsAnything()) {
                    fileSystem[path]!!.addEntry(lines.removeFirst())
                }
            }
        }
        return fileSystem
    }

    fun part1(input: List<String>) : Int {
        return input.toFileSystem().values
            .filter { dir -> dir.size() <= 100_000  }
            .sumOf { dir -> dir.size() }
    }

    fun part2(input: List<String>) : Int {
        val directories = input.toFileSystem().values
        val used = directories.first().size()
        val free = 70_000_000 - used

        return directories
            .filter { dir -> (free + dir.size()) >= 30_000_000 }
            .minOf { dir -> dir.size() }
    }

}

fun main() {
    val solver = Aoc2022Day07()
    val prefix = "aoc2022/aoc2022day07"
    val testData = readLines("$prefix.test.txt")
    val realData = readLines("$prefix.real.txt")

    verify(954_37, solver.part1(testData))
    compute({ solver.part1(realData) }, "$prefix.part1 = ")

    verify(24_933_642, solver.part2(testData))
    compute({ solver.part2(realData) }, "$prefix.part2 = ")
}