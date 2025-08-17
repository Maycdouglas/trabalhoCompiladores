import java.util.Scanner;

public class array {
    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        int[] v = new int[5];
        int i = 0;
        for (int _i0 = 0; _i0 < 5; _i0++) {
            v[i] = (i * 2);
            i = (i + 1);
        }
        System.out.print(v[3]);
        System.out.println();
    }
}
