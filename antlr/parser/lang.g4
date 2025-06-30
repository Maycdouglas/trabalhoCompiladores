grammar lang;

@parser::header {
    package parser;
}

@lexer::header {
    package parser;
}

/* Regras da gramática */
/* iniciadas com letras minusculas */

prog: def*
;

def: data | fun
;

/* O ANTLR PODE RECLAMAR DE AMBIGUIDADE AQUI

Abaixo deixei como tinha feito anteriormente, entretanto, foi necessário mudar, devido a AMBIGUIDADE causada pelo DATA TYID.
A recomendação é separar em duas novas regras, pensando no interpretador e compilador no futuro.

data: 
 ABSTRACT DATA TYID '{' (decl | fun)* '}'
|
 DATA TYID '{' decl* '}'
;
*/

data: dataAbstract | dataRegular
;

dataAbstract: ABSTRACT DATA TYID '{' (decl | fun)* '}'
;

dataRegular: DATA TYID '{' decl* '}'
;

decl: ID '::' type ';'
;


/* Inicialmente fiz como está abaixo, porém, pensando na legibilidade e manutenção futura,
foi recomendado que os tipos do retorno se tornassem uma regra separada.
fun: ID '(' params? ')' (':' type (',' type)* )? cmd
;
*/

fun: ID '(' params? ')' retTypes? cmd
;

retTypes: ':' type (',' type)*
;


/* Inicialmente fiz como está abaixo, porém, é recomendado separar se for usar AST com listas.
Facilitará percorrer a lista de parâmetros no Visitor.
params: ID '::' type (',' ID '::' type)*
;
*/

params: param (',' param)*
;

param: ID '::' type
;

/* aqui foi necessário remover a recursão à esquerda */
type: btype ('[' ']')*
;

btype: INT_TYPE | CHAR_TYPE | BOOL_TYPE | FLOAT_TYPE | TYID
;

block: '{' cmd* '}'
;

cmd: 
 block 
|
 IF '(' exp ')' cmd
|
 IF '(' exp ')' cmd ELSE cmd
|
 ITERATE '(' itcond ')' cmd
|
 READ lvalue ';'
|
 PRINT exp ';'
|
 RETURN exp (',' exp)* ';'
|
 lvalue '=' exp ';'
|
 ID '(' exps? ')' ('<' lvalue (',' lvalue)* '>')? ';'
;

/* Fiz da forma que está abaixo, mas recomendou o uso de labels para facilitar o Visit no futuro.
itcond: ID ':' exp | exp
;
*/

itcond: 
 ID ':' exp          #CondLabelled 
| 
 exp                 #CondExpr
;

/*TUDO ABAIXO TEVE QUE MUDAR, DEVIDO RECURSÃO À ESQUERDA
exp:
 exp op exp
|
 '!' exp
|
 '-' exp
|
 lvalue
|
 '(' exp ')'
|
 NEW type ('[' exp ']')?
|
 ID '(' exps? ')' '[' exp ']'
|
 TRUE
|
 FALSE
|
 NULL
|
 INT
|
 FLOAT
|
 CHAR
;

op: '&&' | '<' | '==' | '!=' | '+' | '-' | '*' | '/' | '%'
;

lvalue:
 ID
|
 lvalue '[' exp ']'
|
 lvalue '.' ID
;

exps: exp (',' exp)
*/

exp: expAnd                  #ExpAnd
;

expAnd:
 expAnd '&&' expEq           #AndExpr
| 
 expEq                       #ToEq
;

// PAREI AQUI PQ O CHAT GPT FICOU BURRO.

expEq:
 expEq ('==' | '!=') expRel  #EqExpr
|
 expRel                      #ToRel
;


/* Regras léxicas */
/* iniciadas com letras maiusculas */

ABSTRACT: 'abstract';
DATA: 'data';
TYID: [A-Z][a-zA-Z0-9_]*;
ID: [a-z][a-zA-Z0-9_]*;
INT_TYPE: 'Int';
CHAR_TYPE: 'Char';
BOOL_TYPE: 'Bool';
FLOAT_TYPE: 'Float';
IF: 'if';
ELSE: 'else';
ITERATE: 'iterate';
READ: 'read';
PRINT: 'print';
RETURN: 'return';
NEW: 'new';
TRUE: 'true';
FALSE: 'false';
NULL: 'null';
INT: [0-9]+;
FLOAT: [0-9]* '.' [0-9]+;
CHAR: 
	'\'' 
    	( ~['\\\n\r]            // caractere normal, exceto aspas simples, barra invertida, quebra linha, carriage return
    	| '\\' [nrtb'\\]        // escapes válidos: \n, \r, \t, \b, \', \\
    	| '\\' [0-9] [0-9] [0-9]// código ASCII: exatamente 3 dígitos
    	) 
  	'\''
;


/* TOKENS QUE EU NÃO ESTAVA USANDO MAS VÃO SER UTEIS NO FUTURO ENTÃO PRECISO SUBSTITUIR OS VALORES LITERAIS ACIMA E VERIFICAR SE PODEM FICAR TODOS NESSA ORDEM */
PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';

fragment ANGLE: '<'; // Fragmento base que não gera token sozinho

EQ: '==';
NEQ: '!=';
LT: ANGLE;
AND: '&&';
NOT: '!';

ASSIGN: '=';
DCOLON: '::';
COLON: ':';
SEMI: ';';
COMMA: ',';
DOT: '.';
PIPE: '|';
ARROW: '->';

LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
RANGLE: '>';
LANGLE: ANGLE;

LINE_COMMENT: '--' ~[\r\n]* -> skip;
BLOCK_COMMENT: '{-' .*? '-}' -> skip;
WS: [ \t\r\n]+ -> skip;