/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package ast;

import interpreter.Visitor;

// Data Abstract e Regular
public abstract class Data implements Def, DotPrintable {
    public final String name;
    public final int line;

    public Data(String name, int line) {
        this.name = name;
        this.line = line;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitData(this);
    }

}