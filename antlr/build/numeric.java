import java.util.Scanner;

public class numeric {
    public static class dowToOneRec_return {
        public float ret0;
        public float ret1;
        public dowToOneRec_return(float ret0, float ret1) {
            this.ret0 = ret0;
            this.ret1 = ret1;
        }
    }

    public static float guessRoot(float x, float a) {
        if ((x < (a * a))) {
            return guessRoot(x, (a / 2));
        }
        return a;
    }

    public static float sqrt(float x) {
        float g = guessRoot(x, (x / 2.0f));
        return sqrtrec(x, g);
    }

    public static float sqrtrec(float x, float x0) {
        float x1 = (((x0 + (x / x0))) / 2);
        float diff = ((x1 * x1) - x);
        if ((diff < 0.0f)) {
            diff = (((0.0f - 1.01f)) * diff);
        }
        if ((diff < 1.0E-6f)) {
            return x1;
        }
        return sqrtrec(x, x1);
    }

    public static float ln(float x) {
        dowToOneRec_return _dowToOneRec_ret0 = dowToOneRec(x, 1.0f);
        float a = _dowToOneRec_ret0.ret0;
        float n = _dowToOneRec_ret0.ret1;
        float y = (((a - 1.0f)) / ((a + 1.0f)));
        float k = 0.0f;
        float s = 0.0f;
        float t = y;
        for (int _i1 = 0; _i1 < 20; _i1++) {
            s = (s + (t / (((2.0f * k) + 1.0f))));
            t = ((t * y) * y);
            k = (k + 1.0f);
        }
        s = (2.0f * s);
        float log = ((((n - 1.0f)) * 2.3025851f) + s);
        return log;
    }

    public static dowToOneRec_return dowToOneRec(float n, float dc) {
        if ((n == 1.0f)) {
            return new dowToOneRec_return(n, dc);
        } else {
if ((((1.0f < n)) && ((n < 10.0f)))) {
            return new dowToOneRec_return(n, dc);
        } else {
if ((!(((!((10.0f < n))) && (!((n == 10.0f))))))) {
            dowToOneRec_return _dowToOneRec_ret2 = dowToOneRec((n / 10.0f), (dc + 1.0f));
            n = _dowToOneRec_ret2.ret0;
            dc = _dowToOneRec_ret2.ret1;
            return new dowToOneRec_return(n, dc);
        } else {
if ((n < 1.0f)) {
            dowToOneRec_return _dowToOneRec_ret3 = dowToOneRec((n * 10.0f), (dc - 1.0f));
            n = _dowToOneRec_ret3.ret0;
            dc = _dowToOneRec_ret3.ret1;
            return new dowToOneRec_return(n, dc);
        }
        }
        }
        }
        return new dowToOneRec_return(0.0f, 0.0f);
    }

    public static float lnb(float base, float x) {
        return (ln(x) / ln(base));
    }

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        float log = lnb(10.0f, 255.0f);
        System.out.print(log);
        System.out.println();
        System.out.print(sqrt(2.0f));
        System.out.println();
    }

}
