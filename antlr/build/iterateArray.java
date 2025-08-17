import java.util.Scanner;

public class iterateArray {
    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        int[] v = new int[3];
        v[0] = 10;
        v[1] = 20;
        v[2] = 30;
        for (int elem : v) {
            System.out.print(elem);
            System.out.print(" ");
        }
        System.out.println();
    }
}
