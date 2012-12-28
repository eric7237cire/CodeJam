package codejam.y2012.round_1C.out_of_gas;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import codejam.utils.datastructures.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
        //return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        /*
         * 

    N lines follow, each of which contains two space-separated, real-valued numbers: a time ti in seconds, and a position xi in meters. The ti and xi values will be given in exactly 6 decimal places.

    One line follows, with A space-separated, real-valued numbers ai, which are accelerations in m/s2. The accelerations will be given in exactly 2 decimal places.

    The other car's position is specified by the (ti, xi) pairs. The car's position at time ti seconds is xi meters measured from the top of the hill (i.e. your initial position). The car travels at constant speed between time ti and ti+1. The positions and times will both be given in increasing order, with t0=0.
         */
            
        InputData in = new InputData(testCase);
        in.D = scanner.nextDouble();
        in.N = scanner.nextInt();
        in.A = scanner.nextInt();
        
        in.pos = new double[in.N];
        in.time = new double[in.N];
        in.acc = new double[in.A];
        
        for(int n = 0; n < in.N; ++n) {
            in.time[n] = scanner.nextDouble();
            in.pos[n] = scanner.nextDouble();
            
            
        }
        
        for(int n = 0; n < in.A; ++n) {
            in.acc[n] = scanner.nextDouble();
        }

        return in;
    }
    
    double intersection(double a, double v0, double pi, double vi) {
        /*v0*t + 0.5a*t^2 == p
        //pi + vi * t = p
        v0*t + 0.5a*t^2 == pi + vi * t
        0.5a*t^2 + v0*t - vi*t - pi == 0
        */
        
        double A = 0.5*a;
        double B = v0-vi;
        double C = -pi;
        
        double t1 = (-B + Math.sqrt(B*B - 4 * A * C)) / (2*A);
        double t2 = (-B - Math.sqrt(B*B - 4 * A * C)) / (2*A);
        
        if (t1 > 0)
            return t1;
        
        if (t2 > 0)
            return t2;
        
        return -10000;
    }

    public String handleCase(InputData in) {

        double[] v = new double[in.N-1];
        for(int n = 1; n < in.N; ++n) {
            v[n-1] = (in.pos[n] - in.pos[n-1]) / (in.time[n] - in.time[n-1]);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        
        for(int aIdx = 0; aIdx < in.A; ++aIdx) {
            double a = in.acc[aIdx];
            
            if (in.N == 1) {
                double tIntersection = intersection(a,0,in.D, 0);
                double pos = 0.5*a*tIntersection * tIntersection;
                sb.append(DoubleFormat.df6.format(tIntersection));
                sb.append('\n');
                continue;
            }
            //v0*t + 0.5*a*t2
            
            double t = in.time[1];
            
            //pi + p_i-1 - pi / t_i-1 - ti = p
            
            double tIntersection = intersection(a, 0, in.pos[0], v[0]);
            
            double pos = 0.5*a*tIntersection * tIntersection;
            double pos2 = in.pos[0] + v[0] * tIntersection;
            
            double minT = (in.D - in.pos[0]) / v[0];
            
            if (pos <= in.D) {
                sb.append(DoubleFormat.df6.format(minT));
                sb.append('\n');
            } else {
                tIntersection = intersection(a,0,in.D, 0);
                pos = 0.5*a*tIntersection * tIntersection;
                sb.append(DoubleFormat.df6.format(tIntersection));
                sb.append('\n');
            }
        }
        
        sb.delete(sb.length()-1, sb.length());
    
        return sb.toString();
    }

}
