package h.code.country

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class RepoReader(private val directory: String) {
    private val currentTimeZone: ZoneId = OffsetDateTime.now().toZonedDateTime().zone

    fun readLog() {
        val logEntries = GitProcess.readLog(directory).reversed()
        for (logEntry in logEntries) {
            val commitHash = logEntry.commitHash
            val commitDate = ZonedDateTime.ofInstant(GitProcess.readCommitDate(directory, commitHash), currentTimeZone)
            val diffSummary = GitProcess.readDiffSummary(directory, Pair(logEntry.parentHashes[0], commitHash))
            println("$logEntry $commitDate $diffSummary")
        }
    }
}
