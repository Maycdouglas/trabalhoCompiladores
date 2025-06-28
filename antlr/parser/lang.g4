grammar lang;

@parser::header {
    package parser;
    import ast.*;
}

@lexer::header {
    package parser;
}

/* Regras da gramática */

prog
	returns[StmtList ast]:
	s1 = stmt SEMI { $ast = new StmtList($s1.ast.getLine(), $s1.ast.getCol(), $s1.ast); } (
		s2 = stmt SEMI { $ast = new StmtList($s2.ast.getLine(), $s2.ast.getCol(), $ast, $s2.ast); }
	)*;

stmt
	returns[Node ast]:
	ID ASSIGN expr { $ast = new Attr($ID.line, $ID.pos, new ID($ID.line, $ID.pos, $ID.text), $expr.ast); 
		}
	| RETURN expr { $ast = new Return($RETURN.line, $RETURN.pos, $expr.ast); }
	| READ { $ast = new Read($READ.line, $READ.pos); }
	| IF expr s1 = stmt ELSE s2 = stmt { $ast = new If($IF.line, $IF.pos, $expr.ast, $s1.ast, $s2.ast); 
		}
	| IF expr stmt { $ast = new If($IF.line, $IF.pos, $expr.ast, $stmt.ast); }
	| ITERATE expr stmt { $ast = new Loop($ITERATE.line, $ITERATE.pos, $expr.ast, $stmt.ast); }
	| expr { $ast = new Print($expr.ast.getLine(), $expr.ast.getCol(), $expr.ast); };

expr
	returns[Expr ast]:
	e1 = expr op = AND e2 = rel_expr { $ast = new And($op.line, $op.pos, $e1.ast, $e2.ast); }
	| rel_expr { $ast = $rel_expr.ast; };

rel_expr
	returns[Expr ast]:
	e1 = rel_expr op = EQ e2 = add_expr { $ast = new Eq($op.line, $op.pos, $e1.ast, $e2.ast); }
	| e1 = rel_expr op = NEQ e2 = add_expr { $ast = new Neq($op.line, $op.pos, $e1.ast, $e2.ast); }
	| e1 = rel_expr op = LT e2 = add_expr { $ast = new Lt($op.line, $op.pos, $e1.ast, $e2.ast); }
	| e1 = rel_expr op = GT e2 = add_expr { $ast = new Gt($op.line, $op.pos, $e1.ast, $e2.ast); }
	| add_expr { $ast = $add_expr.ast; };

add_expr
	returns[Expr ast]:
	e1 = add_expr op = PLUS e2 = mul_expr { $ast = new Add($op.line, $op.pos, $e1.ast, $e2.ast); }
	| e1 = add_expr op = MINUS e2 = mul_expr { $ast = new Sub($op.line, $op.pos, $e1.ast, $e2.ast); 
		}
	| mul_expr { $ast = $mul_expr.ast; };

mul_expr
	returns[Expr ast]:
	e1 = mul_expr op = MUL e2 = unary_expr { $ast = new Mul($op.line, $op.pos, $e1.ast, $e2.ast); }
	| e1 = mul_expr op = DIV e2 = unary_expr { $ast = new Div($op.line, $op.pos, $e1.ast, $e2.ast); 
		}
	| e1 = mul_expr op = MOD e2 = unary_expr { $ast = new Mod($op.line, $op.pos, $e1.ast, $e2.ast); 
		}
	| unary_expr { $ast = $unary_expr.ast; };

unary_expr
	returns[Expr ast]:
	NOT e = unary_expr { $ast = new Not($NOT.line, $NOT.pos, $e.ast); }
	| atom { $ast = $atom.ast; };

atom
	returns[Expr ast]:
	ID { $ast = new ID($ID.line, $ID.pos, $ID.text); }
	| INT { $ast = new Num($INT.line, $INT.pos, Integer.parseInt($INT.text)); }
	| FLOAT { $ast = new Real($FLOAT.line, $FLOAT.pos, Double.parseDouble($FLOAT.text)); }
	| CHAR { $ast = new CharExpr($CHAR.line, $CHAR.pos, $CHAR.text); }
	| TRUE { $ast = new BoolExpr($TRUE.line, $TRUE.pos, true); }
	| FALSE { $ast = new BoolExpr($FALSE.line, $FALSE.pos, false); }
	| LPAREN expr RPAREN { $ast = $expr.ast; };

/* Regras léxicas */

ID: [a-z][a-zA-Z0-9_]*;
TYID: [A-Z][a-zA-Z0-9_]*;

INT: [0-9]+;
FLOAT: [0-9]* '.' [0-9]+;

CHAR:
	'\'' (
		~['\\\n\r] // qualquer caractere comum
		| '\\' [nrtb'\\] // escapes normais
		| '\\' [0-9] [0-9]? [0-9]? // até 3 dígitos numéricos
	) '\'';

TRUE: 'true';
FALSE: 'false';
NULL: 'null';

ABSTRACT: 'abstract';
DATA: 'data';
RETURN: 'return';
READ: 'read';
PRINT: 'print';
IF: 'if';
ELSE: 'else';
ITERATE: 'iterate';

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';

EQ: '==';
NEQ: '!=';
LT: '<';
GT: '>';
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

LINE_COMMENT: '--' ~[\r\n]* -> skip;
BLOCK_COMMENT: '{-' .*? '-}' -> skip;
WS: [ \t\r\n]+ -> skip;