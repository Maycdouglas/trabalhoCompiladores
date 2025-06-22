package analisadorLexico;
import compiladorWithJflex.Token;
 /*  Esta seção é copiada antes da declaração da classe do analisador léxico.
  *  É nesta seção que se deve incluir imports e declaração de pacotes.
  *  Neste exemplo não temos nada a incluir nesta seção.
  */

%%

%unicode
%line
%column
%class LextLang
%function nextToken
%type Token

%{

    /* Código arbitrário pode ser inserido diretamente no analisador dessa forma.
     * Aqui podemos declarar variáveis e métodos adicionais que julgarmos necessários.
     */

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
  DIGIT      = [0-9]
  INT        = {DIGIT}+
  FLOAT      = {DIGIT}*"."{DIGIT}+
  FimDeLinha  = \r|\n|\r\n
  Brancos     = {FimDeLinha} | [ \t\f]

%state COMMENT

%%

<YYINITIAL>{
    {ID}      { return symbol(TOKEN_TYPE.ID, yytext()); }
    {TYID}    { return symbol(TOKEN_TYPE.TYID, yytext()); }
    {INT}    { return symbol(TOKEN_TYPE.INT, Integer.parseInt(yytext())); }
    {FLOAT}  { return symbol(TOKEN_TYPE.FLOAT, Float.parseFloat(yytext())); }
    {Brancos} { /* Ignora espaços em branco */ }
    "if"        { return symbol(TOKEN_TYPE.IF); }
    "else"      { return symbol(TOKEN_TYPE.ELSE); }
    "iterate"   { return symbol(TOKEN_TYPE.ITERATE); }
    "return"    { return symbol(TOKEN_TYPE.RETURN); }
    "read"      { return symbol(TOKEN_TYPE.READ); }
    "print"     { return symbol(TOKEN_TYPE.PRINT); }
    "abstract"  { return symbol(TOKEN_TYPE.ABSTRACT); }
    "data"      { return symbol(TOKEN_TYPE.DATA); }
    "true"      { return symbol(TOKEN_TYPE.TRUE); }
    "false"     { return symbol(TOKEN_TYPE.FALSE); }
    "null"      { return symbol(TOKEN_TYPE.NULL); }
    "="          { return symbol(TOKEN_TYPE.ASSIGN); }
    ";"          { return symbol(TOKEN_TYPE.SEMI); }
    "+"         { return symbol(TOKEN_TYPE.PLUS); }
    "-"         { return symbol(TOKEN_TYPE.MINUS); }
    "*"         { return symbol(TOKEN_TYPE.MUL); }
    "/"         {return symbol(TOKEN_TYPE.DIV); }
    "%"         { return symbol(TOKEN_TYPE.MOD); }
    "=="      { return symbol(TOKEN_TYPE.EQEQ); }
    "!="      { return symbol(TOKEN_TYPE.NEQ); }
    "<"       { return symbol(TOKEN_TYPE.LT); }
    "&&"      { return symbol(TOKEN_TYPE.AND); }
    ":"       { return symbol(TOKEN_TYPE.COLON); }
    "::"      { return symbol(TOKEN_TYPE.COLONCOLON); }
    ","       { return symbol(TOKEN_TYPE.COMMA); }
    "{"       { return symbol(TOKEN_TYPE.LBRACE); }
    "}"       { return symbol(TOKEN_TYPE.RBRACE); }
    "["       { return symbol(TOKEN_TYPE.LBRACKET); }
    "]"       { return symbol(TOKEN_TYPE.RBRACKET); }
    "("       { return symbol(TOKEN_TYPE.LPAREN); }
    ")"       { return symbol(TOKEN_TYPE.RPAREN); }
    "--".*    { /* Ignorar comentário de linha */ }
    "{-"        { yybegin(COMMENT); }
}

<COMMENT>{

    // Fim do comentário de bloco: "-}"
    "-}"                    { yybegin(YYINITIAL); }

    // Qualquer outro caractere dentro do comentário de bloco
    .|\n                    { /* Ignorar conteúdo do comentário de bloco */ }
}



[^]                 { throw new RuntimeException("Illegal character <"+yytext()+">"); }



