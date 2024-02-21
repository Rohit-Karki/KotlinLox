package xyz.rohit.lox

class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Unit> {

    private val globals = Environment()
    fun interpret(statements: List<Stmt>) {
        for (statement in statements) {
            execute(statement)
        }
    }
    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }
    private fun execute(stmt: Stmt) {
        stmt.accept(this)
    }
    private fun isTruthy(obj: Any?): Boolean {
        if (obj == null)return false
        if (obj is Boolean) {
            return obj
        }
        return true
    }
    override fun visit(expr: Expr.Binary): Any {
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
                if (left is String && right is Number) {
                    return left + right.toString()
                }
                if (left is Number && right is String) {
                    return left.toString() + right
                }
            }
            TokenType.SLASH -> {
                checkForNumberOperands(expr.operator, left, right)
                checkForDivisionByZero(expr.operator, right as Double)
                return left as Double / right as Double
            }
            TokenType.GREATER -> {
                checkForNumberOperands(expr.operator, left, right)
                return left as Double > right as Double
            }
            TokenType.GREATER_EQUAL -> {
                checkForNumberOperands(expr.operator, left, right)
                return left as Double >= right as Double
            }
            TokenType.LESS -> {
                checkForNumberOperands(expr.operator, left, right)
                return right as Double > left as Double
            }
            TokenType.LESS_EQUAL -> {
                checkForNumberOperands(expr.operator, left, right)
                return left as Double <= right as Double
            }
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

    override fun visit(expr: Expr.Grouping) = evaluate(expr.expression)

    override fun visit(expr: Expr.Literal) = expr.value!!

    override fun visit(expr: Expr.Unary): Any {
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

    override fun visit(stmt: Stmt.Expression) {
        evaluate(stmt.expression)
    }

    override fun visit(stmt: Stmt.Print) {
        val value = evaluate(stmt.expression)
        println(value.toString())
    }

    /*
    * Statement Variable to declare a variable.
    */
    override fun visit(stmt: Stmt.Var) {
        var value: Any? = null
        if (stmt.initializer != null)
            value = evaluate(stmt.initializer)

        globals.env[stmt.token.lexeme] = value
    }

    /*
    * Expression Variable to access a variable.
    */
    override fun visit(expr: Expr.Variable): Any {
        return globals.env[expr.name] ?: Any()
    }

    private fun checkForNumberOperands(operator: Token, left: Any, right: Any) {
        if (left !is Number && right !is Number) {
            throw RuntimeError(operator, "Operands must be numbers")
        }
    }
    private fun checkForDivisionByZero(token: Token, denominator: Number) {
        if (denominator == 0) {
            throw RuntimeError(token, "Division by 0 is not possible")
        }
    }
}
