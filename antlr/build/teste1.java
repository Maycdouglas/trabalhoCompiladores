import java.util.Scanner;

public class teste1 {
    public static class divMod_return {
        public int ret0;
        public int ret1;
        public divMod_return(int ret0, int ret1) {
            this.ret0 = ret0;
            this.ret1 = ret1;
        }
    }

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        int x = 13;
        int y = 5;
        divMod_return _divMod_ret0 = divMod(x, y);
        int quo = _divMod_ret0.ret0;
        int res = _divMod_ret0.ret1;
        System.out.print(quo);
        System.out.println();
        System.out.print(res);
        System.out.println();
        System.out.print(x);
        System.out.println();
    }

    public static divMod_return divMod(int n, int q) {
        int x = 5;
        return new divMod_return((n / q), (n % q));
        System.out.print("z");
        System.out.println();
    }

}
