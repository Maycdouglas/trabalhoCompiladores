import java.util.Scanner;

public class iterVarArr {
    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        int[] v = new int[10];
        for (int i = 0; i < 10; i++) {
            v[i] = (2 * i);
        }
        for (int i : v) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.print(".");
        System.out.println();
    }

}
