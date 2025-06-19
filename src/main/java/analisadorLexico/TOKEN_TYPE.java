package analisadorLexico;

public enum TOKEN_TYPE {
    ID, // [a-z]([a-zA-Z0-9])*
    TYID, // [A-Z]([a-zA-Z0-9])*
    IF, // if
    ELSE, // else
    ITERATE, // iterate
    RETURN, // return
    READ, // read
    PRINT, // print
    ABSTRACT, // abstract
    DATA, // data
    TRUE, // true
    FALSE, // false
    NULL, // null
    EQEQ, // ==
    NEQ, // !=
    LT, // <
    AND, // &&
    COLON, // ::
    COLONCOLON, // ::
    COMMA, // ,
    LBRACE, // {
    RBRACE, // }
    LBRACKET, // [
    RBRACKET, // ]
    LPAREN, // (
    RPAREN, // )
}
