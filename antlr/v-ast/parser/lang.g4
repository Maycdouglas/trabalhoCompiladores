grammar lang;

@parser::header {
    package parser;
    import ast.*;
}

@lexer::header {
    package parser;
}

/* Regras da gramática */
prog returns [StmtList ast]:
    s1=stmt SEMI               { $ast = new StmtList($s1.ast.getLine(), $s1.ast.getCol(), $s1.ast); }
    (s2=stmt SEMI              { $ast = new StmtList($s2.ast.getLine(), $s2.ast.getCol(), $ast, $s2.ast); })*
;

stmt returns [Node ast]:
    ID ASSIGN expr             { $ast = new Attr($ID.line, $ID.pos, new ID($ID.line, $ID.pos, $ID.text), $expr.ast); }
  | expr                      { $ast = new Print($expr.ast.getLine(), $expr.ast.getCol(), $expr.ast); }
;

expr returns [Expr ast]:
    term op=PLUS expr          { $ast = new Add($op.line, $op.pos, $term.ast, $expr.ast); }
  | term                      { $ast = $term.ast; }
;

term returns [Expr ast]:
    factor op=MUL term         { $ast = new Mul($op.line, $op.pos, $factor.ast, $term.ast); }
  | factor                    { $ast = $factor.ast; }
;

factor returns [Expr ast]:
    ID                         { $ast = new ID($ID.line, $ID.pos, $ID.text); }
  | INT                        { $ast = new Num($INT.line, $INT.pos, Integer.parseInt($INT.text)); }
;

/* Regras léxicas totalmente corrigidas */
ID     : [a-z][a-zA-Z0-9_]*;
TYID   : [A-Z][a-zA-Z0-9_]*;

INT    : [0-9]+;
FLOAT  : [0-9]* '.' [0-9]+;

// Definição de CHAR completamente corrigida
CHAR   : '\'' 
       ( ~['\\\n\r]         // qualquer caractere comum
       | '\\' [nrtb'\\]     // escapes normais
       | '\\' [0-9] [0-9]? [0-9]?  // até 3 dígitos numéricos
       ) 
       '\'';

TRUE   : 'true';
FALSE  : 'false';
NULL   : 'null';

ABSTRACT : 'abstract';
DATA     : 'data';
RETURN   : 'return';
READ     : 'read';
PRINT    : 'print';
IF       : 'if';
ELSE     : 'else';
ITERATE  : 'iterate';

PLUS     : '+';
MINUS    : '-';
MUL      : '*';
DIV      : '/';
MOD      : '%';

EQ       : '==';
NEQ      : '!=';
LT       : '<';      // Operador de comparação
GT       : '>';      // Mantido como único símbolo >
AND      : '&&';
NOT      : '!';

ASSIGN   : '=';
DCOLON   : '::';
COLON    : ':';
SEMI     : ';';
COMMA    : ',';
DOT      : '.';

LPAREN   : '(';
RPAREN   : ')';
LBRACE   : '{';
RBRACE   : '}';
LBRACK   : '[';
RBRACK   : ']';

LINE_COMMENT  : '--' ~[\r\n]* -> skip;
BLOCK_COMMENT : '{-' .*? '-}' -> skip;
WS            : [ \t\r\n]+ -> skip;