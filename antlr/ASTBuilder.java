package antlr;

import parser.langBaseVisitor;
import parser.langParser;
import ast.*;

import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends langBaseVisitor<Object> {

    @Override
    public Prog visitProg(langParser.ProgContext ctx) {
        List<Def> defs = new ArrayList<>();
        for (var defCtx : ctx.def()) {
            defs.add((Def) visit(defCtx)); // cast explícito
        }
        return new Prog(defs);
    }

    @Override
    public Decl visitDecl(langParser.DeclContext ctx) {
        String id = ctx.ID().getText();
        Type type = (Type) visit(ctx.type());
        return new Decl(id, type);
    }

    @Override
    public Type visitType(langParser.TypeContext ctx) {
        String baseType = ctx.btype().getText();
        int arrayDepth = ctx.LBRACK().size(); // cada par de colchetes conta como 1 dimensão
        return new Type(baseType, arrayDepth);
    }

    // Você pode adicionar os próximos visitadores: visitFun, visitParam, visitExp, etc.
}
