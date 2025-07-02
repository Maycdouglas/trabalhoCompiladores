package parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Uso: java parser.Main <caminho do arquivo>");
            System.exit(1);
        }

        String caminhoArquivo = args[0];

        // Lê o arquivo passado como argumento
        CharStream input = CharStreams.fromStream(
            new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8);

        // Cria o lexer
        langLexer lexer = new langLexer(input);

        // Cria o token stream
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Cria o parser
        langParser parser = new langParser(tokens);

        // Adiciona listener de erro mais informativo
        parser.removeErrorListeners();
        parser.addErrorListener(new DiagnosticErrorListener());

        // Executa a regra inicial (prog)
        ParseTree tree = parser.prog();

        // Imprime a árvore sintática
        System.out.println(tree.toStringTree(parser));
    }
}
