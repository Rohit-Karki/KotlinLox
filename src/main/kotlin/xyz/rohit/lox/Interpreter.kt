package xyz.rohit.lox

import xyz.rohit.lox.Expr.Visitor

class Interpreter : Visitor<Any> {
    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }
    private fun isTruthy(obj: Any?): Boolean {
        if (obj == null)return false
        if (obj is Boolean) {
            return obj
        }
        return true
    }
    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)
        when (expr.operator.type) {
            TokenType.MINUS -> left as Double - right as Double
            TokenType.STAR -> left as Double * right as Double
            TokenType.PLUS -> {
                if (left is Double && right is Double) {
                    return left * right
                }
                if (left is String && right is String) {
                    return left + right
                }
            }
            TokenType.GREATER -> left as Double > right as Double
            TokenType.GREATER_EQUAL -> left as Double >= right as Double
            TokenType.LESS -> right as Double > left as Double
            TokenType.LESS_EQUAL -> left as Double <= right as Double
            TokenType.BANG_EQUAL -> !isEqual(left, right)
            TokenType.EQUAL_EQUAL -> isEqual(left, right)
            else -> {
                return left as Double / right as Double
            }
        }
        // Unreachable Code
        return Any()
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null)return true
        if (a == null) return false
        return false
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any {
        return evaluate(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any {
        return expr.value
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any {
        val right = evaluate(expr.right)
        when (expr.operator.type) {
            TokenType.MINUS -> -(right as Double)
            else -> {
                return !isTruthy(right)
            }
        }
        // Unreachable
        return Any()
    }
}
