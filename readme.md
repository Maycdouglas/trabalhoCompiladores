Estando na pasta trabalhoCompiladores/antrl:

comando para o ANTLR (Windows):
java -jar antlr-4.8-complete.jar -visitor -o parser parser/lang.g4

comando para o ANTLR (Linux):
java -jar antlr-4.8-complete.jar -visitor -o ./ parser/lang.g4


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

compilar o arquivo (Windows):
javac -cp ".;antlr-4.8-complete.jar" parser\*.java

compilar o arquivo (Linux):
javac -cp ".:antlr-4.8-complete.jar" parser/*.java


- gera vários arquivos dentro da pasta parser.

### PASSOS PARA TESTAR O PARSER:

<!-- 1. javac -cp ".;antlr-4.8-complete.jar" parser\*.java
2. javac -cp ".;antlr-4.8-complete.jar" Main.java -->
Windows:
1. `javac -cp ".;antlr-4.8-complete.jar" parser/*.java ast/*.java error/*.java Main.java`
2. `java -cp ".;antlr-4.8-complete.jar" Main attr_OR_SUGAR.lan`

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
