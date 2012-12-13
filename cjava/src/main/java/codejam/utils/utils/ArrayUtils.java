package codejam.utils.utils;

public class ArrayUtils {
    public static void reset(int[][] array) {
        
    }
    
    public static char[][] copyArray(char[][] matrix) {
        char [][] myInt = new char[matrix.length][];
        for(int i = 0; i < matrix.length; i++)
        {
          char[] aMatrix = matrix[i];
          int   aLength = aMatrix.length;
          myInt[i] = new char[aLength];
          System.arraycopy(aMatrix, 0, myInt, 0, aLength);
        }
        
        return myInt;
    }
}