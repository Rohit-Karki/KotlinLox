package xyz.rohit.lox

class AstPrinter : Expr.Visitor<String> {
    private fun parenthesize(name: String?, vararg exprs: Expr): String {
        val builder = StringBuilder().append("(").append(name)
        exprs.forEach {
            builder.append(" ").append(it.accept(this))
        }
        return builder.append(")").toString()
    }
    fun print(expr: Expr): String {
        return expr.accept(this)
    }
    override fun visit(expr: Expr.Binary): String {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right)
    }

    override fun visit(expr: Expr.Grouping): String {
        return parenthesize("group", expr.expression)
    }

    override fun visit(expr: Expr.Literal): String = expr.value.toString()

    override fun visit(expr: Expr.Unary): String = parenthesize(expr.operator.lexeme, expr.right)
}
