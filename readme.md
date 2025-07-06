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

1. `javac -cp ".;antlr-4.8-complete.jar" parser/*.java ast/*.java error/*.java Main.java`
2. `java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/errado/nonAssoc.lan`

`java -cp ".;antlr-4.8-complete.jar" Main ../Lang/sintaxe/errado/attrCHAR.lan`

Linux:

1. `javac -cp ".:antlr-4.8-complete.jar" parser/*.java ast/*.java error/*.java Main.java`
2. `java -cp ".:antlr-4.8-complete.jar" Main attr_OR_SUGAR.lan`

### PASSOS PARA GERAR A ÁRVORE EM IMAGEM:

- Download do GRAPHVIZ : `https://graphviz.org/download/`
- Ao instalar, selecionar opção que adiciona o bin do Graphviz ao PATH do Windows
  - assim permitirá que use o comando dot no terminal
- Crie os métodos toDot() para todas as classes na pasta AST
- No Main.java, adicione trecho que irá gerar o arquivo .dot
- No terminal, execute o comando: `dot -Tpng ast.dot -o ast.png`

- No terminal, agora deve executar o comando: `dot -Tpng dotFiles/data.dot -o dotFiles/png/data.png`

### EXECUÇÃO DO SINTAXE_CERTO.PS1 (WINDOWS):

É necessário estar no diretório antlr

- Execute o comando `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass` para permitir a execução de scripts temporariamente nessa sessão do PowerShell
- Execute o comando `.\sintaxe_certo.ps1` para rodar o script

- Execute o comando `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass` para permitir a execução de scripts temporariamente nessa sessão do PowerShell
- Execute o comando `.\sintaxe_errado.ps1` para rodar o script
