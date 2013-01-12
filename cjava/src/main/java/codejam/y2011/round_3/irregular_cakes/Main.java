package codejam.y2011.round_3.irregular_cakes;


import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.geometry.Polygon;
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
       //  return new String[] {"sample.in"};
        // return new String[] { "D-large-practice.in" };
       return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {



        /*
         *    /*
    * W (the cake's width), L (the number of points on the lower boundary),
    *  U (the number of points on the upper boundary) 
    *  and G (the number of guests at the party).

This is followed by L lines specifying the lower boundary. 
The i-th line contains two integers xi and yi, representing the coordinates 
of the i-th point on the lower boundary. This is followed by U more lines 
specifying the upper boundary. The j-th line here contains two integers
 xj and yj, representing the coordinates of the j-th point on the upper boundary.
    */
        
        InputData in = new InputData(testCase);
       
        in.W = scanner.nextInt();        
        in.L = scanner.nextInt();
        in.U = scanner.nextInt();
        in.G = scanner.nextInt();

        in.lower = Lists.newArrayList();
        for(int i = 0; i < in.L; ++i) {
           
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            
            in.lower.add(new Point(x,y));
        }
        
        in.upper = Lists.newArrayList();
        for(int i = 0; i < in.U; ++i) {
           
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            
            in.upper.add(new Point(x,y));
        }

        return in;
    }

        
    
    /**
     * Given the polygon line, return a new line between xLo and xMax
     * @param xLo
     * @param xMax
     * @param points
     * @return
     */
    List<Point> getPointsWithinRange(double xLo, double xMax, List<Point> points) {
        int startIndex = 0;
        int stopIndex = 0;
        Point startPoint = null;
        Point stopPoint = null;
        
        //Find first point that is >= the xLo
        for(int pIdx = 0; pIdx < points.size(); ++pIdx) {
            Point point = points.get(pIdx);
            
            if (point.getX() < xLo ) 
                continue;
                
            if (pIdx == 0) {
                startIndex = 0;
                break;
            }
            
            Line line = new Line(points.get(pIdx - 1), points.get(pIdx));
            
            startPoint = line.getPointGivenX(xLo);
            
            Preconditions.checkState(line.isBetween(startPoint));
            
            startIndex = pIdx;
            break;
        }
        
        for(int pIdx = 0; pIdx < points.size(); ++pIdx) {
            Point point = points.get(pIdx);
            
            if (point.getX() < xMax ) 
                continue;
                
            if (pIdx == 0) {
                stopIndex = 0;
                break;
            }
            
            Line line = new Line(points.get(pIdx - 1), points.get(pIdx));
            stopPoint = line.getPointGivenX(xMax);
            
            Preconditions.checkState(line.isBetween(stopPoint));
            stopIndex = pIdx;
            break;
        }
        
        List<Point> ret = Lists.newArrayList();
        if (startPoint != null)
            ret.add(startPoint);
        
        ret.addAll(points.subList(startIndex, stopIndex+1));
                
        if (stopPoint != null)
            ret.add(stopPoint);
        
        return ret;
    }
    
    double polyArea(double xLo, double xMax, InputData in)
    {
        List<Point> upper = getPointsWithinRange(xLo, xMax, in.upper);

        List<Point> lower = getPointsWithinRange(xLo, xMax, in.lower);
        
        List<Point> combin = Lists.newArrayList();
        combin.addAll(upper);
        combin.addAll(Lists.reverse(lower));
        
        return Polygon.area(combin);
    }
    
    
    double findSlice(InputData in, double targetArea) {
        final double xLeft = in.upper.get(0).getX();
        double xLo = xLeft;
        double xHi = in.W;
        
        double tolerance = 1e-7;
        
        /**
         * Invariant area slicing @ xLo is too small, @ xHi is large enough
         */
        while( true )  
        {         
            double xMid = xLo + (xHi-xLo)  / 2;
        
            double area = polyArea(xLeft, xMid, in);
            
            if (area >= targetArea)
                xHi = xMid;
            else
                xLo = xMid;
        
            //Do the binary search on the area, not the xCoord
            if (DoubleMath.fuzzyEquals(targetArea,area,tolerance)) {
                return xMid;
            }
        }
        
        
    }
    
    public String handleCase(InputData in) {

        
        double totalArea = polyArea(0, in.W, in);
        
        double sliceArea = totalArea / in.G;
        
        /*
         * binary_search(lo, hi, p):
   while we choose not to terminate:
      mid = lo + (hi-lo)/2
      if p(mid) == true:
         hi = mid
      else:
         lo = mid
          
   return lo // lo is close to the border between no and yes
         */
        
        List<Double> slices = Lists.newArrayList();
        
        for(int slice = 1; slice < in.G; ++slice) {
            
            double sliceX = findSlice(in, sliceArea);
            
            in.upper = getPointsWithinRange(sliceX, in.W, in.upper);
            in.lower = getPointsWithinRange(sliceX, in.W, in.lower);
        
            slices.add(sliceX);            
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        for(Double slice : slices) {
            sb.append(DoubleFormat.df7.format(slice));
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

}
