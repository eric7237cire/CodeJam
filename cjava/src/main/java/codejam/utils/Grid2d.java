package codejam.utils;

import java.util.Scanner;

public class Grid2d {
    public static int getSize(char[][] grid)
    {
        return grid.length * grid[0].length;
    }
    
    public static char[][] buildFromScanner(Scanner scanner, int rows, int cols) {

        char[][] g = new char[rows][cols]; 

        for (int r = 0; r < rows; ++r) {
            String rowStr = scanner.next();

            while (rowStr.length() < cols) {
                rowStr += scanner.next();
            }
            for (int c = 0; c < cols; ++c) {
                char ch = rowStr.charAt(c);
                g[r][c] = ch;
            }

        }

        return g;
    }
    
    public static int[][] deltaDir = new int[][]
            {
            {1, 0},
            {0, 1},
            {-1, 0},
            {0, -1}
            };
}
