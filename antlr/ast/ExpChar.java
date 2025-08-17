/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

public class ExpChar extends Exp {
    public final char value;
    public final int line;

    public ExpChar(char value, int line) {
        this.value = value;
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    private String escapeCharForDot(char c) {
        switch (c) {
            case '\n':
                return "\\\\n";
            case '\t':
                return "\\\\t";
            case '\r':
                return "\\\\r";
            case '\'':
                return "\\\\'";
            case '\\':
                return "\\\\\\\\";
            default:
                return Character.toString(c);
        }
    }

    @Override
    public String toDot(String parentId) {
        String idNode = "ExpChar" + hashCode();
        String escaped = escapeCharForDot(value);
        return String.format("\"%s\" [label=\"Char: %s\"];\n\"%s\" -> \"%s\";\n", idNode, escaped, parentId, idNode);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpChar(this);
    }
}
