package xyz.rohit.lox

fun main() {
    var expression: Expr = Expr.Binary(
        Expr.Unary(Token(TokenType.MINUS, "-", null, 1), Expr.Literal(45.68)),
        Token(TokenType.STAR, "*", null, 1),
        Expr.Grouping(Expr.Literal("123")),
    )

    println(AstPrinter().print(expression))
}
