package h.code.country

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class RepoReader(val filePath: String) {
    fun read() {
        val process = ProcessBuilder(gitCommand, "log").directory(File(filePath)).start();
        val input = BufferedReader(InputStreamReader(process.inputStream))
        val errorInput = BufferedReader(InputStreamReader(process.errorStream))
        val lines = input.lines()
        for (line in lines) {
            println(line)
        }
        val errors = errorInput.lines()
        for (error in errors) {
            println(error)
        }
        process.waitFor()
    }
}
