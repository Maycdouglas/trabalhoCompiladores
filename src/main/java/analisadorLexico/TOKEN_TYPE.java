package analisadorLexico;

public enum TOKEN_TYPE {
    // Identificadores e Nomes de Tipos
    ID, // [a-z][a-zA-Z0-9_]*
    TYID, // [A-Z][a-zA-Z0-9_]*

    // Literais
    CHAR, // \'([^\'\n\r\\]|\\[nrtb\\\']|\\[0-9]{3})\'
    INT, // {DIGIT}+
    FLOAT, // {DIGIT}*"."{DIGIT}+
    TRUE, // true
    FALSE, // false
    NULL, // null

    // Palavras reservadas
    INT_TYPE, // Int
    CHAR_TYPE, // Char
    BOOL_TYPE, // Bool
    FLOAT_TYPE, // Float
    IF, // if
    ELSE, // else
    ITERATE, // iterate
    RETURN, // return
    READ, // read
    PRINT, // print
    ABSTRACT, // abstract
    DATA, // data

    // Operadores
    NOT, // !
    EQEQ, // ==
    NEQ, // !=
    LT, // <
    AND, // &&
    PLUS, // +
    MINUS, // -
    MUL, // *
    DIV, // /
    MOD, // %

    // Símbolos
    COLONCOLON, // ::
    COLON, // :
    DOT, // .
    COMMA, // ,
    SEMI, // ;
    ASSIGN, // =
    LBRACE, // {
    RBRACE, // }
    LBRACKET, // [
    RBRACKET, // ]
    LPAREN, // (
    RPAREN, // )
}