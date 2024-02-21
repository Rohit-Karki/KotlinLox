package xyz.rohit.lox

class RuntimeError(token: Token, message: String) : RuntimeException(message)
