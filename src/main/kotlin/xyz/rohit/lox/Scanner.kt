package xyz.rohit.lox

import xyz.rohit.lox.TokenType.*

class Scanner(private var source: String) {
    private val tokens: MutableList<Token> = ArrayList()
    private var start = 0
    private var current = 0
    private var line = 1
    fun scanTokens(): MutableList<Token> {
        while (!isAtEnd()) {
            /* We are at the beginning of the next lexeme */
            start = current
            scanToken()
        }
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun isAtEnd() = (current >= source.length)

    private fun scanToken() {
        when (val c = advance()) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '!' -> addToken(
                when {
                    match('?') -> BANG_QUESTION
                    match('=') -> BANG_EQUAL
                    else -> BANG
                }
            )
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '/' -> {
                if (match('/')) {
                    // A comment goes till the end of the line
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(SLASH)
                }
            }
            ' '->{}
            '\r'->{}
            '\t'->{
                /* Ignore whitespace */
            }
            '\n'->{
                line++
            }
            '"'->{
                string()
            }
            else -> {
                KLox.error(line, "Unexpected Character")
            }
        }
    }

    private fun string(){
        while(peek() != '"' && !isAtEnd()){
            if(peek() == '\n')line++
            advance()
        }
        if(isAtEnd()){
            KLox.error(line,"Undetermined ")
        }
        // The closing ".
        advance()

        // Trim the surrounding quotes.
        // Current = at the index of closing '"' + 1( next of " )
        val value = source.substring(start + 1, current - 1)
        addToken(STRING, value)
    }
    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false
        current++
        return true
    }

    private fun peek(): Char {
        return if (isAtEnd()) {
            0.toChar()
        } else source[current]
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(tokenType: TokenType, literal: Any?) {
        val text = source.substring(current)
        tokens.add(Token(tokenType, text, literal, line))
    }
}