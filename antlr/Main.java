/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

import ast.*;
import parser.*;
import error.SyntaxErrorListener;
import interpreter.*;
import semant.SemanticVisitor;
import jasmin.JasminGeneratorVisitor;

// import codegen.JasminGeneratorVisitor; // Descomentar quando implementar

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @brief Classe principal que orquestra as fases do compilador.
 */
public class Main {

    // --- Campos Estáticos para Armazenar os Resultados de Cada Fase ---
    private static langLexer lexer;
    private static CommonTokenStream tokens;
    private static langParser parser;
    private static ParseTree tree;
    private static Prog ast;
    private static SyntaxErrorListener errorListener;

    /**
     * @brief Ponto de entrada principal. Analisa os argumentos da linha de comando
     *        e chama as fases apropriadas do compilador.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Uso: java Main <diretiva> <caminho_do_arquivo.lan>");
            System.err.println("Diretivas disponíveis: -syn, -t, -i, -gen");
            System.exit(1);
        }

        String directive = args[0];
        String filePath = args[1];

        errorListener = new SyntaxErrorListener();

        try {
            // Execução do analisador léxico e sintático, necessária em todas as diretivas
            lexer(filePath);
            parser();

            if (directive.equals("-syn")) {
                System.out.println("accept");
                return;
            }

            // Geração de árvore sintática
            ast();

            switch (directive) {
                case "-t":
                    semant();
                    System.out.println("accept");
                    break;
                case "-i":
                    semant();
                    interpret();
                    break;
                case "-gen":
                    semant();
                    codeGen(filePath);
                    break;
                default:
                    System.err.println("Diretiva desconhecida: " + directive);
                    System.exit(1);
            }

        } catch (RuntimeException e) {
            System.out.println("reject");
            // Imprime a mensagem de erro específica da fase que falhou
            System.err.println(e.getMessage());
        }
    }

    /**
     * @brief Fase 1: Análise Léxica. Cria o CharStream, o Lexer e o TokenStream a
     *        partir do arquivo de entrada.
     */
    public static void lexer(String filePath) throws IOException {
        CharStream input = CharStreams.fromStream(new FileInputStream(filePath), StandardCharsets.UTF_8);
        lexer = new langLexer(input);

        lexer.removeErrorListeners();
        lexer.addErrorListener(new SyntaxErrorListener());

        tokens = new CommonTokenStream(lexer);
    }

    /**
     * @brief Fase 2: Análise Sintática. Cria o Parser, adiciona o listener de erros
     *        e gera a ParseTree.
     */
    public static void parser() {
        parser = new langParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        tree = parser.prog();

        if (errorListener.hasErrors()) {
            throw new RuntimeException("Erro de Sintaxe: O programa foi rejeitado pelo parser.");
        }
    }

    /**
     * @brief Fase 3: Construção da Árvore de Sintaxe Abstrata (AST). Usa o
     *        ASTBuilder para converter a ParseTree em uma AST.
     */
    public static void ast() {
        ASTBuilder astBuilder = new ASTBuilder();
        ast = (Prog) astBuilder.visit(tree);
        System.out.println("AST criada com sucesso.");
    }

    /**
     * @brief Fase 4: Análise Semântica (Verificação de Tipos). Executa o
     *        SemanticVisitor sobre a AST. Lança uma exceção em caso de erro.
     */
    public static void semant() {
        System.out.println("--- Executando Análise Semântica ---");
        SemanticVisitor semanticVisitor = new SemanticVisitor();
        try {
            ast.accept(semanticVisitor);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro Semântico: " + e.getMessage());
        }
    }

    /**
     * @brief Fase 5a: Interpretação. Executa o InterpreterVisitor sobre a AST para
     *        rodar o programa.
     */
    public static void interpret() {
        System.out.println("--- Executando o Interpretador ---");
        InterpreterVisitor interpreter = new InterpreterVisitor();
        ast.accept(interpreter);
    }

    /**
     * @brief Fase 5b: Geração de Código Jasmin. Executa um visitor para gerar
     *        código .j a partir da AST.
     */
    public static void codeGen(String inputPath) throws IOException {
        System.out.println("--- Gerando Código Jasmin ---");
        String baseName = getBaseName(inputPath);

        JasminGeneratorVisitor jasminVisitor = new JasminGeneratorVisitor(baseName);
        ast.accept(jasminVisitor);
        String jasminCode = jasminVisitor.getCode();

        String outFileName = baseName + ".j";
        FileWriter writer = new FileWriter(outFileName);
        writer.write(jasminCode);
        writer.close();
        System.out.println("Código Jasmin gerado em: " + outFileName);

    }

    /**
     * @brief Método auxiliar para extrair o nome base de um caminho de arquivo.
     */
    private static String getBaseName(String filePath) {
        int lastSlash = filePath.lastIndexOf('/');
        if (lastSlash == -1) {
            lastSlash = filePath.lastIndexOf('\\'); // Para compatibilidade com Windows
        }
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1 || lastDot < lastSlash) {
            lastDot = filePath.length();
        }
        return filePath.substring(lastSlash + 1, lastDot);
    }
}