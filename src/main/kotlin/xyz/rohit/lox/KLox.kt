package xyz.rohit.lox

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

/**
 * @author Rohit-Karki on 09/09/23
 * **/

var hadError = false
var hadRuntimeError = false
fun main(args: Array<String>) {
    when {
        args.size > 1 -> {
            println("Usage: klox [script]")
            exitProcess(64)
        }
        args.size == 1 -> runFile(args[0])
        else -> runPrompt()
    }
}

private fun runFile(path: String) {
    val bytes = Files.readAllBytes(Paths.get(path))
    if (hadError) exitProcess(65)
    if (hadRuntimeError) exitProcess(70)
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
    val tokens: MutableList<Token> = scanner.scanTokens()
    println(tokens.toString())
    val parser = Parser(tokens)
    val interpreter = Interpreter()
    val statements = parser.parse()
    interpreter.interpret(statements)
}

object KLox {
    val interpreter = Interpreter()
    fun error(token: Token, message: String) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message)
        } else {
            report(token.line, " at '" + token.lexeme + "'", message)
        }
    }
    fun error(line: Int, string: String) {
        report(line, "", string)
    }

    private fun report(line: Int, where: String, message: String) {
        println("[PR][Line $line] Error$where: $message")
        hadError = true
    }
}
