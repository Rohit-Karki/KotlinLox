package xyz.rohit.lox

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

/**
 * @author Rohit-Karki on 09/09/23
 */

var hadError = false
fun main(args: Array<String>) {
    when {
        args.size > 1 -> {
            println("Usage: klox [script]")
            System.exit(64)
        }

        args.size == 1 -> runFile(args[0])
        else -> runPrompt()
    }
}

private fun runFile(path: String) {
    val bytes = Files.readAllBytes(Paths.get(path))
    if(hadError) exitProcess(65)
}

private fun runPrompt() {
    val input = InputStreamReader(System.`in`)
    val reader = BufferedReader(input)
    while (true) {
        println("> ")
        val line = reader.readLine() ?: break
        run(line)
        hadError = false
    }
}

private fun run(source: String) {
    val scanner = Scanner(source)
    val tokens : MutableList<Token> = scanner.scanTokens()
    for (token in tokens) {
        println(token)
    }

}

object KLox {
    fun error(line: Int, string: String) {
        report(line, "", string)
    }

    private fun report(line: Int, where: String, message: String) {
        println("[PR][Line $line] Error$where: $message")
        hadError = true
    }
}