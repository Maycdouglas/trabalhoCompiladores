Criei a pasta antlr no disco C: e colei o arquivo .jar do ANTLR lá

comando para o ANTLR:
java -jar C:\antlr\antlr-4.8-complete.jar -visitor -o parser parser/lang.g4

Talvez não precise colocar esse aqui no disco C:
só informar o arquivo que já está na pasta do projeto

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
javac -cp ".;antlr-4.8-complete.jar" parser\*.java

- gera vários arquivos dentro da pasta parser.

### PASSOS PARA TESTAR O PARSER:

1. javac -cp ".;antlr-4.8-complete.jar" parser\*.java
2. java -cp ".;antlr-4.8-complete.jar" parser.Main caminho/para/arquivo.txt
