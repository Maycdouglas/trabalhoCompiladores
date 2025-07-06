package ast;

import java.util.List;

public class CmdCall extends Cmd implements DotPrintable {
    public final String id;
    public final List<Exp> args;
    public final List<LValue> rets;

    public CmdCall(String id, List<Exp> args, List<LValue> rets) {
        this.id = id;
        this.args = args;
        this.rets = rets;
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "CmdCall" + hashCode();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\"%s\" [label=\"Call: %s\"];\n", idNode, id));
        sb.append(String.format("\"%s\" -> \"%s\";\n", parentId, idNode));
        for (Exp arg : args) {
            sb.append(arg.toDot(idNode));
        }
        for (LValue lval : rets) {
            sb.append(lval.toDot(idNode));
        }
        return sb.toString();
    }
}
