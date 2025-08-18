import java.util.Scanner;

public class returna {
    public static class fn_return {
        public int ret0;
        public String ret1;
        public boolean ret2;
        public fn_return(int ret0, String ret1, boolean ret2) {
            this.ret0 = ret0;
            this.ret1 = ret1;
            this.ret2 = ret2;
        }
    }

    public static fn_return fn() {
        return new fn_return((2 + 1), "a", false);
    }

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        boolean x = (fn().ret2);
        int y = (fn().ret0);
        String z = (fn().ret1);
        System.out.print(x);
        System.out.println();
        System.out.print(y);
        System.out.println();
        System.out.print(z);
        System.out.println();
    }

}
