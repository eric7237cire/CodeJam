package codejam.y2012.round_final.twirling_freedom;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.complex.Complex;

import codejam.utils.geometry.GrahamScan;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.Point2D;
import codejam.utils.geometry.PointInt;
import codejam.utils.geometry.draw.StdDraw;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.polynomial.AddTerms;
import codejam.utils.polynomial.CoefficientTerm;
import codejam.utils.polynomial.MultTerms;
import codejam.utils.polynomial.Polynomial;
import codejam.utils.polynomial.Term;
import codejam.utils.polynomial.VariableTerm;
import codejam.utils.utils.DoubleFormat;
import codejam.utils.utils.PermutationWithRepetition;

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

    Complex p0 = new Complex(0, 0);
    
    Complex choice0 = new Complex(3, -3);
    Complex choice1 = new Complex(-4, -3);
    Complex choice2 = new Complex(-3, 5);
    Complex choice3 = new Complex(6, 3);

    //Choosing points in counter clockwise order
    List<Complex> qList = Arrays.asList(
            choice0,
            choice1,
            choice2,
            choice3,
            choice0,
            choice1,
            choice2,
            choice3,
            choice0,
            choice1,
            choice2,
            choice3
            
            );
    
    public void compareComplexAndPoint() {
        Complex c = new Complex(4, 2);
        
        Complex i = new Complex(0, 1);
        Complex iNeg = new Complex(0, -1);
        
        Point p = new Point(c.getReal(), c.getImaginary());
        
        log.debug("Complex {}, p {}", c.multiply(iNeg), p.rotate(-Math.PI / 2));
        
        
        c = new Complex(5, 4);        
        Complex q = new Complex(14,7);
        
         p = new Point(c.getReal(), c.getImaginary());
         Point qPoint = new Point(q.getReal(), q.getImaginary());
        
        
        //Complex p1 = iNeg.multiply(p0).add(i.multiply(q)).add(q);
        Complex p1 = iNeg.multiply(p0).add(q.multiply(new Complex(1, 1)));
        
        
        log.debug("Complex {} ; {}, p {}", 
                c.subtract(q).multiply(iNeg).add(q),
                p1,
                p.translate(qPoint).rotate(-Math.PI / 2).translate( qPoint.scale(-1)));
        
        
       
        
        List<Complex> pList = Lists.newArrayList();
        pList.add(p0);
        for(int qNum = 0; qNum < qList.size(); ++qNum)
        {
            Complex pLast = pList.get(pList.size() - 1);
            Complex qCur = qList.get(qNum);
            
            Complex pNext = iNeg.multiply(pLast).add(qCur.multiply(new Complex(1, 1)));
            
            pList.add(pNext);
            
            log.debug("q{}={} p{}={}", qNum, qCur, qNum+1, pNext);
        }
        
        //Complex p2  = iNeg.multiply(p1).add(q1.multiply(new Complex(1, 1)));
        
        //Complex p2_alt = p0.multiply(-1).add(q0).add(q1.multiply(i)).subtract(q0.multiply(i)).add(q1);
        
        
        
    }
    
    /**
     * Organizing p12 we have
     * 
(-1+i) 0,4,8,12 : -1*q_0 + i*q_0 -1*q_4 + i*q_4 -1*q_8 + i*q_8
(-1-i) 1,5,9 :   -1*q_1 -1*i*q_1 -1*q_5  -1*i*q_5  -1*q_9 -1*i*q_9 
(1-i) 2,6,10 : q_2 -1*i*q_2  -1*i*q_10 + q_6 -1*i*q_6 + q_10  
(1+i) 3,7,11 : q_3 + i*q_3 + q_7 + i*q_7 + i*q_11 + q_11  

Choosing a direction
(-1 - i)v  (-1 + i)  =  (1 + 1)v
(-1 + i)v  (-1 - i) =   (1 + 1)v
(1 + i) v  (1 - i)  =   (1 + 1)v
(1 - i) v (1 + i)   =   (1 + 1)v

     */
    public void formula() {
        CoefficientTerm co = new CoefficientTerm(-1);
        Term m1 = MultTerms.buildMultTerm(co, new VariableTerm("i"),new VariableTerm("p"));
        Term m2 = MultTerms.buildMultTerm(new VariableTerm("q"),
                new AddTerms(new VariableTerm("i"), new CoefficientTerm(1)));
        
        Term add = new AddTerms(m1,m2);
        Polynomial p = new Polynomial();
        p.setAddTerms(add);
        //p.getAddTerms().add(m2);
        p.doSimplify();
        
        Polynomial p1 = new Polynomial(p);
        p1.substitute(new VariableTerm("q"),new VariableTerm("q_0"));
        p1.substitute(new VariableTerm("p"),new VariableTerm("p_0"));
        
        List<Polynomial> polys = Lists.newArrayList();
        polys.add(p1);
        
        
        
        for(int pNum = 2; pNum <= 12; ++pNum) {
            Polynomial px = new Polynomial(p);
            px.substitute(new VariableTerm("q"),new VariableTerm("q_" + (pNum-1)));
            px.substitute(new VariableTerm("p"), polys.get(polys.size()-1).getAddTerms());
            px.doSimplify();
            polys.add(px);
        }
        
        for(int pNum = 1; pNum <= 12; ++pNum) {
            log.debug("P{} = {}", pNum, polys.get(pNum-1).toString());
        }
        
        
        for(int pNum = 0; pNum < 12; ++pNum) {
            Polynomial poly = polys.get(pNum);
            
            poly.substitute(new VariableTerm("p_0"), complexToTerm(p0));
            for(int q = 0; q < qList.size(); ++q) {
                poly.substitute(new VariableTerm("q_" + q), complexToTerm(qList.get(q)));
            }
            poly.doSimplify();
            
            log.debug("P{} = {}", pNum+1, poly.toString());
        }
        
        
    }
    
    public Term complexToTerm(Complex c) {
        return new AddTerms(new CoefficientTerm((int)c.getReal()),
                MultTerms.buildMultTerm(new VariableTerm("i"), 
                        new CoefficientTerm((int)c.getImaginary())));
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
     * Given the multipliers and a desired direction vector v
     * it is how to maximize the travel in direction v.
     * 
    Choosing a direction
    (-1 - i)v  (-1 + i)  =  (1 + 1)v
    (-1 + i)v  (-1 - i) =   (1 + 1)v
    (1 + i) v  (1 - i)  =   (1 + 1)v
    (1 - i) v (1 + i)   =   (1 + 1)v
     */
    Complex[] desiredDirection = new Complex[] {
            new Complex(-1, -1),
            new Complex(-1, 1),
            new Complex(1, 1),
            new Complex(1, -1)
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
    
    public void testStuff(InputData in) {
        
        Point2D[] points = new Point2D[in.N];
        
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        
        for(int s = 0; s < in.N; ++s) {
            points[s] = new Point2D(in.stars.get(s).getX(),in.stars.get(s).getY());
            
            minX = Math.min(minX, in.stars.get(s).getX());
            minY = Math.min(minY, in.stars.get(s).getY());
            maxY = Math.max(maxY, in.stars.get(s).getY());
            maxX = Math.max(maxX, in.stars.get(s).getX());
        }
        
        int fac = 3;
        int dim = 30;
        minX = -dim;
        maxX = dim;
        minY = -dim;
        maxY = dim;
        
        boolean draw = false;
        
        if (draw) {
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(minX, maxX);
        StdDraw.setYscale(minY, maxY);
        StdDraw.line(minX, 0, maxX, 0);
        StdDraw.line(0, minY, 0, maxY);
        }
        
        for(int s = 0; s < in.N; ++s) {
            points[s] = new Point2D(in.stars.get(s).getX(),in.stars.get(s).getY());
         
            if (draw) {
            points[s].draw();
            StdDraw.text(points[s].x(), points[s].y(), "" + s);
            //StdDraw.text(points[s].x(), points[s].y(), "Point " + s + in.stars.get(s).toPoint().toString());
            }
            
            Point q = in.stars.get(s).toPoint();
            
            
            
        }
        
        Point p0 = new Point(0,0);
        
        PointInt[] choices = new PointInt[4];
        choices[0] = in.stars.get(1);
        choices[1] = in.stars.get(0);
        choices[2] = in.stars.get(0);
        choices[3] = in.stars.get(2);
        
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
        
        Point pPrev = p0;
        int M = in.M;
        
        int[] used = new int[4];
        for(int i = 0; i < 4; ++i) {
            used[i] = M / 4 + (i >= 4 - M % 4  ? 1 : 0);
            log.debug("Used {} = {}", i, used[i]);
        }
        
        
        Integer[] inPerm = new Integer[] {0,1,2,3};
        Integer[] outPerm = new Integer[4]; 
        PermutationWithRepetition<Integer> pr = PermutationWithRepetition.<Integer>create(inPerm,outPerm);
        while(pr.hasNext()) {
            outPerm = pr.next();
            log.debug("Perm {}", (Object)outPerm);
            
            Complex[] q = new Complex[4];
            for(int i = 0; i < 4; ++i) {
                q[i] = new Complex(in.stars.get(outPerm[i]).getX(),in.stars.get(outPerm[i]).getY());
            }
            
            Complex c1 = new Complex(-1, 1).multiply(q[0].multiply(used[0])); 
            Complex c2 = new Complex(-1, -1).multiply(q[1].multiply(used[1]));
            Complex c3 = new Complex(1, -1).multiply(q[2].multiply(used[2]));
            Complex c4 = new Complex(1, 1).multiply(q[3].multiply(used[3]));
            
        //    Complex pointC = c1.add(c2.multiply(used[1])).add(c3.multiply(used[2])).add(c4.multiply(used[3]));
            Complex pointC = c1.add(c2).add(c3).add(c4);
            log.debug("Calculated final point {} dist {}", pointC,
                    new Point(pointC.getReal(), pointC.getImaginary()).distance(new Point(0,0)));
        }
        
        Complex c1 = new Complex(-1, 1).multiply(new Complex(choices[0].getX(),choices[0].getY()).multiply(used[0])); 
        Complex c2 = new Complex(-1, -1).multiply(new Complex(choices[1].getX(),choices[1].getY()).multiply(used[1]));
        Complex c3 = new Complex(1, -1).multiply(new Complex(choices[2].getX(),choices[2].getY()).multiply(used[2]));
        Complex c4 = new Complex(1, 1).multiply(new Complex(choices[3].getX(),choices[3].getY()).multiply(used[3]));
        
    //    Complex pointC = c1.add(c2.multiply(used[1])).add(c3.multiply(used[2])).add(c4.multiply(used[3]));
        Complex pointC = c1.add(c2).add(c3).add(c4);
        log.debug("Calculated final point {}", pointC);
        
        for(int m = 0; m < M; ++m) {
            
            //So that m-1 corresponds to 3 ; m-2 to 2 ; m -3 to 1 etc
            int offset = 3 - ((M-1) % 4);
            
            int choice = (m+offset) % 4;
            log.debug("offset {} choice {}", offset, choice);
            
            Point q = choices[choice].toPoint();
            Point newPoint = pPrev.rotateAbout(q, -Math.PI / 2);
            
            
            
            log.debug("Dist {} new point {}", newPoint.distance(new Point(0,0)), newPoint);
            
            //double ang1 = new Point(0,0).translate(q).polarAngle();
            double ang1 = newPoint.translate(q).polarAngle();
            double ang2 = ang1 + Math.PI / 2;
            
            if (draw) {
            StdDraw.text(newPoint.getX(), newPoint.getY(), "NewPoint " + m + "ch" + choice + " "+ q.toString() );
            StdDraw.arc(q.getX(), q.getY(), 
                    q.distance(pPrev), 
                    ang1 * 180 /Math.PI, 
                    ang2 * 180 /Math.PI );
            }
            pPrev = newPoint;
            
        }
        
        //StdDraw.show(0);

        if (in != null) 
            throw new RuntimeException("ha");
        
        //GrahamScan gs = new GrahamScan(points);
        
        Point2D[] best = new Point2D[4];
        //gs.hull()
    }

}
