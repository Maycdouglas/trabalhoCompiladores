# Makefile para projeto ANTLR + Java (Linux)

# Variáveis
ANTLR_JAR := antlr-4.8-complete.jar
GRAMMAR := parser/lang.g4
MAIN_CLASS := Main
SOURCE_FILES := parser/*.java ast/*.java error/*.java Main.java

# Regras principais
.PHONY: all clean run

all: generate compile

# Gera o parser a partir da gramática
generate:
	@echo "Gerando parser..."
	java -jar $(ANTLR_JAR) -visitor -o ./ $(GRAMMAR)

# Compila o projeto
compile: generate
	@echo "Compilando..."
	javac -cp ".:$(ANTLR_JAR)" $(SOURCE_FILES)

# Executa o programa (aceita argumento direto: make run arquivo.lan)
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

# Gera a visualização da AST (make ast arquivo.lan)
ast: compile
	@echo "Gerando AST do arquivo: $(word 2, $(MAKECMDGOALS))"
	@if [ -z "$(word 2, $(MAKECMDGOALS))" ]; then \
		echo "Por favor, especifique um arquivo: make ast arquivo.lan"; \
		exit 1; \
	fi
	$(eval FILE_BASE=$(shell basename $(word 2, $(MAKECMDGOALS)) .lan))
	$(eval DOT_FILE=dotFiles/$(FILE_BASE).dot)
	$(eval PNG_FILE=dotFiles/png/$(FILE_BASE).png)

	@mkdir -p dotFiles/png

	java -cp ".:$(ANTLR_JAR)" $(MAIN_CLASS) $(word 2, $(MAKECMDGOALS))

	dot -Tpng $(DOT_FILE) -o $(PNG_FILE)
	@echo "AST .dot gerada em $(DOT_FILE)"
	@echo "AST .png gerada em $(PNG_FILE)"

# Limpeza
clean:
	@echo "Limpando arquivos gerados..."
	rm -f parser/*.class ast/*.class error/*.class *.class
	rm -f parser/lang*.java parser/lang*.tokens parser/lang*.interp