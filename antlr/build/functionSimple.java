import java.util.Scanner;

public class functionSimple {
    public static int fibonacci(int n) {
        if ((n < 1)) {
return n;
        }
        if ((n == 1)) {
return n;
        }
        return (fibonacci((n - 1)) + fibonacci((n - 2)));
    }

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        int v = fibonacci(15);
        System.out.print(v);
        System.out.println();
    }

}
