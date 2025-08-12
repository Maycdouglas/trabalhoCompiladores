# ====================================================================
# Makefile Simplificado para o Compilador da Linguagem Lang
# Uso:
#   make compile          - Compila o projeto
#   make clean            - Remove arquivos gerados
#   make run-semant <arq> - Executa a análise semântica
#   make run-interp <arq> - Interpreta o arquivo
#   make run-gen <arq>    - Gera, monta e executa o código Jasmin
# ====================================================================

# --- Variáveis de Configuração ---
ANTLR_JAR 		:= antlr-4.8-complete.jar
JASMIN_JAR 		:= jasmin.jar
MAIN_CLASS 		:= Main

# MUDANÇA: Voltamos para uma definição de fontes mais simples para evitar erros.
# Garanta que todas as suas pastas de código fonte estejam listadas aqui.
SOURCE_FILES 	:= parser/*.java ast/*.java error/*.java semant/*.java interpreter/*.java jasmin/*.java Main.java

# Ferramentas e Classpath
JAVAC 			:= javac
JAVA  			:= java
CP 				:= ".:$(ANTLR_JAR)"

# MUDANÇA: Captura o nome do arquivo passado após o alvo (ex: 'make run-gen arquivo.lan')
# $(word 2, ...) pega a segunda palavra do comando.
ARGS := $(word 2, $(MAKECMDGOALS))

# --- Alvos Principais ---

# Alvo padrão: compila tudo
all: compile

# Compila todos os arquivos .java do projeto
compile: generate
	@echo "--- Compilando fontes Java... ---"
	$(JAVAC) -cp $(CP) $(SOURCE_FILES)

# Gera o Lexer e o Parser com ANTLR
generate: parser/lang.g4
	@echo "--- Gerando Lexer/Parser com ANTLR... ---"
	$(JAVA) -jar $(ANTLR_JAR) -visitor parser/lang.g4

# Limpa todos os arquivos gerados e compilados
clean:
	@echo "--- Limpando... ---"
	rm -f parser/*.class ast/*.class error/*.class semant/*.class interpreter/*.class jasmin/*.class *.class *.j
	rm -f parser/lang*.java parser/lang*.tokens parser/lang*.interp

# --- Alvos de Execução ---

# Extrai o nome base do arquivo (ex: 'assoc' de '../path/assoc.lan')
BASENAME := $(basename $(notdir $(ARGS)))

# Alvo para Análise Sintática (-syn)
run-syn: compile
	@echo "--- Executando Análise Sintática em $(ARGS)... ---"
	$(JAVA) -cp $(CP) $(MAIN_CLASS) -syn $(ARGS)

# Alvo para Análise Semântica (-t)
run-semant: compile
	@echo "--- Executando Análise Semântica em $(ARGS)... ---"
	$(JAVA) -cp $(CP) $(MAIN_CLASS) -t $(ARGS)

# Alvo para Interpretação (-i)
run-interp: compile
	@echo "--- Interpretando $(ARGS)... ---"
	$(JAVA) -cp $(CP) $(MAIN_CLASS) -i $(ARGS)

# Alvo para Geração de Código Jasmin (-gen) com Montagem e Execução
run-gen: compile
	@echo "\n--- [1/3] Gerando código Jasmin para $(ARGS)... ---"
	$(JAVA) -cp $(CP) $(MAIN_CLASS) -gen $(ARGS)
	
	@echo "\n--- [2/3] Montando $(BASENAME).j com Jasmin... ---"
	$(JAVA) -jar $(JASMIN_JAR) $(BASENAME).j
	
	@echo "\n--- [3/3] Executando $(BASENAME).class ---"
	$(JAVA) -cp . $(BASENAME)

# Ignora o nome do arquivo para que 'make' não o interprete como um alvo
%:
	@:

# Declara os alvos que não são nomes de arquivos
.PHONY: all compile generate clean run-syn run-semant run-interp run-gen