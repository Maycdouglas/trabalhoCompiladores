/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package jasmin;

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
    private int nextLocalIndex = 0;

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
     * @brief Adiciona o cabeçalho de um método ao buffer de código.
     */
    private void emitHeader(String header) {
        code.append(header).append("\n");
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

    @Override
    public Void visitProg(Prog prog) {
        emitHeader(".class public " + className);
        emitHeader(".super java/lang/Object");
        emitHeader("");

        emitHeader("; Construtor padrão");
        emitHeader(".method public <init>()V");
        emit("aload_0");
        emit("invokenonvirtual java/lang/Object/<init>()V");
        emit("return");
        emitHeader(".end method");
        emitHeader("");

        // Visita todas as definições (funções e dados)
        for (Def def : prog.definitions) {
            def.accept(this);
        }
        return null;
    }

    @Override
    public Void visitFun(Fun fun) {
        locals.clear();
        nextLocalIndex = 0; // Para métodos estáticos, o índice 0 já está disponível para o primeiro
                            // argumento.

        // TODO: Construir a assinatura do método com base nos parâmetros e retornos de
        // 'fun'
        String signature = "main([Ljava/lang/String;)V"; // Exemplo para main

        emitHeader(".method public static " + signature);
        emit("    .limit stack 20"); // TODO: Calcular dinamicamente
        emit("    .limit locals 20"); // TODO: Calcular dinamicamente
        emitHeader("");

        fun.body.accept(this);

        emit("return");
        emitHeader(".end method");
        return null;
    }

    @Override
    public Void visitCmdPrint(CmdPrint cmd) {
        emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
        cmd.value.accept(this);
        // TODO: Determinar o tipo da expressão para chamar a sobrecarga correta do
        // println
        // Exemplo para Inteiro:
        emit("invokevirtual java/io/PrintStream/println(I)V");
        return null;
    }

    @Override
    public Void visitExpInt(ExpInt exp) {
        emit("ldc " + exp.value);
        return null;
    }

    @Override
    public Void visitCmdAssign(CmdAssign cmd) {
        // TODO: Implementar a geração de código para atribuição.
        // 1. Avaliar a expressão do lado direito (o valor estará no topo da pilha).
        // 2. Determinar o índice da variável local (LValueId).
        // 3. Usar 'istore' (ou 'fstore', 'astore', etc.) para armazenar o valor.
        return null;
    }

    @Override
    public Void visitExpVar(ExpVar expVar) {
        // TODO: Implementar o carregamento de uma variável.
        // 1. Obter o índice da variável a partir do mapa 'locals'.
        // 2. Usar 'iload' (ou 'fload', 'aload', etc.) para carregar o valor na pilha.
        return null;
    }

    // --- Placeholders para os métodos restantes ---

    @Override
    public Void visitCmd(Cmd exp) {
        return null;
    }

    @Override
    public Void visitCmdBlock(CmdBlock cmd) {
        for (Cmd c : cmd.cmds) {
            c.accept(this);
        }
        return null;
    }

    @Override
    public Void visitCmdCall(CmdCall cmd) {
        /* TODO */ return null;
    }

    @Override
    public Void visitCmdIf(CmdIf cmd) {
        /* TODO */ return null;
    }

    @Override
    public Void visitCmdIterate(CmdIterate cmd) {
        /* TODO */ return null;
    }

    @Override
    public Void visitCmdRead(CmdRead cmd) {
        /* TODO */ return null;
    }

    @Override
    public Void visitCmdReturn(CmdReturn cmd) {
        /* TODO */ return null;
    }

    @Override
    public Void visitData(Data data) {
        /* TODO (gerar campos estáticos ou classes internas se necessário) */ return null;
    }

    @Override
    public Void visitDataAbstract(DataAbstract data) {
        /* TODO */ return null;
    }

    @Override
    public Void visitDataRegular(DataRegular data) {
        /* TODO */ return null;
    }

    @Override
    public Void visitDecl(Decl decl) {
        return null;
    } // Geralmente não gera código diretamente

    @Override
    public Void visitDef(Def def) {
        def.accept(this);
        return null;
    }

    @Override
    public Void visitExp(Exp exp) {
        exp.accept(this);
        return null;
    }

    @Override
    public Void visitExpBinOp(ExpBinOp exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpBool(ExpBool exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpCall(ExpCall exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpCallIndexed(ExpCallIndexed exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpChar(ExpChar exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpField(ExpField exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpFloat(ExpFloat exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpIndex(ExpIndex exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpNew(ExpNew exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpNull(ExpNull exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitExpParen(ExpParen exp) {
        exp.exp.accept(this);
        return null;
    }

    @Override
    public Void visitExpUnaryOp(ExpUnaryOp exp) {
        /* TODO */ return null;
    }

    @Override
    public Void visitItCond(ItCond itCond) {
        itCond.accept(this);
        return null;
    }

    @Override
    public Void visitItCondExpr(ItCondExpr itCondExpr) {
        itCondExpr.expression.accept(this);
        return null;
    }

    @Override
    public Void visitItCondLabelled(ItCondLabelled itCondLabelled) {
        /* TODO */ return null;
    }

    @Override
    public Void visitLValue(LValue lValue) {
        lValue.accept(this);
        return null;
    }

    @Override
    public Void visitLValueField(LValueField lValueField) {
        /* TODO */ return null;
    }

    @Override
    public Void visitLValueId(LValueId lValueId) {
        /* TODO */ return null;
    }

    @Override
    public Void visitLValueIndex(LValueIndex lValueIndex) {
        /* TODO */ return null;
    }

    @Override
    public Void visitParam(Param param) {
        return null;
    } // Tratado em visitFun

    @Override
    public Void visitType(Type type) {
        return null;
    } // Geralmente não gera código diretamente
}