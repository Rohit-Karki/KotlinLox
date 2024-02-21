package xyz.rohit.lox

abstract class Expr {
    abstract fun <R> accept(visitor: Visitor<R>): R
    interface Visitor<out R> {
        fun visit(expr: Binary): R
        fun visit(expr: Grouping): R
        fun visit(expr: Literal): R
        fun visit(expr: Unary): R
    }
    class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }

    class Grouping(val expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }

    class Literal(val value: Any?) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }

    class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) = visitor.visit(this)
    }
}
