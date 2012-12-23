package codejam.y2012.round_qual.hall_of_mirrors;

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
    Point[] getIntersectionPoints(Point C, Point S, double target, boolean isTargetY, int numTriangles) {
        
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
        
//        Line S_T = new Line(S, T);
//        Line C_T = new Line(C, T);
//        Preconditions.checkState(Line.dc.compare(S_T.getM(), - C_T.getM()) == 0);
        return new Point[] {T, TCorner};
    }
    
    @Override
    public String handleCase(InputData in) {
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
            if (intersection.distance(self) <= in.D) {
                ++count;
            }
        }
        
        for(Point corner : corners) {
            if (self.distance(corner) <= in.D) 
                ++count;
            
            
            for(int triangles = 1; triangles <= 50; ++triangles) {
                
                for(Line wall : walls) {
                    
                    if (wall.onLine(corner))
                        continue;
                    
                    Point[] intP = null;
                    if (wall.getType() == Line.Type.HORIZONTAL) 
                        intP = getIntersectionPoints(corner,self,wall.getPointGivenX(1).getY(), true, triangles);
                    else
                        if (wall.getType() == Line.Type.VERTICAL) 
                            intP = getIntersectionPoints(corner,self,wall.getPointGivenY(1).getX(), false, triangles);
                    
                    
                    double distance = (2 * triangles - 1) * intP[1].distance(corner) + intP[0].distance(self);
                    
                    if (distance <= in.D)
                        ++count;
                    else
                        break;
                }
            }
            
        }
        
        return String.format("Case #%d: %d", in.testCase, count);
        
    }
    
    public String handleCase2(InputData in) {
     
       //.5 .5 
        Point S = new Point(.5,.5);
        
        int numTriangles = 4;
        //Bounce of west wall
        //Point iP = getIntersectionPoint(new Point(1,1), S, 0, false, numTriangles);
        
        
        //bounce off North wall
        //Point iP = getIntersectionPoint(new Point(1,-1), S, 1, true);
        Point iP = getIntersectionPoints(new Point(1,-1), S, 1, true, numTriangles)[0];
        
        Line vec = new Line(new Point(.5,.5), iP);
        
        Line[] walls = new Line[4];
        
        //north
        walls[0] = new Line(new Point(0,1), new Point(1,1));
        
        //east
        walls[1] = new Line(new Point(1,1), new Point(1,-1));
        
        //south
        walls[2] = new Line(new Point(1,-1), new Point(0,-1));
        
        //west
        walls[3] = new Line(new Point(0,-1), new Point(0,1));
        
        Point from = S;
        double fromAngle = Math.atan2(iP.getY() - from.getY(), iP.getX() - from.getX());
        fromAngle = Angle.makeAnglePositive(fromAngle);
        
        DoubleComparator dc = new DoubleComparator(0.0001);
        
        Point firstIntersection = null;
        
        for (int j = 0; j < 2*numTriangles; ++j) {
            
            int foundWall = -1;
            
            for(int i = 0; i < 4; ++i) {
                
                Point intersection = walls[i].getIntersection(vec);
                if (intersection == null)
                    continue;
                double angleIntersection =  Math.atan2(intersection.getY() - from.getY(), intersection.getX() - from.getX());
                angleIntersection = Angle.makeAnglePositive(angleIntersection);
                
                //other side of line
                double angleIntersection2 = angleIntersection + Math.PI;
                angleIntersection2 = Angle.makeAnglePositive(angleIntersection2);
                
                if (dc.compare(angleIntersection, fromAngle) != 0 && dc.compare(angleIntersection2, fromAngle) != 0) 
                    continue;
                
                if (walls[i].isBetween(intersection)) {
                    log.debug("Intersection found with wall {} = {}.  Angle is {}", i, intersection, angleIntersection * 180d / Math.PI);
                    
                    if (firstIntersection == null) {
                        firstIntersection = intersection;
                    } else if (Math.abs(intersection.getY() - firstIntersection.getY()) < .001 &&
                            Math.abs(intersection.getX() - firstIntersection.getX()) < .001){
                        log.debug("Close");
                    }
                    
                    if (vec.onLine(new Point(0.5, 0.5))) {
                        log.debug("Exact");
                    }
                    foundWall = i;
                    from = intersection;
                    fromAngle = -fromAngle;
                    fromAngle = Angle.makeAnglePositive(fromAngle);
                    
                    //log.debug("New angle {}", fromAngle * 180 / Math.PI);
                    
                    double y = Math.sin(fromAngle) + from.getY();
                    double x = Math.cos(fromAngle) + from.getX();
                    
                    vec = new Line(from, new Point(x,y));
                    
                    break;
                }
                
                
            }
            
            
           Preconditions.checkState(foundWall >= 0 );
            
        
        }
        
        

        return String.format("Case #%d: NO", in.testCase);
        
    }

}
