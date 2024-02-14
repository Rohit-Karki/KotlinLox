package xyz.rohit.lox

import xyz.rohit.lox.Expr.Visitor

class Interpreter : Visitor<Any> {
    private fun evaluate(expr:Expr):Any{
        return expr.accept(this)
    }
    private fun isTruthy(expr:Any):Boolean{
        return true
    }
    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        TODO("Not yet implemented")
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any {
        TODO("Not yet implemented")
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any {
        return expr.value
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any {
        val right = evaluate(expr.right)

        when(expr.operator.type){
            TokenType.MINUS -> -(double)right
            TokenType.BANG -> !isTruthy(right)
        }
    }

}