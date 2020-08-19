package h.code.country

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class RepoReader(private val directory: String) {
    private val currentTimeZone: ZoneId = OffsetDateTime.now().toZonedDateTime().zone

    fun readLog() {
        val commitHashes = GitProcess.readLog(directory).reversed()
        println(commitHashes)
        var olderCommitHash = GitProcess.emptyHash
        for (commitHash in commitHashes) {
            val commitDate = ZonedDateTime.ofInstant(GitProcess.readCommitDate(directory, commitHash), currentTimeZone)
            val diffSummary = GitProcess.readDiffSummary(directory, Pair(olderCommitHash, commitHash))
            println("$commitHash $commitDate $diffSummary")
            olderCommitHash = commitHash
        }
    }
}
