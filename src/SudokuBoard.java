import java.util.Random;

public class SudokuBoard {
    private int[][] board;
    private int[][] solution;
    private static final int[][][] predefinedBoards = {

            {
                    {0, 0, 0, 2, 6, 0, 7, 0, 1},
                    {6, 8, 0, 0, 7, 0, 0, 9, 0},
                    {1, 9, 0, 0, 0, 4, 5, 0, 0},
                    {8, 2, 0, 1, 0, 0, 0, 4, 0},
                    {0, 0, 4, 6, 0, 2, 9, 0, 0},
                    {0, 5, 0, 0, 0, 3, 0, 2, 8},
                    {0, 0, 9, 3, 0, 0, 0, 7, 4},
                    {0, 4, 0, 0, 5, 0, 0, 3, 6},
                    {7, 0, 3, 0, 1, 8, 0, 0, 0}
            },

            //------------------------- 2nd ------------------------------
            {
                    {1, 0, 0, 4, 8, 9, 0, 0, 6},
                    {7, 3, 0, 0, 0, 0, 0, 4, 0},
                    {0, 0, 0, 0, 0, 1, 2, 9, 5},
                    {0, 0, 7, 1, 2, 0, 6, 0, 0},
                    {5, 0, 0, 7, 0, 3, 0, 0, 8},
                    {0, 0, 6, 0, 9, 5, 7, 0, 0},
                    {9, 1, 4, 6, 0, 0, 0, 0, 0},
                    {0, 2, 0, 0, 0, 0, 0, 3, 7},
                    {8, 0, 0, 5, 1, 2, 0, 0, 4}
            },

    };


    private static final int[][][] solutions = {
            // ... Corresponding solutions for each predefined board
            {
                    {4, 3, 5, 2, 6, 9, 7, 8, 1},
                    {6, 8, 2, 5, 7, 1, 4, 9, 3},
                    {1, 9, 7, 8, 3, 4, 5, 6, 2},
                    {8, 2, 6, 1, 9, 5, 3, 4, 7},
                    {3, 7, 4, 6, 8, 2, 9, 1, 5},
                    {9, 5, 1, 7, 4, 3, 6, 2, 8},
                    {5, 1, 9, 3, 2, 6, 8, 7, 4},
                    {2, 4, 8, 9, 5, 7, 1, 3, 6},
                    {7, 6, 3, 4, 1, 8, 2, 5, 9}
            },

            //------------------------- 2nd ------------------------------
            {
                    {1, 5, 2, 4, 8, 9, 3, 7, 6},
                    {7, 3, 9, 2, 5, 6, 8, 4, 1},
                    {4, 6, 8, 3, 7, 1, 2, 9, 5},
                    {3, 8, 7, 1, 2, 4, 6, 5, 9},
                    {5, 9, 1, 7, 6, 3, 4, 2, 8},
                    {2, 4, 6, 8, 9, 5, 7, 1, 3},
                    {9, 1, 4, 6, 3, 7, 5, 8, 2},
                    {6, 2, 5, 9, 4, 8, 1, 3, 7},
                    {8, 7, 3, 5, 1, 2, 9, 6, 4}
            },

    };


    public SudokuBoard() {
        // Randomly select one of the predefined boards to be the current board
        Random random = new Random();
        int index = random.nextInt(predefinedBoards.length);
        board = copyBoard(predefinedBoards[index]);
        solution = copyBoard(solutions[index]);
    }

    private int[][] copyBoard(int[][] source) {
        int[][] copy = new int[source.length][source.length];
        for (int row = 0; row < source.length; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, source[row].length);
        }
        return copy;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolution() {
        return solution;
    }
}