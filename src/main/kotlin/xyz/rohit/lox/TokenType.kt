package xyz.rohit.lox

enum class TokenType {
    /* Single-Character Token */
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    /*One or two character tokens*/
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    QUESTION, BANG_QUESTION,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    /*Literals*/
    IDENTIFIER, STRING, NUMBER,

    /*Keywords*/
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF
}
