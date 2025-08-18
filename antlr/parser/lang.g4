/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES -
 202065570C
 */

grammar lang;

@parser::header {
    package parser;
}

@lexer::header {
    package parser;
}

/* Regras da gramática */
/* iniciadas com letras minusculas */

prog: def*;

def: data | fun;

data: dataAbstract | dataRegular;

dataAbstract: ABSTRACT DATA TYID LBRACE (decl | fun)* RBRACE;

dataRegular: DATA TYID LBRACE decl* RBRACE;

decl: ID DCOLON type SEMI;

fun: ID LPAREN params? RPAREN retTypes? cmd;

retTypes: COLON type (COMMA type)*;

params: param (COMMA param)*;

param: ID DCOLON type;

/* aqui foi necessário remover a recursão à esquerda */
type: btype (LBRACK RBRACK)*;

btype: INT_TYPE | CHAR_TYPE | BOOL_TYPE | FLOAT_TYPE | TYID;

block: LBRACE cmd* RBRACE;

cmd:
	block
	| IF LPAREN exp RPAREN cmd
	| IF LPAREN exp RPAREN cmd ELSE cmd
	| ITERATE LPAREN itcond RPAREN cmd
	| READ lvalue SEMI
	| PRINT exp SEMI
	| RETURN exp (COMMA exp)* SEMI
	| lvalue ASSIGN exp SEMI
	| ID LPAREN exps? RPAREN (
		LANGLE lvalue (COMMA lvalue)* RANGLE
	)? SEMI;

itcond: ID COLON exp # CondLabelled | exp # CondExpr;

exp: expOr # ExpTop;

expOr: expAnd (AND expAnd)* # OrExpr;

expAnd: expRel (LANGLE expRel)* # AndExpr;

expRel: expEq ((EQ | NEQ) expEq)* # RelExpr;

expEq: expAdd ((PLUS | MINUS) expAdd)* # EqExpr;

expAdd: expMul ((MUL | DIV | MOD) expMul)* # AddExpr;

expMul:
	NOT expMul		# NotExpr
	| MINUS expMul	# NegExpr
	| expPrimary	# ToPrimary;

expPrimary:
	lvalue										# LvalExpr
	| LPAREN exp RPAREN							# ParenExpr
	| NEW type (LBRACK exp RBRACK)?				# NewExpr
	| ID LPAREN exps? RPAREN					# CallExpr
	| ID LPAREN exps? RPAREN LBRACK exp RBRACK	# CallIndexedExpr
	| TRUE										# TrueExpr
	| FALSE										# FalseExpr
	| NULL										# NullExpr
	| INT										# IntExpr
	| FLOAT										# FloatExpr
	| CHAR										# CharExpr;

lvalue:
	ID							# IdLval
	| lvalue LBRACK exp RBRACK	# IndexLval
	| lvalue DOT ID				# FieldLval;

exps: exp (COMMA exp)* # ExpsList;

/* Regras léxicas */
/* iniciadas com letras maiusculas */

ABSTRACT: 'abstract';
DATA: 'data';
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
TYID: [A-Z][a-zA-Z0-9_]*;
ID: [a-z][a-zA-Z0-9_]*;
INT: [0-9]+;

FLOAT: [0-9]+ '.' [0-9]+ | '.' [0-9]+;

CHAR:
	'\'' (
		~['\\\n\r]
		// caractere normal, exceto aspas simples, barra invertida, quebra linha, carriage return
		| '\\' [nrtb'\\] // escapes válidos: \n, \r, \t, \b, \', \\
		| '\\' [0-9] [0-9] [0-9] // código ASCII: exatamente 3 dígitos
	) '\'';
PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';
EQ: '==';
NEQ: '!=';
AND: '&&';
NOT: '!';
ASSIGN: '=';
DCOLON: '::';
COLON: ':';
SEMI: ';';
COMMA: ',';
DOT: '.';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
RANGLE: '>';
LANGLE: '<';

LINE_COMMENT: '--' ~[\r\n]* -> skip;
BLOCK_COMMENT: '{-' .*? '-}' -> skip;
WS: [ \t\r\n]+ -> skip;