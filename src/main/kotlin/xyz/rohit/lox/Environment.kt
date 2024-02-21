package xyz.rohit.lox

class Environment {
    var env = HashMap<String, Any?>()

    operator fun get(name: Token): Any {
        when {
            env.contains(name.lexeme) -> env[name.lexeme]
        }
        throw RuntimeError(name, "Undefined Variable name ${name.lexeme}.")
    }
}
