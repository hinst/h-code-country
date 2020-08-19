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
    class CommitSummaryRow(val hash: String, val parentHashes: List<String>)

    companion object {
        private const val commandName = "git"
        const val emptyHash = "4b825dc642cb6eb9a060e54bf8d69288fbee4904"

        /** @return list of commit hashes */
        fun readLog(directory: String): List<String> =
                GitProcess(listOf(commandName, "log", "--format=%H"), directory).run()

        fun readCommitDate(directory: String, commitHash: String): Instant {
            val process = GitProcess(listOf(commandName, "show", "--format=%at", "--quiet", commitHash), directory)
            return Instant.ofEpochSecond(process.run().joinToString().toLong())
        }

        /** @return (insertions, deletions, filePath) */
        fun readDiffSummary(directory: String, commitHashes: Pair<String, String>): List<DiffSummaryRow> {
            val process = GitProcess(listOf(commandName, "diff", "--numstat", commitHashes.first, commitHashes.second), directory)
            return process.run().stream()
                    .filter { line -> line.isNotBlank() }
                    .map { line -> line.split(' ', '\t') }
                    .map { parts -> DiffSummaryRow(parts[0].toLong(), parts[1].toLong(), parts[2]) }
                    .collect(Collectors.toList())
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
