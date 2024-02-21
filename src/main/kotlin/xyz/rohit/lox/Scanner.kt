package xyz.rohit.lox

import xyz.rohit.lox.TokenType.*

// ktlint-disable no-wildcard-imports

class Scanner(private var source: String) {
    private val keywords = hashMapOf(
        "and" to AND,
        "class" to CLASS,
        "else" to ELSE,
        "false" to FALSE,
        "for" to FOR,
        "fun" to FUN,
        "if" to IF,
        "nil" to NIL,
        "or" to OR,
        "print" to PRINT,
        "return" to RETURN,
        "super" to SUPER,
        "this" to THIS,
        "true" to TRUE,
        "var" to VAR,
        "while" to WHILE,
        /*"break" to BREAK,
        "continue" to CONTINUE,*/
    )

    private val tokens: MutableList<Token> = ArrayList()
    private var start = 0
    private var current = 0
    private var line = 1
    fun scanTokens(): MutableList<Token> {
        while (!isAtEnd) {
            /* We are at the beginning of the next lexeme */
            start = current
            scanToken()
        }
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private val isAtEnd: Boolean
        get() = current >= source.length

    private fun scanToken() {
        val c = advance()
        when (c) {
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
                },
            )
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '/' -> {
                if (match('/')) {
                    // A comment goes till the end of the line
                    while (peek() != '\n' && !isAtEnd) advance()
                } else {
                    addToken(SLASH)
                }
            }
            /* Ignore whitespace */
            ' ', '\r', '\t' -> {}
            '\n' -> {
                line++
            }
            '"' -> {
                string()
            }

            in '0'..'9' -> {
                number()
            }

            in 'a'..'z', in 'A'..'z' -> identifier()

            else -> KLox.error(line, "Unexpected Character")
        }
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd) {
            if (peek() == '\n')line++
            advance()
        }
        if (isAtEnd) {
            KLox.error(line, "Undetermined ")
        }
        // The closing ".
        advance()

        // Trim the surrounding quotes.
        // Current = at the index of closing '"' + 1( next of " )
        val value = source.substring(start + 1, current - 1)
        addToken(STRING, value)
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd || source[current] != expected) return false
        current++
        return true
    }

    private fun peek(): Char {
        return if (isAtEnd) {
            0.toChar()
        } else {
            source[current]
        }
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length)return 0.toChar()

        return source[current + 1]
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun number() {
        while (peek() in '0'..'9')
            advance()
        // Check for fractional part
        if (peek() == '.' && peekNext() in '0'..'9') {
            // Consume the decimal digit
            advance()
            while (peek() in '0'..'9') {
                advance()
            }
        }
        addToken(NUMBER, source.substring(start, current).toDouble())
    }

    private fun identifier() {
        while (isAlphaNumeric(peek()))advance()

        val text = source.substring(start, current)
        val type = keywords[text] ?: IDENTIFIER
        addToken(type)
    }

    private fun isAlpha(c: Char): Boolean {
        return (c in 'a'..'z') || (c in 'A'..'Z') || c == '_'
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c)
    }

    private fun addToken(tokenType: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(tokenType, text, literal, line))
    }
}

//  1 + / 3
/*
    expression  -> binary
                -> expression operator expression
                -> 1 + expression
                -> 1 + unary
                -> 1 +
*/
