package xyz.rohit.lox

import xyz.rohit.lox.TokenType.*

class Scanner(private var source: String) {
    private val keywords = hashMapOf<String, TokenType>()
    init {
        keywords["and"] = AND
        keywords["class"] = CLASS;
        keywords["else"] = ELSE;
        keywords["false"] = FALSE;
        keywords["for"] = FOR;
        keywords["fun"] = FUN;
        keywords["if"] = IF;
        keywords["nil"] = NIL;
        keywords["or"] = OR;
        keywords["print"] = PRINT;
        keywords["return"] = RETURN;
        keywords["super"] = SUPER;
        keywords["this"] = THIS;
        keywords["true"] = TRUE;
        keywords["var"] = VAR;
        keywords["while"] = WHILE;
    }
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
                if(isDigit(c)){
                    number()
                }else if(isAlphaNumeric(c)){
                    identifier()
                }
                else {
                    KLox.error(line, "Unexpected Character")
                }
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

    private fun peekNext(): Char{
        if(current + 1 >= source.length)return 0.toChar()

        return source[current + 1]
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun number(){
        while(isDigit(peek())){
            advance()
        }
        // Check for fractional part
        if(peek() == '.'){
            // Consume the decimal digit
            advance()
            while(isDigit(peek())){
                advance()
            }
            addToken(NUMBER, source.substring(start, current).toDouble())
        }
    }

    private fun identifier(){
        while(isAlphaNumeric(peek()))advance()

        val text = source.substring(start, current)
        var type = keywords[text]
        if(type == null) type = IDENTIFIER
        addToken(type)
    }

    private fun isAlpha(c: Char): Boolean{
        return (c in 'a'..'z') || (c in 'A'..'Z') || c == '_'
    }

    private fun isAlphaNumeric(c: Char): Boolean{
        return isAlpha(c) || isDigit(c)
    }
    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(tokenType: TokenType, literal: Any?) {
        val text = source.substring(current)
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