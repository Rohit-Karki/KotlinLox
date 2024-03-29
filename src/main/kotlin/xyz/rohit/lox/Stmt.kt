package xyz.rohit.lox

abstract class Stmt {
    abstract fun <R> accept(visitor: Visitor<R>): R
    interface Visitor<out R> {
        fun visit(stmt: Expression): R
        fun visit(stmt: Print): R
        fun visit(stmt: Var): R
    }
    class Expression(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visit(this)
        }
    }

    class Print(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visit(this)
        }
    }

    class Var(val token: Token, val initializer: Expr?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visit(this)
        }
    }
}
