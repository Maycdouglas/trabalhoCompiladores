import java.util.Scanner;

public class or_equiv {
    public static boolean or(boolean a, boolean b) {
        return (!(((!a) && (!b))));
    }

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        boolean a = true;
        boolean b = true;
        System.out.print(or(a, b));
        System.out.println();
        a = true;
        b = false;
        System.out.print(or(a, b));
        System.out.println();
        a = false;
        b = true;
        System.out.print(or(a, b));
        System.out.println();
        a = false;
        b = false;
        System.out.print(or(a, b));
        System.out.println();
    }

}
