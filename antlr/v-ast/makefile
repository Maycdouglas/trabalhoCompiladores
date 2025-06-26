compile: genparser Teste.java
	javac -cp .:antlr-4.8-complete.jar Teste.java

genparser: parser/lang.g4
	java -jar antlr-4.8-complete.jar parser/lang.g4

lexer:
	java -cp .:antlr-4.8-complete.jar org.antlr.v4.runtime.misc.TestRig parser.lang tokens -tokens $(filter-out $@,$(MAKECMDGOALS))

run: compile genparser
	java -cp .:antlr-4.8-complete.jar Teste $(filter-out $@,$(MAKECMDGOALS))

clean:
	rm -R parser/*Listener.java parser/langLexer* parser/langParser.java parser/lang.interp parser/lang.tokens
	find . -type f -name "*.class" -delete
	find . -type f -name "*~" -delete
