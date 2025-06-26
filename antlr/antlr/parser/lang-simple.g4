grammar lang;

@parser::header
{
    package parser;
}

@lexer::header
{
    package parser;
}

/* Regras da gramática */

prog: stmt+;

stmt :
 ID '=' expr ';'
|
 expr ';'
;

expr:
 factor ('+' | '*') expr
|
 factor
;

factor:
 ID
|
 INT
;

/* Regras léxicas */

ID : [a-z];
INT : [0-9]+;

NEWLINE: '\r'? '\n' -> skip;
WS : [ \t]+ -> skip;
LINE_COMMENT : '//' ~('\r' | '\n')* NEWLINE -> skip;
COMMENT: '/*' .*?  '*/' -> skip;

