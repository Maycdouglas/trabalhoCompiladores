
import parser.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Teste {

    public static void main(String args[]) throws Exception {
	// Create a ANTLR CharStream from a file
	CharStream stream = CharStreams.fromFileName(args[0]);
	// create a lexer that feeds off of stream
	langLexer lex = new langLexer(stream);
	// create a buffer of tokens pulled from the lexer
	CommonTokenStream tokens = new CommonTokenStream(lex);
	// create a parser that feeds off the tokens buffer
	langParser parser = new langParser(tokens);

	ParseTree tree = parser.prog();
	System.out.println(tree.toStringTree(parser));
    }
}
