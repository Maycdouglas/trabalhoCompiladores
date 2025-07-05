package ast;

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

    @Override
    public List<Type> visitRetTypes(langParser.RetTypesContext ctx) {
        List<Type> types = new ArrayList<>();
        for (var t : ctx.type()) {
            types.add((Type) visit(t));
        }
        return types;
    }

    @Override
    public Def visitDef(langParser.DefContext ctx) {
        if (ctx.data() != null)
            return (Def) visit(ctx.data());
        else
            return (Def) visit(ctx.fun());
    }

    @Override
    public DataRegular visitDataRegular(langParser.DataRegularContext ctx) {
        String name = ctx.TYID().getText();
        List<Decl> declarations = new ArrayList<>();
        for (var declCtx : ctx.decl()){
            declarations.add((Decl) visit(declCtx));
        }
        return new DataRegular(name, declarations);
    }

    @Override
    public DataAbstract visitDataAbstract(langParser.DataAbstractContext ctx){
        String name = ctx.TYID().getText();
        List<Decl> declarations = new ArrayList<>();
        List<Fun> functions = new ArrayList<>();

        for (var child : ctx.children){
            if (child instanceof langParser.DeclContext)
                declarations.add((Decl) visit(child));
            else if (child instanceof langParser.FunContext)
                functions.add((Fun) visit(child));
        }

        return new DataAbstract(name, declarations, functions);
    }

    @Override
    public Fun visitFun(langParser.FunContext ctx){
        String name = ctx.ID().getText();

        List<Param> params = new ArrayList<>();
        if(ctx.params() != null)
            params = (List<Param>) visit(ctx.params());

        List<Type> returnTypes = ctx.retTypes() != null ? visitRetTypes(ctx.retTypes()) : new ArrayList<>();
        if (ctx.retTypes() != null)
            returnTypes = (List<Type>) visit(ctx.retTypes());

        // Aqui poderá visitar o cmd quando estiver implementado
        // Cmd body = (Cmd) visit(ctx.cmd());

        return new Fun(name, params, returnTypes, null); //use null emporariamente para o corpo.
    }

    @Override
    public Param visitParam(langParser.ParamContext ctx) {
        String id = ctx.ID().getText();
        Type type = (Type) visit(ctx.type());
        return new Param(id, type);
    }

    @Override
    public List<Param> visitParams(langParser.ParamsContext ctx) {
        List<Param> list = new ArrayList<>();
        for (var p : ctx.param()) {
            list.add((Param) visit(p));
        }
        return list;
    }

    // Você pode adicionar os próximos visitadores: visitFun, visitParam, visitExp, etc.
}
