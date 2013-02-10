package codejam.y2012.round_final.twirling_freedom;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.complex.Complex;

import codejam.utils.geometry.GrahamScan;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.Point2D;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler 
    implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    public Main() 
    {
        super("D", 1, 1, 0);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();
        in.M = scanner.nextInt();

        in.stars = Lists.newArrayList();

        for (int i = 0; i < in.N; ++i) {

            in.stars.add(new PointInt(scanner.nextInt(),scanner.nextInt()));
        }
        return in;
    }

    
    /**
     * From the equations, if we rotate around say 10 stars ; q0 to q9
     * then we organize them like
     * 
     * 6, 2      == choice[0]  * -1+i
     * 7, 3      == choice[1]  * -1-i
     * 8, 4, 0   == choice[2]  * 1-i
     * 9, 5, 1   == choice[3]  * 1+i
     */
    Complex[] eqMult = new Complex[] {
            new Complex(-1, 1),
            new Complex(-1, -1),
            new Complex(1, -1),
            new Complex(1, 1)
    };
    
    
    /**
     * Take each star and multiply it by each of the 4 possible
     * directions
     * 
     */
    public Point2D[][] buildStarVectors(InputData in) {
        
        /**
         * Organizing p12 we have for q0 to q11
         * 
    (-1+i) 0,4,8 : -1*q_0 + i*q_0 -1*q_4 + i*q_4 -1*q_8 + i*q_8
    (-1-i) 1,5,9 :   -1*q_1 -1*i*q_1 -1*q_5  -1*i*q_5  -1*q_9 -1*i*q_9 
    (1-i) 2,6,10 : q_2 -1*i*q_2  -1*i*q_10 + q_6 -1*i*q_6 + q_10  
    (1+i) 3,7,11 : q_3 + i*q_3 + q_7 + i*q_7 + i*q_11 + q_11  

    Choosing a direction
    (-1 - i)v  (-1 + i)  =  (1 + 1)v
    (-1 + i)v  (-1 - i) =   (1 + 1)v
    (1 + i) v  (1 - i)  =   (1 + 1)v
    (1 - i) v (1 + i)   =   (1 + 1)v

Another example for p7

(-1 +i)   q3                       -- choice 0
(-1 - i ) q0, q4                   -- choice 1
 (1-i)    q1, q5                   -- choice 2
 (1+i)    q2, q6   (q_m-1, q_m-5)  -- choice 3
 
P7 = i*q_6 + i*p_0 + i*q_2 + i*q_3 + -1*i*q_0 + -1*i*q_1 
 -1*i*q_4 + -1*i*q_5 + -1*q_0 + q_1 + q_2 + -1*q_3 + -1*q_4 + q_5 + q_6

         */
        
        Point2D[][] ret = new Point2D[4][in.N];
        
        for(int starIdx = 0; starIdx < in.N; ++starIdx) {
            for(int dir = 0; dir < 4; ++dir) {
                Complex c = in.stars.get(starIdx).toPoint().toComplex().multiply(eqMult[dir]);
                Point2D point = new Point2D(c.getReal(), c.getImaginary());
                ret[dir][starIdx] = point;
                
                log.debug("Rot {} of {} = {}", dir, in.stars.get(starIdx).toPoint(), point);
            }            
        }
        
        return ret;
    }
    
    
    static class Event implements Comparable<Event>
    {
        double ang;
        int hullIndex;
        int hullPointIndex;
        
        Event() {}
        
        Event(Event o) {
            ang = o.ang;
            hullIndex = o.hullIndex;
            hullPointIndex = o.hullPointIndex;
        }
        @Override
        public int compareTo(Event o)
        {
            return Double.compare(ang, o.ang);
        }
    }
    
    public String handleCase(InputData in) {

        Complex c = new Complex(12, 2);
        for(int i = 0; i < 10; ++i) {
        c = c.multiply(new Complex(1, -1));
        log.debug("C {}", c);
        }
        
       // compareComplexAndPoint();
        
        //formula();
        
        Point2D[][] starDirections = buildStarVectors(in);
        
        List<List<Point>> hulls = Lists.newArrayList();
        
        /**
         * Build hulls, not of the stars, but of their 
         * contribution to the distance travelled of the "space ship".
         */
        for(int dir = 0; dir < 4; ++dir) {
            GrahamScan gs = new GrahamScan(starDirections[dir]);
            
            List<Point> hullPoints = Lists.newArrayList();
            
            for(Point2D p2d : gs.hull()) {
                hullPoints.add(new Point(p2d.x(), p2d.y()));
            }
            
            hulls.add(hullPoints);
            
            log.debug("Hull {} = {}", dir, hullPoints);
        }
        
        List<Event> events = Lists.newArrayList();
        
        /**
         * This is probably the rotating callipurs bit.
         * 
         * Go through each hull and add an "Event" that is
         * the angle of the side in question.  
         * 
         * This angle corresponds to the vector v in the solution.
         */
        for(int hullIndex = 0; hullIndex < 4; ++hullIndex)
        {
            List<Point> hull = hulls.get(hullIndex);
            for(int hullPointIndex = 0; hullPointIndex < hull.size(); ++hullPointIndex)
            {
                int prevIndex = (hullPointIndex + hull.size() - 1) % hull.size();
                
                Point cur = hull.get(hullPointIndex);
                Point prev = hull.get(prevIndex);
                
                Event e = new Event();
                if (prevIndex == hullPointIndex) {
                    e.ang = 0;
                } else {
                    e.ang = cur.translate(prev).polarAngle();
                }
                e.hullPointIndex = hullPointIndex;
                e.hullIndex = hullIndex;
                
                for(int i = -1; i <= 0; ++i) {
                    Event e2 = new Event(e);
                    e2.ang += i * 2 * Math.PI;
                    events.add(e2);
                }
            }
        }
        
        
        Collections.sort(events);
        
        Point[] rotationVecs = new Point[4];
        
        double best = 0;
        
        /**
         * As we go through the events, we are taking the 4 vectors that 
         * go in the same direction
         */
        for(Event e : events) 
        {
            rotationVecs[e.hullIndex] = hulls.get(e.hullIndex).get(e.hullPointIndex);
            
            log.debug("Setting star choice {} to {}", e.hullIndex, rotationVecs[e.hullIndex]);
                
            /**
             * It is possible that we do not use all the rotations, we check
             * down to M - 3
             */
            for (int k = Math.max(1, in.M - 3); k <= in.M; ++k) 
            {
                Point start = new Point(0,0);
                    
                boolean bad = false;
                log.debug("K is {}",k);
                
                int[] usedArr = new int[4];
                for(int i = 0; i < 4; ++i) {
                    usedArr[i] = k / 4 + (i >= 4 - k % 4  ? 1 : 0);
                    log.debug("Used {} = {}", i, usedArr[i]);
                }
                
                for (int i = 0; i < 4; ++i) {
                    int used = usedArr[i];
                    if (used > 0 && rotationVecs[i] == null) {
                        bad = true;
                        break;
                    }
                    if (used > 0) {
                        log.debug(" Used {} of best[{}] = {}",
                                 used, i, rotationVecs[i]); 
                        
                        start = start.add(rotationVecs[i].scale(used));
                    }
                }
                if (bad) {
                    continue;
                }
                double dist = start.distance(new Point(0,0));
                log.debug("Result is {}", dist);
                best = Math.max(best, dist);
                
            }
        }
        
        return String.format("Case #%d: %s ", in.testCase, DoubleFormat.df7.format(best));
        
    }
    
  

}
