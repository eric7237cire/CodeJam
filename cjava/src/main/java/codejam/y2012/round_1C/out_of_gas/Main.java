package codejam.y2012.round_1C.out_of_gas;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //   return new String[] {"sample.in"};
       // return new String[] {"B-small-practice.in"};
      //  return new String[] {"B-large-practice.in"};
        return new String[] { "B-small-practice.in", "B-large-practice.in" };
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
        double initialTime; 
        double acceleration; //always max
        /*
         * p = ip + iv(t - ti) + .5 * acc * (t-ti)
         */
        
        double getIntersectionForPosition(double p) {
            //Expanding out p = ip + iv(t - ti) + .5 * acc * (t-ti)
            double a = .5 * acceleration;
            double b =  - acceleration * initialTime;
            double c =  .5 * acceleration * initialTime*initialTime;
            
            //Setting equation = p and moving everything to 1 side
            c -= p;
            
            double[] tBoth = solveQuadractic(a,b,c);
            double t = tBoth[1];
            
            double pCheck = + .5 * acceleration * (t-initialTime) * (t-initialTime);
            
            Preconditions.checkState(DoubleMath.fuzzyEquals(p, pCheck, 1e-5));
            return t;
            
        }
        
        //time, position
        double[] getIntersectionWithOtherCar( double otherCarInitialTime, double otherCarInitialPosition, double otherCarVelocity) {
          //Expanding out p = ip + iv(t - ti) + .5 * acc * (t-ti)
            double a = .5 * acceleration;
            double b =  - acceleration * initialTime;
            double c = .5 * acceleration * initialTime*initialTime;
            
            //Other equation p = p_oc0 + oc_v*(t - oc_t0)
            
            //Setting them equal and moving all to 1 side
            c =  c - otherCarInitialPosition + otherCarVelocity * otherCarInitialTime;
            b -= otherCarVelocity;
            
            double[] tBoth = solveQuadractic(a,b,c);
            
            double t;
            if (DoubleMath.fuzzyCompare(tBoth[1], otherCarInitialTime, 0.0001) >= 0) {
                t = tBoth[1];
            } else {
                return null;
            }
            
            double pCheck1 =  .5 * acceleration * (t-initialTime) * (t-initialTime);
            double pCheck2 = otherCarInitialPosition + otherCarVelocity * (t - otherCarInitialTime);
            
           // log.info("check1 - check2 {}", pCheck1 - pCheck2);
            Preconditions.checkState(DoubleMath.fuzzyEquals(pCheck1, pCheck2, 0.003));
            Preconditions.checkState(DoubleMath.fuzzyCompare(pCheck1, otherCarInitialPosition, 0003 ) >= 0);
            Preconditions.checkState( pCheck1 >= 0 );
            return new double[] {t, pCheck1};
        }
        
        /**
         * Find some point t2, p2 by keeping acceleration to 0
         * for as long as possible.  This ends at t1,p1
         * @param t2
         * @param p2
         * @return t1, p1
         */
        double[] findIntermediatePoint(double t2, double p2) {
            double a = .5 * acceleration;
            double b = -t2*acceleration ;
            double c =  - p2 + 
                    .5 * acceleration * t2*t2;
            
            double[] tBoth = solveQuadractic(a,b,c);
            
            double t1;
            if (DoubleMath.fuzzyCompare(tBoth[0], initialTime, 0.0001) >= 0) {
                t1 = tBoth[0];
            } else
            if (DoubleMath.fuzzyCompare(tBoth[1], initialTime, 0.0001) >= 0) {
                t1 = tBoth[1];
            } else {
                return null;
            }
            
            double p1 = 0;
            
            double pCheck1 = p1 + .5 * acceleration * (t2-t1) * (t2-t1);
            Preconditions.checkState(DoubleMath.fuzzyEquals(pCheck1, p2, .0003));
            
            return new double[] { t1, p1 };
        }

        public Node(double initialTime, double acceleration) {
            super();
            this.initialTime = initialTime;
            this.acceleration = acceleration;
        }

        @Override
        public String toString() {
            return "Node [tStart=" + initialTime + 
                    ", acc="
                    + acceleration + "]";
        }
    }

    
    private static double[] solveQuadractic(double a, double b, double c) {
        double det = Math.sqrt(b*b - 4 * a * c);
        double y1 = (-b + det) / (2*a);
        double y2 = (-b - det) / (2*a);
        
        return new double[] { Math.min(y1,y2), Math.max(y1,y2)};
                    
    }
    
    
    public String handleCase(InputData in) {

        
        
        
        
        double[] v = new double[in.N-1];
        for(int otherCarIdx = 1; otherCarIdx < in.N; ++otherCarIdx) {
            v[otherCarIdx-1] = (in.pos[otherCarIdx] - in.pos[otherCarIdx-1]) / (in.time[otherCarIdx] - in.time[otherCarIdx-1]);
        }
        
        
        //Sanitize input, make last point stop at D and remove all after it
        for(int i = 1; i < in.N; ++i) {
            if (in.pos[i] > in.D) {
                if (i != in.N - 1) {
                    in.N = i + 1;
                    v = Arrays.copyOf(v, in.N - 1);
                    in.pos = Arrays.copyOf(in.pos, in.N);
                    in.time = Arrays.copyOf(in.time, in.N);
                }
                Preconditions.checkState(i == in.N - 1);
                in.pos[i] = in.D;
                in.time[i] = in.time[i-1] + (in.pos[i] - in.pos[i-1]) / v[i-1];
            }
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        
        for(int aIdx = 0; aIdx < in.A; ++aIdx) {
            double acc = in.acc[aIdx];
            Node node = new Node(0, acc);
            
           
            
            for (int otherCarIdx = 0; otherCarIdx < in.N - 1; ++otherCarIdx) {

                // Determine if the node doesn't intersect the car at all
                double[] timePos = node.getIntersectionWithOtherCar(in.time[otherCarIdx], in.pos[otherCarIdx], v[otherCarIdx]);

                if (timePos == null) {
                    continue;
                }
                if (timePos[1] >= in.pos[otherCarIdx + 1] || timePos[0] >= in.time[otherCarIdx + 1]) {
                    // no intersection
                    log.debug("No intersection between node {} and car {}", node, otherCarIdx);
                    continue;
                }
             
                timePos = node.findIntermediatePoint(in.time[otherCarIdx + 1], in.pos[otherCarIdx + 1]);

                log.debug("Extending line from node {} to time {}, pos {}", node, timePos[0], timePos[1]);

                Node replaceNode = new Node(timePos[0], acc);
                node = replaceNode;

            }    
            
            double tIntersection = node.getIntersectionForPosition(in.D);
            sb.append(DoubleFormat.df6.format(tIntersection));
            
            //double tIntersectionCheck = intersection(a,0,in.D, 0);
            //Preconditions.checkState(DoubleMath.fuzzyEquals(tIntersection, tIntersectionCheck,1e-7));
            sb.append('\n');
            continue;
        
        }
        
        sb.delete(sb.length()-1, sb.length());
    
        return sb.toString();
    }

}
