package jasmin; // Ou o nome do pacote que você escolheu

import ast.*;
import interpreter.Visitor;
import java.util.HashMap;
import java.util.Map;

/**
 * @brief Visitor responsável por percorrer a AST e gerar código de montagem
 *        Jasmin.
 */
public class JasminGeneratorVisitor implements Visitor<Void> {

    private String className;
    private StringBuilder code = new StringBuilder();
    private int labelCounter = 0;

    // Mapeia nomes de variáveis para seus índices na JVM (locais)
    private Map<String, Integer> locals = new HashMap<>();
    private int nextLocalIndex = 0; // Começa em 0 para 'this'/'args', 1 para o primeiro local

    public JasminGeneratorVisitor(String className) {
        this.className = className;
    }

    /**
     * @brief Retorna o código Jasmin gerado.
     */
    public String getCode() {
        return code.toString();
    }

    /**
     * @brief Adiciona uma instrução ao buffer de código.
     */
    private void emit(String instruction) {
        code.append("    ").append(instruction).append("\n");
    }

    /**
     * @brief Adiciona um rótulo ao buffer de código.
     */
    private void emitLabel(String label) {
        code.append(label).append(":\n");
    }

    /**
     * @brief Gera um novo nome de rótulo único.
     */
    private String newLabel() {
        return "L" + (labelCounter++);
    }

    // --- Métodos de Visita ---

    /**
     * @brief Ponto de entrada da geração de código. Gera o cabeçalho da classe e o
     *        construtor.
     */
    @Override
    public Void visitProg(Prog prog) {
        emit(".class public " + className);
        emit(".super java/lang/Object");
        emit("");

        emit("; Construtor padrão");
        emit(".method public <init>()V");
        emit("    aload_0");
        emit("    invokenonvirtual java/lang/Object/<init>()V");
        emit("    return");
        emit(".end method");
        emit("");

        // Procura e visita apenas a função 'main' para começar
        for (Def def : prog.definitions) {
            if (def instanceof Fun && ((Fun) def).id.equals("main")) {
                def.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitFun(Fun fun) {
        emit("; Função: " + fun.id);
        emit(".method public " + fun.id + "()V");
        emit("    .limit locals " + (nextLocalIndex + 1));
        emit("    .limit stack 2");
        emit("");

        // Visita o corpo da função
        fun.body.accept(this);

        emit("    return");
        emit(".end method");
        emit("");
        return null;
    }

    @Override
    public Void visitCmdPrint(CmdPrint cmd) {
        emit("; Comando Print");
        emit(".method public static print(Ljava/lang/String;)V");
        emit("    .limit locals 1");
        emit("    .limit stack 2");
        emit("");
        emit("    new java/lang/StringBuilder");
        emit("    dup");
        emit("    invokespecial java/lang/StringBuilder/<init>()V");
        emit("    ldc \"" + cmd.value + "\"");
        emit("    invokevirtual java/lang/StringBuilder/append(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        emit("    pop");
        emit("    return");
        emit(".end method");
        emit("");
        return null;
    }

    @Override
    public Void visitCmdAssign(CmdAssign cmd) {
        emit("; Comando Assign");
        emit(".method public static assign(Ljava/lang/String;Ljava/lang/Object;)V");
        emit("    .limit locals 2");
        emit("    .limit stack 2");
        emit("");
        emit("    aload_0");
        emit("    aload_1");
        emit("    putstatic " + className + "/" + cmd.varName + "Ljava/lang/Object;");
        emit("    return");
        emit(".end method");
        emit("");
        return null;
    }
}