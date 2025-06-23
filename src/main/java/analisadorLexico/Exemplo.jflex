package analisadorLexico;
import analisadorLexico.Token;

 /* Esta seção é copiada antes da declaração da classe do analisador léxico.
  * É nesta seção que se deve incluir imports e declaração de pacotes.
  * Neste exemplo não temos nada a incluir nesta seção.
  */

%%

%unicode
%line
%column
%class LextLang
%function nextToken
%type Token

%{

    private char processEscapeSequence(String content) {
        if (content.startsWith("\\")) {
            if (content.matches("\\\\[0-9]{3}")) { // Escape numérico (exemplo: '\065')
                return (char) Integer.parseInt(content.substring(1), 8); // Interpreta como octal
            }
            switch (content.charAt(1)) {
                case 'n': return '\n';
                case 't': return '\t';
                case 'r': return '\r';
                case 'b': return '\b';
                case '\'': return '\'';
                case '\\': return '\\';
                default:
                    // Isso deve ser uma exceção de runtime ou erro léxico
                    throw new RuntimeException("Escape inválido em literal char: \\" + content.charAt(1));
            }
        } else {
            // Caractere simples (exemplo: 'a', 'x', etc)
            return content.charAt(0);
        }
    }


    private int ntk;

    public int readedTokens(){
       return ntk;
    }
    private Token symbol(TOKEN_TYPE t) {
        ntk++;
        return new Token(t,yytext(), yyline+1, yycolumn+1);

    }
    private Token symbol(TOKEN_TYPE t, Object value) {
        ntk++;
        return new Token(t, value, yyline+1, yycolumn+1);
    }
%}

%init{
    ntk = 0; // Isto é copiado direto no construtor do lexer.
%init}


  /* Agora vamos definir algumas macros */
  ID     = [a-z][a-zA-Z0-9_]*
  TYID   = [A-Z][a-zA-Z0-9_]*
  CHAR   = \'([^\'\n\r\\]|\\[nrtb\\\']|\\[0-9]{3})\'
  DIGIT  = [0-9]
  INT    = {DIGIT}+
  FLOAT  = {DIGIT}*"."{DIGIT}+
  FimDeLinha  = \r|\n|\r\n
  Brancos     = {FimDeLinha} | [ \t\f]

%state COMMENT

%%

<YYINITIAL>{
    // Comentários (sempre processados primeiro para ignorá-los)
    "--"[^\r\n]* { /* Ignorar comentário de linha */ }
    "{-"         { yybegin(COMMENT); }

    // Espaços (sempre processados primeiro para ignorá-los)
    {Brancos}    { /* Ignorar */ }

    [cite_start]// Palavras reservadas (devem vir antes de ID e TYID) [cite: 142]
    "Int"        { return symbol(TOKEN_TYPE.INT_TYPE); }
    "Char"       { return symbol(TOKEN_TYPE.CHAR_TYPE); }
    "Bool"       { return symbol(TOKEN_TYPE.BOOL_TYPE); }
    "Float"      { return symbol(TOKEN_TYPE.FLOAT_TYPE); }
    "if"         { return symbol(TOKEN_TYPE.IF); }
    "else"       { return symbol(TOKEN_TYPE.ELSE); }
    "iterate"    { return symbol(TOKEN_TYPE.ITERATE); }
    "return"     { return symbol(TOKEN_TYPE.RETURN); }
    "read"       { return symbol(TOKEN_TYPE.READ); }
    "print"      { return symbol(TOKEN_TYPE.PRINT); }
    "abstract"   { return symbol(TOKEN_TYPE.ABSTRACT); }
    "data"       { return symbol(TOKEN_TYPE.DATA); }
    "true"       { return symbol(TOKEN_TYPE.TRUE); }
    "false"      { return symbol(TOKEN_TYPE.FALSE); }
    "null"       { return symbol(TOKEN_TYPE.NULL); }

    // Operadores
    "!"          { return symbol(TOKEN_TYPE.NOT); }
    "=="         { return symbol(TOKEN_TYPE.EQEQ); }
    "!="         { return symbol(TOKEN_TYPE.NEQ); }
    "<"          { return symbol(TOKEN_TYPE.LT); }
    "&&"         { return symbol(TOKEN_TYPE.AND); }
    "+"          { return symbol(TOKEN_TYPE.PLUS); }
    "-"          { return symbol(TOKEN_TYPE.MINUS); }
    "*"          { return symbol(TOKEN_TYPE.MUL); }
    "/"          { return symbol(TOKEN_TYPE.DIV); }
    "%"          { return symbol(TOKEN_TYPE.MOD); }

    // Símbolos (operadores de múltiplos caracteres devem vir antes dos de um caractere se houver sobreposição)
    "::"         { return symbol(TOKEN_TYPE.COLONCOLON); }
    "."          { return symbol(TOKEN_TYPE.DOT); } // Coloque antes de ':' se '.' puder ser parte de um token maior que começasse com '.'
    ","          { return symbol(TOKEN_TYPE.COMMA); }
    ";"          { return symbol(TOKEN_TYPE.SEMI); }
    "="          { return symbol(TOKEN_TYPE.ASSIGN); }
    "{"          { return symbol(TOKEN_TYPE.LBRACE); }
    "}"          { return symbol(TOKEN_TYPE.RBRACE); }
    "["          { return symbol(TOKEN_TYPE.LBRACKET); }
    "]"          { return symbol(TOKEN_TYPE.RBRACKET); }
    "("          { return symbol(TOKEN_TYPE.LPAREN); }
    ")"          { return symbol(TOKEN_TYPE.RPAREN); }
    ":"          { return symbol(TOKEN_TYPE.COLON); } // Depois de "::"

    // Identificadores e Nomes de Tipos (depois das palavras reservadas)
    {TYID}       { return symbol(TOKEN_TYPE.TYID, yytext()); } // TYID antes de ID se houver chance de TYID ser um ID válido.
    {ID}         { return symbol(TOKEN_TYPE.ID, yytext()); }


    // Literais (depois de palavras reservadas que poderiam ser literais, como true/false/null)
    {CHAR} {
        String text = yytext();
        String content = text.substring(1, text.length() - 1);
        char c = processEscapeSequence(content);
        return symbol(TOKEN_TYPE.CHAR, c);
    }
    {INT}        { return symbol(TOKEN_TYPE.INT, Integer.parseInt(yytext())); }
    {FLOAT}      { return symbol(TOKEN_TYPE.FLOAT, Float.parseFloat(yytext())); }

}

<COMMENT>{
    "-}"        { yybegin(YYINITIAL); }
    .|\n        { /* Ignorar qualquer caractere ou newline dentro do comentário de bloco */ }
    <<EOF>>     { throw new RuntimeException("Comentário de bloco não fechado ao atingir EOF"); }
}

[cite_start]// Regra para caracteres ilegais [cite: 1]
[^]                 { throw new RuntimeException("Illegal character <"+yytext()+">"); }