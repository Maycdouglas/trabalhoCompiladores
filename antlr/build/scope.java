import java.util.Scanner;

public class scope {
    public static void inicio() {
        int x = 0;
        if ((x == 0)) {
            int y = 2;
            if (false) {
                boolean z = (y == x);
            }
            System.out.print(y);
        } else {
            z = 2;
            System.out.print(z);
        }
        System.out.print(" entrada> ");
        x = _scanner.nextInt();
    }

}
