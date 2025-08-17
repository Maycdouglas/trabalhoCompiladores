import java.util.Scanner;

public class board {
    public static void printBoard(String[][] board, int nl, int nc) {
        int i = 0;
        for (int _i0 = 0; _i0 < nl; _i0++) {
            int j = 0;
            for (int _i1 = 0; _i1 < nc; _i1++) {
                System.out.print(board[i][j]);
                j = (j + 1);
            }
            System.out.println();
            i = (i + 1);
        }
    }

    public static String[][] startBoard(String c, int nl, int nc) {
        String[][] board = new String[][nl];
        int i = 0;
        for (int _i2 = 0; _i2 < nl; _i2++) {
            int j = 0;
            board[i] = new String[nc];
            for (int _i3 = 0; _i3 < nc; _i3++) {
                board[i][j] = c;
                j = (j + 1);
            }
            i = (i + 1);
        }
        return board;
    }

    public static void set(String[][] board, int x, int y) {
        board[x][y] = "A";
    }

    public static void main(String[] args) {
        Scanner _scanner = new Scanner(System.in);
        String[][] board = startBoard("*", 3, 4);
        printBoard(board, 3, 4);
        set(board, 1, 2);
        printBoard(board, 3, 4);
    }

}
