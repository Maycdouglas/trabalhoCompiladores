import ast.*;
import parser.*;
import ast.ASTBuilder;
import error.SyntaxErrorListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File; // Importa a classe File
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Uso: java Main <caminho do arquivo de entrada> <diretorio de saida>");
            System.exit(1);
        }

        String caminhoArquivo = args[0];
        String outputDir = args[1];

        // Configuração do Lexer e Parser
        CharStream input = CharStreams.fromStream(new FileInputStream(caminhoArquivo), StandardCharsets.UTF_8);
        langLexer lexer = new langLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        langParser parser = new langParser(tokens);

        // Adiciona listener de erro personalizado
        SyntaxErrorListener errorListener = new SyntaxErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        // Processamento e construção da AST
        ParseTree tree = parser.prog();
        System.out.println(tree.toStringTree(parser));

        // Verifica se houve erro léxico ou sintático
        if (errorListener.hasErrors()) {
            System.err.println("Erros foram detectados. A AST não será gerada.");
            return;
        }

        // Verifica se houve erro léxico ou sintático
        ASTBuilder visitor = new ASTBuilder();
        Prog ast = (Prog) visitor.visit(tree);
        System.out.println("AST criada com sucesso: " + ast);

        String nomeArquivoBase = new File(caminhoArquivo).getName().replaceFirst("[.][^.]+$", "");
        String nomeSaidaDot = outputDir + File.separator + nomeArquivoBase + ".dot";

        // Garante que o diretório de destino exista
        new File(outputDir).mkdirs();

        // Gera o arquivo .dot
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