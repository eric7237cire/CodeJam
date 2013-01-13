package codejam.y2008.round_final.ping_pong_balls;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //   return new String[] {"sample.in"};
       //  return new String[] { "B-small-practice.in" };
       return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        
        InputData in = new InputData(testCase);
       
        in.W = scanner.nextInt();
        in.H = scanner.nextInt();
        
        in.vec1 = new PointInt(scanner.nextInt(), scanner.nextInt());
        
        in.vec2 = new PointInt(scanner.nextInt(), scanner.nextInt());
        
        in.initial = new PointInt(scanner.nextInt(), scanner.nextInt());
        
        return in;
    }

    
        
    public String handleColinear(InputData in) {
        //all x, y 
        
        // x,y = a (vec1) + b ( vec2 )
        
        Set<PointInt> points = Sets.newHashSet();
        
        
        Queue<PointInt> toVisit = new LinkedList<>();
        
        toVisit.add(in.initial);
        
        while(!toVisit.isEmpty()) {
            
            PointInt current = toVisit.poll();
            
            if (points.contains(current))
                continue;
            
            points.add(current);
            
            PointInt withVec1 = PointInt.add(current, in.vec1);
            PointInt withVec2 = PointInt.add(current, in.vec2);
            
            if (inside(in, withVec1)) {
                toVisit.add(withVec1);
            }
            
            if (inside(in, withVec2)) {
                toVisit.add(withVec2);
            }
        }
        
        return String.format("Case #%d: %d", in.testCase, points.size());
    }
    
    boolean inside(InputData in, PointInt p) {
        return p.getX() >= 0 && p.getX() < in.W && p.getY() >= 0 && p.getY() < in.H;
    }
    
    boolean inside(InputData in, int a, int b) {
        int x = in.initial.getX() + a*in.vec1.getX() + b*in.vec2.getX();
        int y = in.initial.getY() + a*in.vec1.getY() + b*in.vec2.getY();
        if(x<0 || x>=in.W) return false;
        if(y<0 || y>=in.H) return false;
        return true;
    }

    //Define a coordinate system, a is how many jumps in vec1 direction
    // b is how many in vec 2
    public String handleCase(InputData in) {

        if (DoubleMath.fuzzyEquals(Point.crossProduct(in.vec1.toPoint(), in.vec2.toPoint()), 0, 1e-6))
            return handleColinear(in);

        int bLower = 0;
        long count = 0;
        
        //Intially b cannot be more than the greatest dimension of room
        int bUpper = 1000001;
        outer:
        for(int a = 0; a < 20000001; ++a) {
            //The bLowers are increasing.  Its like how many b jumps were needed before to remain in the room
            while(!inside(in, a, bLower)) {
                ++bLower;
                
                //if bLower is more than bUpper, we are done
                if (bLower > bUpper)
                    break outer;
            }
            
            //Now find bUpper, such that a, bUpper is in the room, but a, bUpper+1 is not
            
            if (inside(in, a, bUpper)) {
                while(inside(in, a, bUpper))
                    ++bUpper;
                
                --bUpper;
            } else {
                while(!inside(in,a, bUpper)) {
                    --bUpper;
                }
            }
            
            Preconditions.checkState(bUpper >= bLower);
            
            count += bUpper - bLower + 1;
            
        }
        
        return String.format("Case #%d: %d", in.testCase, count);        
    }

}
