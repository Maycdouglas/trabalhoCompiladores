/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

import ast.*;
import parser.*;
import error.SyntaxErrorListener;
import semant.SemanticVisitor; // Importa o novo visitor

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Uso: java Main <caminho do arquivo de entrada>");
            System.exit(1);
        }

        String caminhoArquivo = args[0];

        // --- Etapa 1: Análise Léxica e Sintática ---
        CharStream input = CharStreams.fromStream(new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8);
        langLexer lexer = new langLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        langParser parser = new langParser(tokens);

        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.prog();

        if (errorListener.hasErrors()) {
            System.err.println("Erros de sintaxe detetados. Análise interrompida.");
            System.out.println("reject");
            return;
        }

        // --- Etapa 2: Construção da AST ---
        ASTBuilder astBuilder = new ASTBuilder();
        Prog ast = (Prog) astBuilder.visit(tree);
        System.out.println("AST criada com sucesso.");

        // --- Etapa 3: Análise Semântica ---
        System.out.println("--- Executando Análise Semântica ---");
        SemanticVisitor semanticVisitor = new SemanticVisitor();
        try {
            ast.accept(semanticVisitor);
            System.out.println("accept"); // Se não houver exceções, o programa é semanticamente correto
        } catch (RuntimeException e) {
            System.out.println("reject"); // Se houver uma exceção, o programa é inválido
            System.err.println("Erro Semântico: " + e.getMessage());
        }
    }
}
