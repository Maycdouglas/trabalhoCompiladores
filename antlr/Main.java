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
            System.err.println("Uso: java Main <arquivo de entrada> [arquivo de saida .dot]");
            System.exit(1);
        }

        String caminhoArquivoEntrada = args[0];
        // Se o caminho de saída for passado, usa-o. Senão, o padrão é
        // "dotFiles/output.dot".
        String caminhoArquivoSaida = (args.length > 1) ? args[1] : "dotFiles/output.dot";

        // Configuração do Lexer e Parser
        CharStream input = CharStreams.fromStream(new FileInputStream(caminhoArquivoEntrada), StandardCharsets.UTF_8);
        langLexer lexer = new langLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        langParser parser = new langParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new SyntaxErrorListener());

        // Processamento e construção da AST
        ParseTree tree = parser.prog();
        System.out.println(tree.toStringTree(parser));
        ASTBuilder visitor = new ASTBuilder();
        Prog ast = (Prog) visitor.visit(tree);
        System.out.println("AST criada com sucesso: " + ast);

        // Gera o arquivo .dot no caminho exato que foi passado ou no padrão
        try (PrintWriter out = new PrintWriter(caminhoArquivoSaida)) {
            out.println("digraph AST {");
            out.print(ast.toDot(null));
            out.println("}");
            System.out.println("Arquivo " + caminhoArquivoSaida + " gerado com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo de saída: " + e.getMessage());
            System.err.println("Verifique se o diretório de destino '"
                    + new java.io.File(caminhoArquivoSaida).getParent() + "' existe.");
            System.exit(1);
        }
    }
}