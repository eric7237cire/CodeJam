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

        total = (1L << (Qn + Qn));
        if (M >= total) {
            return ("Case #" + in.testCase + ": " + DoubleFormat.df6.format(1.0));

        }

        R = new double[1];
        R[0] = 1.0;

        int k, x;
        for (i = 0; i < Qn; ++i) {
            double[] T = new double[4 * R.length];
            k = 0;
            for (x = 0; x < R.length; ++x) {
                for (j = 0; j < 4; ++j) {
                    T[k] = R[x] * P[i][j];
                    ++k;
                }
            }
            Arrays.sort(T);
            R = trim(T, in);
        }

        double res = 0.0;
        for (i = 1; i <= M; ++i) {
            // res=res*(1.0-R.get(total-i));
            res = res + R[R.length - i];
        }
        return "Case #" + in.testCase + ": " + DoubleFormat.df6.format(res);

    }

    public double[] trim(double[] T, InputData in) {
        if (T.length <= in.M)
            return T;

        double[] R = new double[in.M];
        int i, k;
        k = 1;
        for (i = 0; i < in.M; ++i) {
            R[i] = T[T.length - k];
            ++k;
        }
        return R;
    }

}
