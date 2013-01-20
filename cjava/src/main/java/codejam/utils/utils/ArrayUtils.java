package codejam.utils.utils;

import java.util.List;

import com.google.common.base.Preconditions;

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
    
    public static <T extends Comparable<T> >  int  binarySearch(int loIdx, int hiIdx, List<T> list, T target)
    {
        if (list.get(hiIdx).compareTo(target) <= 0) {
            //Everything in the range is lower than target
            return hiIdx;
        }
        if (list.get(loIdx).compareTo(target) > 0) {
            //Everything is greater
            return loIdx;
        }
        //  invariant list[loIdx] <= target ; list[hiIdx] > target
        while (true) {
            int midIdx = loIdx + (hiIdx - loIdx) / 2;

            T value = list.get(midIdx);

            if (value.compareTo(target) <= 0) {
                loIdx = midIdx;
            } else {
                hiIdx = midIdx;
            }

            Preconditions.checkState(loIdx <= hiIdx);

            if (hiIdx - loIdx <= 1)
                break;
        }
        
        return loIdx;
    }
}