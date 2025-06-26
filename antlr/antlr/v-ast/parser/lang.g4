grammar lang;

@parser::header
{
    package parser;

    import ast.*;
}

@lexer::header
{
    package parser;
}

/* Regras da gramática */

prog returns [StmtList ast]:
  s1=stmt ';' {$ast = new StmtList($s1.ast.getLine(), $s1.ast.getCol(), $s1.ast);}
  (s2=stmt ';' {$ast = new StmtList($s2.ast.getLine(), $s2.ast.getCol(), $ast, $s2.ast);})*
;

stmt returns [Node ast]:
 ID '=' expr {$ast = new Attr($ID.line, $ID.pos, new ID($ID.line, $ID.pos, $ID.text), $expr.ast);}
|
 expr op='?' '[' s1=stmt ']' ':' '[' s2=stmt ']' {$ast = new If($op.line, $op.pos, $expr.ast, $s1.ast, $s2.ast);}
|
 expr op='?' '[' s1=stmt ']' {$ast = new If($op.line, $op.pos, $expr.ast, $s1.ast);}
|
 expr {$ast = new Print($expr.ast.getLine(), $expr.ast.getCol(), $expr.ast);}
;

expr returns [Expr ast]:
 term op='+' e=expr {$ast = new Add($op.line, $op.pos, $term.ast, $e.ast);}
|
 term {$ast = $term.ast;}
;

term returns [Expr ast]:
 factor op='*' e=term {$ast = new Mul($op.line, $op.pos, $factor.ast, $e.ast);}
|
 factor {$ast = $factor.ast;}
;

factor returns [Expr ast]:
 ID {$ast = new ID($ID.line, $ID.pos, $ID.text);}
|
 INT {$ast = new Num($INT.line, $INT.pos, Integer.parseInt($INT.text));}
;

/* Regras léxicas */

ID : [a-z]+;
INT : [0-9]+;

NEWLINE: '\r'? '\n' -> skip;
WS : [ \t]+ -> skip;
LINE_COMMENT : '//' ~('\r' | '\n')* NEWLINE -> skip;
COMMENT: '/*' .*?  '*/' -> skip;

