package h.code.country

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.time.Instant
import java.util.stream.Collectors

class GitProcess(private val command: List<String>, private val directory: String) {
    class Exception(val command: List<String>, val errorLines: List<String>) : java.lang.Exception("Git exception")
    class DiffSummaryRow(val insertionCount: Long, val deletionCount: Long, val filePath: String) {
        override fun toString(): String =
                "+$insertionCount -$deletionCount $filePath"
    }
    class LogEntryRow(val commitHash: String, val parentHashes: List<String>) {
        override fun toString(): String {
            return "$commitHash <- $parentHashes"
        }
    }

    companion object {
        private const val commandName = "git"

        /** @return list of commit hashes */
        fun readLog(directory: String): List<LogEntryRow> {
            val lines = GitProcess(listOf(commandName, "log", "--format=%H %P"), directory).run()
            return lines.stream().filter { line -> line.isNotBlank() }
                    .map { line -> line.split(' ')}
                    .map { parts -> LogEntryRow(parts[0], parts.subList(1, parts.size) )}
                    .collect(Collectors.toList())
        }

        fun readCommitDate(directory: String, commitHash: String): Instant {
            val process = GitProcess(listOf(commandName, "show", "--format=%at", "--quiet", commitHash), directory)
            return Instant.ofEpochSecond(process.run().joinToString().toLong())
        }

        /** @return (insertions, deletions, filePath) */
        fun readDiffSummary(directory: String, commitHashes: Pair<String, String>): List<DiffSummaryRow> {
            val command = listOf(commandName, "diff", "--numstat", commitHashes.first, commitHashes.second)
            val lines = GitProcess(command, directory).run()
            return lines.stream()
                    .filter { line -> line.isNotBlank() }
                    .map { line -> line.split(' ', '\t') }
                    .map { parts ->
                        DiffSummaryRow(
                            diffSummaryPartToLong(parts[0]),
                            diffSummaryPartToLong(parts[1]),
                            parts[2]
                        )
                    }
                    .collect(Collectors.toList())
        }

        private fun diffSummaryPartToLong(text: String): Long {
            return if (text == "-") -1 else text.toLong()
        }
    }

    fun run(): List<String> {
        val process = ProcessBuilder(command).directory(File(directory)).start()
        val standardInput = BufferedReader(InputStreamReader(process.inputStream))
        val errorInput = BufferedReader(InputStreamReader(process.errorStream))
        val inputLines = standardInput.readLines()
        val errorLines = errorInput.readLines()
        if (errorLines.isNotEmpty())
            throw Exception(command, errorLines)
        return inputLines
    }
}
