Estando na pasta trabalhoCompiladores/antrl:

comando para o ANTLR:
`java -jar ./antlr-4.8-complete.jar -visitor -o parser parser/lang.g4`

Linux:
java -jar antlr-4.8-complete.jar -visitor -o parser parser/lang.g4

- PRECISA ESTAR NA PASTA ANTLR
- Esse comando gera os arquivos abaixo:
  - langBaseListener.java
  - langBaseVisitor.java
  - langLexer.java
  - langListener.java
  - langParser.java
  - langVisitor.java
  - lang.interp
  - lang.tokens
  - langLexer.interp
  - langLexer.tokens

compilar o arquivo:
`javac -cp ".;antlr-4.8-complete.jar" parser\*.java`

Linux:
`javac -cp ".:antlr-4.8-complete.jar" parser/*.java`

- gera vários arquivos dentro da pasta parser.

### PASSOS PARA TESTAR O PARSER:

<!-- 1. javac -cp ".;antlr-4.8-complete.jar" parser\*.java
2. javac -cp ".;antlr-4.8-complete.jar" Main.java -->

1. `javac -cp ".;antlr-4.8-complete.jar" parser/*.java ast/*.java error/*.java interpreter/*.java Main.java`
2. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/errado/nonAssoc.lan`
3. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/certo/attrADD.lan`
4. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/certo/attrAND.lan`

5. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/certo/attrCHAR.lan`

### PROBLEMATICOS:

1. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/problematicos/attrAND.lan`
2. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/problematicos/data.lan`
3. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/problematicos/function_call_expr.lan`
4. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/problematicos/if_oneCMD.lan`
5. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/problematicos/instanciate.lan`
6. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/problematicos/iterateLocalArrVar.lan`
7. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/problematicos/iterateLocalVar.lan`

### ARQUIVOS SEMANTICOS:

#### SIMPLE

1. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/assoc.lan`
   - CERTO
2. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/equivChar.lan`
   - CERTO
3. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/ifchain.lan`
   - CERTO
4. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/iterVar.lan`
   - CERTO
5. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/iterVarArr.lan`
   - CERTO
6. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/iterVarDec.lan`
   - CERTO
7. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/read.lan`
   - CERTO
8. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/simple/teste0.lan`
   - CERTO

#### FUNCTION

1. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/function/board.lan`
   - Exception in thread "main" java.lang.RuntimeException: Acesso a índice fora dos limites do array: 3
2. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/function/fib.lan`
   - CERTO
3. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/function/numeric.lan`
   - ERRADO (RESOLVIDO)
     - primeira impressão está errada
       - errado
         - 2.0
       - certo
         - 2.4065402
     - segunda impressão está correta
       1.4142135
4. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/function/or_equiv.lan`
   - CERTO
5. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/function/return.lan`
   - CERTO
     - AO MENOS PARA MIM ESTÁ
6. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/function/sort.lan`
   - ERRADO (RESOLVIDO)
     - imprime primeira linha corretamente, mas a segunda linha fica igual
     - {123,345,21,12,0,94,1,6,3,2}
     - {123,345,21,12,0,94,1,6,3,2}
     - vai dar errado no semantico
7. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/function/teste1.lan`
   - CERTO

#### FULL

1. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/full/AFD.lan`
   - Exception in thread "main" java.lang.UnsupportedOperationException: Tipo de atribuição não suportado.
     (RESOLVIDO)
2. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/semantica/certo/full/linked.lan`
   - CERTO

#### TYPES - SIMPLE

1. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/types/simple/scope.lan` (ERRADO)

#### TYPES - FUNCTION

#### TYPES - FULL

#### TYPES - ERRADO

1. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/types/errado/errado1.lan`
2. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/types/errado/errado2.lan`
3. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/types/errado/errado3.lan`
4. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/types/errado/errado4.lan`
5. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/types/errado/errado5.lan`
6. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/types/errado/errado6.lan`
7. `
`

`java -cp ".;antlr-4.8-complete.jar" Main teste.lan images`

Linux:

1. `javac -cp ".:antlr-4.8-complete.jar" parser/*.java ast/*.java error/*.java Main.java`
2. `java -cp ".:antlr-4.8-complete.jar" Main attr_OR_SUGAR.lan`

### COMANDO COM O MAKEFILE

1. `java -cp ".:antlr-4.8-complete.jar" Main -i arquivosTeste/dataAbstract.lan`
2. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/sintaxe/certo/printCMD.lan ./build`
3. `java -cp ".:antlr-4.8-complete.jar" Main -src arquivosTeste/dataAbstract.lan ./build`

### COMANDO PARA EXECUTAR SOURCE-TO-SOURCE

1. `javac build/dataAbstract.java`
2. `java -cp build dataAbstract`

### PASSOS PARA GERAR A ÁRVORE EM IMAGEM:

- Download do GRAPHVIZ : `https://graphviz.org/download/`
- Ao instalar, selecionar opção que adiciona o bin do Graphviz ao PATH do Windows
  - assim permitirá que use o comando dot no terminal
- Crie os métodos toDot() para todas as classes na pasta AST
- No Main.java, adicione trecho que irá gerar o arquivo .dot
- No terminal, execute o comando: `dot -Tpng ast.dot -o ast.png`

- No terminal, agora deve executar o comando: `dot -Tpng dotFiles/data.dot -o dotFiles/png/data.png`

`dot -Tpng dotFiles/numeric.dot -o dotFiles/numeric.png`

### EXECUÇÃO DO SINTAXE_CERTO.PS1 (WINDOWS):

É necessário estar no diretório antlr

- Execute o comando `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass` para permitir a execução de scripts temporariamente nessa sessão do PowerShell
- Execute o comando `.\scripts\sintaxe_certo.ps1` para rodar o script

- Execute o comando `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass` para permitir a execução de scripts temporariamente nessa sessão do PowerShell
- Execute o comando `.\scripts\sintaxe_errado.ps1` para rodar o script

### ARQUIVOS SEMANTICOS no SOURCE-TO-SOURCE:

### COMANDO PARA EXECUTAR SOURCE-TO-SOURCE

Primeiramente, executa MAKE!

1. `javac build/dataAbstract.java`
2. `java -cp build dataAbstract`

#### SIMPLE

1. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/assoc.lan ./build`
   - `javac build/assoc.java`
   - `java -cp build assoc`
   - CERTO
2. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/equivChar.lan ./build`
   - `javac build/equivChar.java`
   - `java -cp build equivChar`
   - CERTO
3. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/ifchain.lan ./build`
   - `javac build/ifchain.java`
   - `java -cp build ifchain`
   - CERTO
4. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/iterVar.lan ./build`
   - `javac build/iterVar.java`
   - `java -cp build iterVar`
   - CERTO
5. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/iterVarArr.lan ./build`
   - `javac build/iterVarArr.java`
   - `java -cp build iterVarArr`
   - CERTO
6. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/iterVarDec.lan ./build`
   - `javac build/iterVarDec.java`
   - `java -cp build iterVarDec`
   - ERRADO
     - Problema com escopo
7. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/read.lan ./build`
   - `javac build/read.java`
   - `java -cp build read`
   - CERTO
8. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/simple/teste0.lan ./build`
   - `javac build/teste0.java`
   - `java -cp build teste0`
   - CERTO

#### FUNCTION

1. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/function/board.lan ./build`
   - `javac build/board.java`
   - `java -cp build board`
   - ERRADO
2. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/function/fib.lan ./build`
   - `javac build/fib.java`
   - `java -cp build fib`
   - CERTO
3. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/function/numeric.lan ./build`
   - `javac build/numeric.java`
   - `java -cp build numeric`
   - CERTO
4. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/function/or_equiv.lan ./build`
   - `javac build/or_equiv.java`
   - `java -cp build or_equiv`
   - CERTO
5. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/function/return.lan ./build`
   - `javac build/return.java`
   - `java -cp build return`
   - SEMANTICO RECUSA
6. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/function/sort.lan ./build`
   - `javac build/sort.java`
   - `java -cp build sort`
   - SEMANTICO RECUSA
7. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/function/teste1.lan ./build`
   - `javac build/teste1.java`
   - `java -cp build teste1`
   - ERRO

#### FULL

1. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/full/AFD.lan ./build`
   - `javac build/AFD.java`
   - `java -cp build AFD`
   - SEMANTICO RECUSA
2. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/semantica/certo/full/linked.lan ./build`
   - `javac build/linked.java`
   - `java -cp build linked`
   - CERTO

#### TYPES - SIMPLE

1. `java -cp ".:antlr-4.8-complete.jar" Main -src ../Lang/types/simple/scope.lan ./build`
   - `javac build/scope.java`
   - `java -cp build scope`
   - ERRADO
