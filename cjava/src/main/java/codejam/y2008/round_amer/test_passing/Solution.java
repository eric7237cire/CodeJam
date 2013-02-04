package codejam.y2008.round_amer.test_passing;

import java.util.Arrays;

import codejam.utils.utils.DoubleFormat;

public class Solution {

    //public DecimalFormat fmt = new DecimalFormat("0.0000000000");

    public String handleCase(InputData in) {
        int M = in.M;
        int Qn = in.Q;
        long total;
        double[][] P = in.prob;

        double[] R;

        int i, j;

        //Total # of tries = # of problems ^ 4, we can try eveything
        total = (1L << (Qn + Qn));
        if (M >= total) {
            return ("Case #" + in.testCase + ": " + DoubleFormat.df6.format(1.0));

        }

        R = new double[1];
        R[0] = 1.0;

        int k, x;
        for (i = 0; i < Qn; ++i) {
            //For each existing path, Add current question
            double[] T = new double[4 * R.length];
            k = 0;
            for (x = 0; x < R.length; ++x) {
                for (j = 0; j < 4; ++j) {
                    T[k] = R[x] * P[i][j];
                    ++k;
                }
            }
            
            //Only keep best elements
            Arrays.sort(T);
            
            R = trim(T, in);
        }

        double res = 0.0;
        for (i = 1; i <= M; ++i) {
            /**
             * Added because each path is the expected value
             * to get all the answers correct.
             * 
             * Also, it is not independent, guessing wrong
             * eliminates that possibility
             */
            res = res + R[R.length - i] ;
        }
        return "Case #" + in.testCase + ": " + DoubleFormat.df6.format(res);

    }

    public double[] trim(double[] T, InputData in) {
        if (T.length <= in.M)
            return T;

        
        double[] R = new double[in.M];
        System.arraycopy(T, T.length - in.M, R, 0, in.M);
        return R;
       
    }

}
