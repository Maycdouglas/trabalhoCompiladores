public class iterateVar {
    public static void main(String[] args) {
        int x = 1;
        for (int i = 0; i < 4; i++) {
            x = (x * 2);
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
        System.out.print(x);
    }
}
