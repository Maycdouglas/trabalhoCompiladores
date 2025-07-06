import ast.*;
import parser.*;
import ast.ASTBuilder;
import error.SyntaxErrorListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

        // // Em tese deve impedir a geração de árvores parciais se tiver erro de sintaxe
        // parser.removeErrorListeners();
        // parser.addErrorListener(new SyntaxErrorListener());

        // Executa a regra inicial (prog)
        ParseTree tree = parser.prog();

        // Imprime a árvore sintática
        System.out.println(tree.toStringTree(parser));

        // Contrói a AST
        ASTBuilder visitor = new ASTBuilder();
        Prog ast = (Prog) visitor.visit(tree);

        System.out.println("AST criada com sucesso: " + ast);

        // Extrai o nome do arquivo sem path nem extensão
        String nomeArquivo = new java.io.File(caminhoArquivo).getName().replaceFirst("[.][^.]+$", "");
        String nomeSaidaDot = "dotFiles/" + nomeArquivo + ".dot";

        // Gera arquivo .dot da AST
        try (PrintWriter out = new PrintWriter(nomeSaidaDot)) {
            out.println("digraph AST {");
            out.print(ast.toDot(null));
            out.println("}");
            System.out.println("Arquivo " + nomeSaidaDot + " gerado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
