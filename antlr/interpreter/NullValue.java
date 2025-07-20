/* Feito por:
 FREDERICO DÔNDICI GAMA VIEIRA - 202165037AC
 MAYCON DOUGLAS HENRIQUE DA SILVA GOMES - 202065570C
*/

package interpreter;

public class NullValue implements Value {
    public NullValue() {
    }

    @Override
    public String toString() {
        return "null";
    }
}