package xyz.rohit.lox

class Parser(tokens: List<Token>) {

    private class ParseError() : RuntimeException()
    private var tokens: MutableList<Token>
    var current: Int = 0

    init {
        this.tokens = tokens.toMutableList()
    }

    private fun synchronize() {
        advance()
        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON)return
            when (peek().type) {
                TokenType.CLASS, TokenType.FUN, TokenType.VAR, TokenType.FOR, TokenType.IF, TokenType.WHILE, TokenType.PRINT -> {
                }
                TokenType.RETURN -> return
                else -> {
                }
            }
            advance()
        }
    }

    private fun parse(): Expr? {
        return try {
            expression()
        } catch (error: ParseError) {
            null
        }
    }
    private fun comparision(): Expr {
        var expr: Expr = term()
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun equality(): Expr {
        var expr: Expr = comparision()
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparision()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }
    private fun term(): Expr {
        var expr = factor()
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }
    private fun factor(): Expr {
        var expr = unary()
        while (match(TokenType.SLASH, TokenType.STAR)) {
            val operator = previous()
            val right: Expr = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }
    private fun unary(): Expr {
        if (match(TokenType.MINUS, TokenType.BANG)) {
            val operator = previous()
            val right = primary()
            return Expr.Unary(operator, right)
        }
        return primary()
    }
    private fun primary(): Expr {
        if (match(TokenType.FALSE))return Expr.Literal(false)
        if (match(TokenType.TRUE))return Expr.Literal(true)
        if (match(TokenType.NIL))return Expr.Literal(null)
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return Expr.Literal(previous().literal!!)
        }
        if (match(TokenType.LEFT_PAREN)) {
            val expr = expression()
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }
        throw error(peek(), "Expect expression")
    }

    private fun expression(): Expr {
        return equality()
    }

    /*private fun statement(): Stmt {
        if (match(TokenType.PRINT))return printStatement()
        return expressionStatement()
    }*/
    private fun match(vararg tokenTypes: TokenType): Boolean {
        for (type in tokenTypes) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }
    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return peek().type == type
    }
    private fun advance(): Token {
        if (!isAtEnd()) {
            current++
        }
        return previous()
    }
    private fun isAtEnd(): Boolean {
        return peek().type == TokenType.EOF
    }
    private fun previous(): Token {
        return tokens[current - 1]
    }
    private fun peek(): Token {
        return this.tokens[current]
    }
    private fun consume(type: TokenType, errorMessage: String): Token {
        if (check(type))return advance()

        throw error(peek(), errorMessage)
    }
    private fun error(token: Token, message: String): ParseError {
        KLox.error(token, message)
        return ParseError()
    }
}
