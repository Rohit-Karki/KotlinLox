package xyz.rohit.lox

fun main() {
    println("Rohit")
}
class Parser(private val tokens: List<Token>) {
    private class ParseError() : RuntimeException()
    private var current = 0

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

    fun parse(): MutableList<Stmt> {
        var statements = mutableListOf<Stmt>()
        while (!isAtEnd()) {
            val stmt = declaration()
            if(stmt != null) {
                    statements.add(stmt)
            }
        }
        return statements
    }

    /*
    * program           → declaration* EOF ;

      declaration       → varDecl
                        | statement ;

       statement        → exprStmt
                        | printStmt ;
    */
    private fun declaration() = try {
            if(match(TokenType.VAR)) varDeclaration()
            else statement()
        } catch (error: ParseError){
            synchronize()
            null
        }
    }

    private fun varDeclaration(): Stmt {
        val name = consume(TokenType.IDENTIFIER, "Expect a variable name.")
        var initializer: Expr? = null
        if (match(TokenType.EQUAL)) {
            initializer = expression()
        }
        consume(TokenType.SEMICOLON, "Expect SemiColon")
        return Stmt.Var(name, initializer)
    }

    private fun statement(): Stmt {
        if (match(TokenType.PRINT)) {
            return printStatement()
        }
        return expressionStatement()
    }

    private fun printStatement(): Stmt {
        val value: Expr = expression()
        consume(TokenType.SEMICOLON, "Except ; after value")
        return Stmt.Print(value)
    }

    private fun expressionStatement(): Stmt {
        val value: Expr = expression()
        consume(TokenType.SEMICOLON, "Except ; after value")
        return Stmt.Expression(value)
    }

    private fun expression(): Expr {
        return equality()
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

    private fun comparision(): Expr {
        var expr: Expr = term()
        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            val operator = previous()
            val right = term()
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
        /*
        * primary        → "true" | "false" | "nil"
                         | NUMBER | STRING
                         | "(" expression ")"
                         | IDENTIFIER ;
        * That IDENTIFIER clause matches a single
        * identifier token, which is understood to be the
        * name of the variable being accessed.
        */
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
        if (match(TokenType.IDENTIFIER)) {
           return Expr.Variable(previous())

        }
        throw error(peek(), "Expect expression")
    }


    private fun match(vararg tokenTypes: TokenType): Boolean {
        for (type in tokenTypes) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType) = if (isAtEnd()) false else peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd())
            current++

        return previous()
    }

    private fun isAtEnd() = peek().type == TokenType.EOF

    private fun previous() = tokens[current - 1]

    private fun peek() = tokens[current]

    /*
    * Consume takes a tokenType, and it checks for the token currently in the program,
    * and it advances
    * But if it doesn't match then the error message is shown.
    * */
    private fun consume(type: TokenType, errorMessage: String): Token {
        if (check(type))return advance()

        throw error(peek(), errorMessage)
    }

    private fun error(token: Token, message: String): ParseError {
        KLox.error(token, message)
        return ParseError()
    }
}
