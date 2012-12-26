package codejam.y2012.round_1B.safety_numbers;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
     //    return new String[] {"sample.in"};
        return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();

        in.score = new int[in.N];

        for (int i = 0; i < in.N; ++i) {
            in.score[i] = scanner.nextInt();
        }

        return in;
    }

    public String handleCase(InputData in) {

        int sum = 0;

        for (int i = 0; i < in.N; ++i) {
            sum += in.score[i];
        }

        double avg = 2d * sum / in.N;

        
        boolean[] neverLose = new boolean[in.N];
        double[] ans = new double[in.N];
        
        int neverLoseSum = 0;
        int neverLoseCount = 0;
        
        for (int i = 0; i < in.N; ++i) {
            if (neverLose[i])
                continue;
            
            double a = 100* (avg - in.score[i]) / sum;
            ans[i] = a;
            
            if (a < 0) {
                neverLose[i] = true;
                ans[i] = 0;
                ++neverLoseCount;
                neverLoseSum += in.score[i];
                avg = (2.0d * sum - neverLoseSum) / (in.N - neverLoseCount);
                i=-1;
                continue;
            }
            
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d: ", in.testCase));

        for (int i = 0; i < in.N; ++i) {
            sb.append(DoubleFormat.df6.format(ans[i]));
            sb.append(" ");
        }

        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

}
