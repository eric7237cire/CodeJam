package codejam.y2012.round_1C.out_of_gas;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

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
    
    static class Node {
        double initialPosition;
        double initialTime;
        double initialVelocity;
        double acceleration;
        /*
         * p = ip + iv(t - ti) + .5 * acc * (t-ti)
         */
        
        double getIntersectionForPosition(double p) {
            //Expanding out p = ip + iv(t - ti) + .5 * acc * (t-ti)
            double a = .5 * acceleration;
            double b = initialVelocity - acceleration * initialTime;
            double c = -initialVelocity * initialTime + initialPosition + .5 * acceleration * initialTime*initialTime;
            
            //Setting equation = p and moving everything to 1 side
            c -= p;
            
            double t = solveQuadractic(a,b,c,initialTime);
            
            double pCheck = initialPosition + initialVelocity * (t - initialTime) + .5 * acceleration * (t-initialTime) * (t-initialTime);
            
            Preconditions.checkState(DoubleMath.fuzzyEquals(p, pCheck, 0.000001));
            return t;
            
        }
        
        double getIntersectionWithOtherCar(double otherCarInitialPosition, double otherCarInitialTime, double otherCarVelocity) {
          //Expanding out p = ip + iv(t - ti) + .5 * acc * (t-ti)
            double a = .5 * acceleration;
            double b = initialVelocity - acceleration * initialTime;
            double c = -initialVelocity * initialTime + initialPosition + .5 * acceleration * initialTime*initialTime;
            
            //Other equation p = p_oc0 + oc_v*(t - oc_t0)
            
            //Setting them equal and moving all to 1 side
            c =  c - otherCarInitialPosition + otherCarVelocity * otherCarInitialTime;
            b -= otherCarVelocity;
            
            double t = solveQuadractic(a,b,c,initialTime);
            
            double pCheck1 = initialPosition + initialVelocity * (t - initialTime) + .5 * acceleration * (t-initialTime) * (t-initialTime);
            double pCheck2 = otherCarInitialPosition + otherCarVelocity * (t - otherCarInitialTime);
            
            Preconditions.checkState(DoubleMath.fuzzyEquals(pCheck1, pCheck2, 0.000001));
            Preconditions.checkState( pCheck1 >= otherCarInitialPosition );
            Preconditions.checkState( pCheck1 >= initialPosition );
            return t;
        }

        public Node(double initialPosition, double initialTime, double initialVelocity, double acceleration) {
            super();
            this.initialPosition = initialPosition;
            this.initialTime = initialTime;
            this.initialVelocity = initialVelocity;
            this.acceleration = acceleration;
        }
    }
    
    static double solveQuadractic(double a, double b, double c, double minY) {
        double y1 = (-b + Math.sqrt(b*b - 4 * a * c)) / (2*a);
        double y2 = (-b - Math.sqrt(b*b - 4 * a * c)) / (2*a);
        
        if (DoubleMath.fuzzyCompare(y1, minY, 0.000001) >= 0)
            return y1;
        
        return y2;
                    
    }
    
    double intersection(double a, double v0, double otherCarInitialPosition, double otherCarVelocity) {
        /*v0*t + 0.5a*t^2 == p
        //pi + vi * t = p
        v0*t + 0.5a*t^2 == pi + vi * t
        0.5a*t^2 + v0*t - vi*t - pi == 0
        */
        
        double A = 0.5*a;
        double B = v0-otherCarVelocity;
        double C = -otherCarInitialPosition;
        
        double t1 = (-B + Math.sqrt(B*B - 4 * A * C)) / (2*A);
        double t2 = (-B - Math.sqrt(B*B - 4 * A * C)) / (2*A);
        
        if (t1 > 0)
            return t1;
        
        if (t2 > 0)
            return t2;
        
        return -10000;
    }

    public void testIntersections() {
        double j = 300;
        double h = 200;
        double i = 14;
        double g = 6;
        Node node = new Node(j, i, (j-h) / (i-g), 3);
        log.info("At 1000 : {}", node.getIntersectionForPosition(1000));
        
        
        node = new Node(j, i, (j-h) / (i-g), 9);
        double l = 600;
        double m = 24;
        double k = 15;
        double n = 800;
        log.info("At line : {}", node.getIntersectionWithOtherCar(l, k, (n-l)/(m-k)));
    }
    
    public String handleCase(InputData in) {

        
        
        
        
        double[] v = new double[in.N-1];
        for(int otherCarIdx = 1; otherCarIdx < in.N; ++otherCarIdx) {
            v[otherCarIdx-1] = (in.pos[otherCarIdx] - in.pos[otherCarIdx-1]) / (in.time[otherCarIdx] - in.time[otherCarIdx-1]);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        
        for(int aIdx = 0; aIdx < in.A; ++aIdx) {
            double a = in.acc[aIdx];
            List<Node> nodes = Lists.newArrayList();
            nodes.add(new Node(0,0,0, a));
            
            if (in.N == 1) {
                double tIntersection = nodes.get(0).getIntersectionForPosition(in.D);
                sb.append(DoubleFormat.df6.format(tIntersection));
                
                double tIntersectionCheck = intersection(a,0,in.D, 0);
                Preconditions.checkState(DoubleMath.fuzzyEquals(tIntersection, tIntersectionCheck,1e-7));
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
