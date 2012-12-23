package codejam.y2012.round_qual.hall_of_mirrors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Angle;
import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleComparator;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
       return new String[] {"sample.in"};
        //return new String[] {"C-small-practice.in", "C-large-practice.in"};
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        in.H = scanner.nextInt();
        in.W = scanner.nextInt();
        in.D  = scanner.nextInt();
        in.grid  = GridChar.buildFromScannerYZeroBottom(scanner,in.H,in.W, ' ');
        
        return in;
    }
    
    /**
     * 
     * @param C corner
     * @param S self -- you
     * @param targetX
     * @return
     */
    Point getIntersectionPoint(Point C, Point S, double target, boolean isTargetY) {
    /*
     * slope S to T == -slope C to T
     * (Ty - Sy) / (Tx - Sx) = (-Cy + Ty) / (Cx - Tx)  
     * CxTy -TxTy - CxSy + TxSy = -CyTx + CySx +TyTx - TySx
     *  
     *  solving for Ty if Tx is known
     * CxTy -TxTy -TyTx +TySx = -CyTx + CySx + CxSy - TxSy
     * Ty =  (-CyTx + CySx + CxSy - TxSy) / (Cx - Tx - Tx + Sx)
     * 
     * solving for Tx if Ty is known
     * -TxTy + TxSy + CyTx - TyTx = -CxTy +cxSy + CySx - TySx
     * Tx (-Ty + Sy + Cy - Ty ) = -CxTy +cxSy + CySx - TySx
     * Tx = (-CxTy +cxSy + CySx - TySx) / (-Ty + Sy + Cy - Ty ) 
     */
    
    double Cx = C.getX();
    double Cy = C.getY();
    double Sx = S.getX();
    double Sy = S.getY();
    
    Point T = null;
    if (isTargetY) {
        double Ty = target;
        double Tx = (-Cx*Ty +Cx*Sy + Cy*Sx - Ty*Sx) / (-Ty + Sy + Cy - Ty );
        T = new Point(Tx,Ty);
    } else {
        double Tx = target;
        double Ty = (-Cy*Tx + Cy*Sx + Cx*Sy - Tx*Sy) / (Cx - Tx - Tx + Sx);
        T  = new Point(Tx,Ty);
    }
    
    //Checks
    Line S_T = new Line(S, T);
    Line C_T = new Line(C, T);
    Preconditions.checkState(Line.dc.compare(S_T.getM(), - C_T.getM()) == 0);
    return T;
}
    
    /**
     * First is with the wall, next is from corner
     * @param C
     * @param S
     * @param target
     * @param isTargetY
     * @param numTriangles
     * @return
     */
    Point[] getIntersectionPoints(Point C, Point S, Line targetWall, int numTriangles) {
        
        double target;
        boolean isTargetY;
        
        //Assume there is a wall parralel 
        if (targetWall.getType() == Line.Type.HORIZONTAL) {
            isTargetY = true;
            target = targetWall.getPointGivenX(1).getY();
        } else {
            isTargetY = false;
            target  = targetWall.getPointGivenY(1).getX();
        }
            
        //Triangle between S and T has sides a and b
        //Triangle between C and T has sides c and d
        
        /*
         * c / a == d / b
         * 
         * We also know that 
         * 
         * delta = 2(k-1)d + d + b == 2kd -2d + d + b = 2kd - d + b
         * 
         * d = (delta - b) / (2k - 1)
         * 
         * substituting
         * 
         * c / a == (delta - b) / (2k - 1) / b
         * cb = a(delta - b) / (2k - 1)
         * cb (2k-1) = a*delta - ab
         * 2kcb - cb = a*delta - ab
         * 2kcb -cb + ab = a*delta
         * b = a*delta / (2kc - c + a) 
         */
        double a = isTargetY ? target - S.getY() : target - S.getX();
        double c = isTargetY ? target - C.getY() : target - C.getX();
        
        a = Math.abs(a);
        c = Math.abs(c);
        
        double delta = isTargetY ? C.getX() - S.getX() : C.getY() - S.getY();
        
        delta = Math.abs(delta);
        
        double b = a * delta / (2*numTriangles * c - c + a);        
        double d = (delta - b) / (2 * numTriangles - 1);
        
        Point T = null;
        Point TCorner = null;
        
        if (isTargetY) {
            if (C.getX() > S.getX()) {
                T = new Point(S.getX() + b, target);
                TCorner = new Point(C.getX() - d, target);
            } else {
                T = new Point(S.getX() - b, target);
                TCorner = new Point(C.getX() + d, target);
            }
        } else {
            if (C.getY() > S.getY()) {
                T = new Point(target, S.getY() + b);
                TCorner = new Point(target, C.getY() - d);
            } else {
                T = new Point(target, S.getY() - b);
                TCorner = new Point(target, C.getY() + d);
            }
        }
        
        Point[] ret = new Point[4];
        ret[0] = T;
        ret[1] = TCorner;
        //Now calculate if we hit the corner wall first
        
        a = isTargetY ? S.getY() - C.getY() : S.getX() - C.getX();
        
        //c is the same
        //double c = isTargetY ? target - C.getY() : target - C.getX();
        
        a = Math.abs(a);
        c = Math.abs(c);
        
        b = a * delta / (2*numTriangles * c + a);        
        d = (delta - b) / (2 * numTriangles);
        
        T = null;
        TCorner = null;
        
        if (isTargetY) {
            if (C.getX() > S.getX()) {
                T = new Point(S.getX() + b, C.getY());
                TCorner = new Point(C.getX() - d, target);
            } else {
                T = new Point(S.getX() - b, C.getY());
                TCorner = new Point(C.getX() + d, target);
            }
        } else {
            if (C.getY() > S.getY()) {
                T = new Point(C.getX(), S.getY() + b);
                TCorner = new Point(target, C.getY() - d);
            } else {
                T = new Point(C.getX(), S.getY() - b);
                TCorner = new Point(target, C.getY() + d);
            }
        }
//      
        ret[2] = T;
        ret[3] = TCorner;
        
        return ret;
    }
    
    
    public String handleCase(InputData in) {
        
        if (1==1)
            return handleCase22(in);
        
        List<Point> corners = Lists.newArrayList();
        
        //SW
        corners.add(new Point(1,1));
        //SE
        corners.add(new Point(in.W - 1,1));
        //NE
        corners.add(new Point(in.W - 1,in.H - 1));
        //NW
        corners.add(new Point(1,in.H - 1));
        
        int idx = in.grid.getIndexesOf('X').iterator().next();
        int[] rowCol = in.grid.getRowCol(idx);
        
        Point self = new Point(rowCol[1] + .5 , rowCol[0] + .5);
        
        List<Line> walls = Lists.newArrayList();
        for(int corner = 0; corner < 4; ++corner) {
            int nextCorner = corner + 1 == 4 ? 0 : corner + 1;
            walls.add(new Line(corners.get(corner), corners.get(nextCorner)));
        }
        
        //Hit walls and corners directly
        
        int count = 0;
        
        for(Line wall : walls) {
            Line perp = wall.getLinePerpendicular(self);
            Point intersection = perp.getIntersection(wall);
            if (2*intersection.distance(self) <= in.D) {
                ++count;
            }
        }
        
        for(Point corner : corners) {
            if (2*self.distance(corner) <= in.D) 
                ++count;
            
            triangleLoop:
            for(int triangles = 1; triangles <= 50; ++triangles) {
                
                for(Line wall : walls) {
                    
                    if (wall.onLine(corner))
                        continue;
                    
                    Point[] intP = getIntersectionPoints(corner,self,wall, triangles);
                                
                    boolean one = false;
                    
                    double distance = 2* ( (2 * triangles - 1) * intP[1].distance(corner) + intP[0].distance(self));
                    
                    log.debug("Hitting (opp. wall first) corner {} from wall {} triangles {}  distance {}", corner,wall,triangles, distance);
                    if (distance <= in.D) {
                        ++count;
                        one = true;
                    }
                    
                    distance = 2 * ((2 * triangles ) * intP[3].distance(corner) + intP[2].distance(self));
                    
                    log.debug("Hitting (corner wall first) corner {} from wall {} triangles {}  distance {}", corner,wall,triangles, distance);
                    if (distance <= in.D) {
                        ++count;
                        one = true;
                    }
                    
                    if (!one)
                        break triangleLoop;
                }
                
                
            }
            
        }
        
        return String.format("Case #%d: %d", in.testCase, count);
        
    }
    
    public String handleCase22(InputData in) {
     
      
        
        int numTriangles = 1;
        //Bounce of west wall
        //Point iP = getIntersectionPoint(new Point(1,1), S, 0, false, numTriangles);
        
        
        //bounce off North wall
        //Point iP = getIntersectionPoint(new Point(1,-1), S, 1, true);
        
        List<Point> corners = Lists.newArrayList();
        
        //SW
        corners.add(new Point(1,1));
        //SE
        corners.add(new Point(in.W - 1,1));
        //NE
        corners.add(new Point(in.W - 1,in.H - 1));
        //NW
        corners.add(new Point(1,in.H - 1));
        
        int idx = in.grid.getIndexesOf('X').iterator().next();
        int[] rowCol = in.grid.getRowCol(idx);
        
        Point self = new Point(rowCol[1] + .5 , rowCol[0] + .5);
        
        List<Line> walls = Lists.newArrayList();
        for(int corner = 0; corner < 4; ++corner) {
            int nextCorner = corner + 1 == 4 ? 0 : corner + 1;
            walls.add(new Line(corners.get(corner), corners.get(nextCorner)));
        }
        
        //wall [0]  SW SE  -- south
        //wall [1]  SE NE  -- east
        //wall [2]  NE NW  -- north
        //wall [3]  NW SW  -- west
        
        Point[] iPs = getIntersectionPoints( corners.get(1), self, walls.get(2), numTriangles);
        
        
        Point iP = iPs[2];
        Line vec = new Line(self, iP);
        
        List<Line> wallsToConsider = Arrays.asList(walls.get(0), walls.get(2));
        
        //List<Line> wallsToConsider = Arrays.asList(walls.get(2), walls.get(0));
        
        Point from = self;
        double fromAngle = Math.atan2(iP.getY() - from.getY(), iP.getX() - from.getX());
        fromAngle = Angle.makeAnglePositive(fromAngle);
        
        DoubleComparator dc = new DoubleComparator(0.0001);
        
        Point firstIntersection = null;
        
        for (int j = 0; j <= 2 * numTriangles + 1; ++j) {

            boolean foundWall = false;
            Line wall = wallsToConsider.get(j % 2);

            Point intersection = wall.getIntersection(vec);
            if (intersection == null)
                continue;
            double angleIntersection = Math.atan2(intersection.getY() - from.getY(), intersection.getX() - from.getX());
            angleIntersection = Angle.makeAnglePositive(angleIntersection);

            // other side of line
            double angleIntersection2 = angleIntersection + Math.PI;
            angleIntersection2 = Angle.makeAnglePositive(angleIntersection2);

            if (dc.compare(angleIntersection, fromAngle) != 0 && dc.compare(angleIntersection2, fromAngle) != 0)
                continue;

            if (wall.isBetween(intersection)) {
                log.debug("Intersection found with wall {} = {}.  Angle is {}", wall, intersection, angleIntersection * 180d / Math.PI);

                if (firstIntersection == null) {
                    firstIntersection = intersection;
                } else if (Math.abs(intersection.getY() - firstIntersection.getY()) < .001 && Math.abs(intersection.getX() - firstIntersection.getX()) < .001) {
                    log.debug("Close");
                }

                foundWall = true;
                from = intersection;
                fromAngle = -fromAngle;
                fromAngle = Angle.makeAnglePositive(fromAngle);

                // log.debug("New angle {}", fromAngle * 180 / Math.PI);

                double y = Math.sin(fromAngle) + from.getY();
                double x = Math.cos(fromAngle) + from.getX();

                vec = new Line(from, new Point(x, y));

            }

        }
        
        

        return String.format("Case #%d: NO", in.testCase);
        
    }

}
