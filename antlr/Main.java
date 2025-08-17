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
import codegen.JavaCodeGeneratorVisitor;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Stack;

/**
 * @brief Classe principal que orquestra as fases do compilador.
 */
public class Main {

    private static langLexer lexer;
    private static CommonTokenStream tokens;
    private static langParser parser;
    private static ParseTree tree;
    private static Prog ast;
    private static SyntaxErrorListener errorListener;
    private static SemanticVisitor semanticVisitor;

    /**
     * @brief Ponto de entrada principal. Analisa os argumentos da linha de comando
     *        e chama as fases apropriadas do compilador.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Uso: java Main <diretiva> <caminho_do_arquivo.lan> [diretorio_saida]");
            System.err.println("Diretivas disponíveis: -syn, -t, -i, -gen, -src");
            System.exit(1);
        }

        String directive = args[0];
        String filePath = args[1];
        // O diretório de saída é opcional; o padrão é o diretório atual.
        String outputDir = (args.length > 2) ? args[2] : ".";

        errorListener = new SyntaxErrorListener();

        try {
            lexer(filePath);
            parser();

            if (directive.equals("-syn")) {
                System.out.println("accept");
                return;
            }

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
                    codeGen(filePath, outputDir);
                    break;
                case "-src":
                    semant();
                    sourceGen(filePath, outputDir);
                    break;
                case "-dot":
                    printParseTreeDot(tree, parser, "" + getBaseName(filePath) + ".dot");
                    break;
                default:
                    System.err.println("Diretiva desconhecida: " + directive);
                    System.exit(1);
            }

        } catch (RuntimeException e) {
            System.out.println("reject");
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
        semanticVisitor = new SemanticVisitor();
        ast.accept(semanticVisitor);
        if (semanticVisitor.hasErrors()) {
            System.out.println("reject"); // Sinaliza que o código foi rejeitado
            semanticVisitor.printErrors(); // Imprime todos os erros encontrados
            System.exit(1); // Termina a execução
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
    public static void codeGen(String inputPath, String outputDir) throws IOException {
        System.out.println("--- Gerando Código Jasmin ---");
        String baseName = getBaseName(inputPath);

        Map<String, Data> delta = semanticVisitor.getDelta();
        Map<String, Fun> theta = semanticVisitor.getTheta();
        JasminGeneratorVisitor jasminVisitor = new JasminGeneratorVisitor(baseName, delta, theta);

        ast.accept(jasminVisitor);
        String jasminCode = jasminVisitor.getCode();

        String outFileName = Paths.get(outputDir, baseName + ".j").toString();
        FileWriter writer = new FileWriter(outFileName);
        writer.write(jasminCode);
        writer.close();
        System.out.println("Código Jasmin gerado com sucesso em: " + outFileName);
    }

    public static void printParseTreeDot(ParseTree tree, langParser parser, String outputPath) throws IOException {
        try (PrintWriter out = new PrintWriter(outputPath)) {
            out.println("digraph AST {");
            out.print(ast.toDot(null));
            out.println("}");
            System.out.println("Arquivo " + outputPath + " gerado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @brief Fase 5c: Geração de Código Source-to-Source.
     *        Executa um visitor para gerar código .java a partir da AST.
     */
    // ATUALIZAR sourceGen para aceitar o diretório de saída
    public static void sourceGen(String inputPath, String outputDir) throws IOException {
        System.out.println("--- Gerando Código Java (Source-to-Source) ---");
        String baseName = getBaseName(inputPath);

        JavaCodeGeneratorVisitor javaVisitor = new JavaCodeGeneratorVisitor(baseName);
        String javaCode = ast.accept(javaVisitor);

        // Constrói o caminho de saída completo
        String outFileName = Paths.get(outputDir, baseName + ".java").toString();
        try (FileWriter writer = new FileWriter(outFileName)) {
            writer.write(javaCode);
        }
        System.out.println("Código Java gerado com sucesso em: " + outFileName);
    }

    /**
     * @brief Método auxiliar para extrair o nome base de um caminho de arquivo.
     */
    private static String getBaseName(String filePath) {
        int lastSlash = filePath.lastIndexOf('/');
        if (lastSlash == -1) {
            lastSlash = filePath.lastIndexOf('\\');
        }
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot == -1 || lastDot < lastSlash) {
            lastDot = filePath.length();
        }
        return filePath.substring(lastSlash + 1, lastDot);
    }
}