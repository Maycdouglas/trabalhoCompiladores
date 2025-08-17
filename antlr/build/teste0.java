import java.util.Scanner;

public class teste0 {
    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        int nlines = 5;
        int i = nlines;
        for (int _i0 = 0; _i0 < nlines; _i0++) {
            for (int _i1 = 0; _i1 < i; _i1++) {
                System.out.print("*");
            }
            i = (i - 1);
            System.out.println();
        }
    }

}
