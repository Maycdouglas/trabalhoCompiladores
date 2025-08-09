# Makefile para projeto ANTLR + Java (Linux)

# Variáveis
ANTLR_JAR := antlr-4.8-complete.jar
MAIN_CLASS := Main
# Adiciona o novo pacote 'semant' aos ficheiros fonte
SOURCE_FILES := parser/*.java ast/*.java error/*.java semant/*.java jasmin/*.java Main.java

# Regras principais
.PHONY: all clean run

all: compile

# Gera o parser a partir da gramática
generate:
	@echo "Gerando parser..."
	java -jar $(ANTLR_JAR) -visitor -o ./ parser/lang.g4

# Compila o projeto
compile: generate
	@echo "Compilando..."
	javac -cp ".:$(ANTLR_JAR)" $(SOURCE_FILES)

# Executa o programa (agora focado na análise semântica)
run: compile
	@echo "Executando com arquivo: $(word 2, $(MAKECMDGOALS))"
	@if [ -z "$(word 2, $(MAKECMDGOALS))" ]; then \
		echo "Por favor, especifique um arquivo: make run arquivo.lan"; \
		exit 1; \
	fi
	java -cp ".:$(ANTLR_JAR)" $(MAIN_CLASS) $(word 2, $(MAKECMDGOALS))

# Ignora tentativas de fazer o make interpretar o nome do arquivo como um target
%:
	@:

# Limpeza
clean:
	@echo "Limpando arquivos gerados..."
	rm -f parser/*.class ast/*.class error/*.class interpreter/*.class semant/*.class jasmin/*.class *.class *.j
	rm -f parser/lang*.java parser/lang*.tokens parser/lang*.interp
