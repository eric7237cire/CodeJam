package codejam.y2008.round_2.star_wars;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import codejam.utils.linear.Simplex;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("C", 1, 0, 0);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.N = scanner.nextInt();
        in.x = new int[in.N];
        in.y = new int[in.N];
        in.z = new int[in.N];
        in.p = new int[in.N];
        
        for(int i = 0; i < in.N; ++i)
        {
            in.x[i] = scanner.nextInt();
            in.y[i] = scanner.nextInt();
            in.z[i] = scanner.nextInt();
            in.p[i] = scanner.nextInt();
        }
        return in;
    }

  
    @Override
    public String handleCase(InputData in) {
       
        Simplex s = new Simplex(4);
        s.addObjectiveFunctionToMinimize(Arrays.asList(0d,0d,0d,1d));
        
        for(int i = 0; i < in.N; ++i)
        {
            for(int dx=-1; dx <= 1; dx+=2)
            {
                for(int dy=-1; dy <= 1; dy+=2)
                {
                    for(int dz=-1; dz <= 1; dz+=2)
                    {
                        /*
                         * abs(x_i  - x) + ... <= p_i * Y
                         * x_i - x    ==>  -x ... <= p_i * Y - x_i
                         * -x_i + x
                         */
                        s.addConstraintLTE(Arrays.<Double>asList(
                                (double)dx,
                                (double)dy,
                                (double)dz,
                                (double)-in.p[i]
                                ), (double) dx * in.x[i] + dy * in.y[i] + dz * in.z[i]);
                        
                        log.debug( "{} x {} y {} z - {} Y <= {} + {} + {} = {}",
                                dx,dy,dz, -in.p[i],dx * in.x[i], dy * in.y[i], dz * in.z[i],
                                (double) dx * in.x[i] + dy * in.y[i] + dz * in.z[i]);
                    }
                    
                }
                
            }
        }
        
        List<Double> sol = Lists.newArrayList();
        s.solve(sol);

        return String.format("Case #%d: %s", in.testCase, DoubleFormat.df7.format(sol.get(sol.size()-1)));
    }
}