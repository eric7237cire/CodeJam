package codejam.y2012.round_1B.safety_numbers;

import java.util.Scanner;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {


    public Main() {
        super("A", 1,1);
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
            
            /**
             * The avg is the score they need to not be eliminated, anything
             * below this means that we can distribute audience points
             * to all the other scores lower than this one to be higher.
             * 
             * ex
             * 
             * 10 20 30
             * average = 60*2 / 3 = 40
             * 
             * If someone has 40 then there is no way for the others to have both >= 40
             * as there is not enough points leftover to distribute
             */
            double a = 100* (avg - in.score[i]) / sum;
            ans[i] = a;
            
            /**
             * But, if they don't need any help from the audience,
             * this changes the average.  We recompute and
             * start all over
             */
            if (a < 0) {
                neverLose[i] = true;
                ans[i] = 0;
                ++neverLoseCount;
                neverLoseSum += in.score[i];
                
                //Adjust the average to be distributed over the vulnerable places
                avg = (2.0d * sum - neverLoseSum) / (in.N - neverLoseCount);
                
                //Go back to the beginning for Vasilli
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
