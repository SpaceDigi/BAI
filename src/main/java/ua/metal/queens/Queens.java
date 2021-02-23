package ua.metal.queens;

public class Queens {

    private final int[] x;

    public Queens(int N) {
        x = new int[N];
    }

    private boolean canPlaceQueen(int r, int c) {
        for (int i = 0; i < r; i++) {
            if (x[i] == c || (i - r) == (x[i] - c) || (i - r) == (c - x[i])) {
                return false;
            }
        }
        return true;
    }

    private void printQueens(int[] x) {
        int N = x.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (x[i] == j) {
                    System.out.print("Q ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean find = false;

    private void placeQueens(int r, int n) {
        for (int c = 0; c < n; c++) {
            if (canPlaceQueen(r, c)) {
                x[r] = c;
                if (r == n - 1) {
                    printQueens(x);
                    find = true;
                } else if (!find) {
                    placeQueens(r + 1, n);
                }
            }
        }
    }

    public void placeQueens() {
        //Randomly setting first queen
        x[0] = (int) (Math.random() * x.length);
        placeQueens(1, x.length);
    }


    public static void main(String[] args) {
        Queens q = new Queens(8);
        q.placeQueens();
    }

}