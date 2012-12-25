package codejam.y2012.round_qual.hall_of_mirrors;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.fraction.Fraction;
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
import codejam.utils.utils.DoubleComparator;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.text.DecimalFormatSymbols;
import java.util.*;
import codejam.utils.utils.*;
import codejam.utils.datastructures.*;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
       return new String[] {"sample.in"};
       //return new String[] {"D-small-practice.in"};
       //return new String[] {"D-large-practice.in"};
        //return new String[] {"C-small-practice.in", "C-large-practice.in"};
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        in.H = scanner.nextInt();
        in.W = scanner.nextInt();
        in.D  = scanner.nextInt();
        in.grid  = GridChar.buildFromScannerYZeroBottom(scanner,in.H,in.W, '#');
        
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
    public Point[] getIntersectionPoints(Point S, Line targetWall, Line sideWall1, Line sideWall2, int numTriangles) {
        
        double target;
        boolean isTargetY;
        
        double side1 ;
        double side2;
        
        //Assume there is a wall parralel 
        if (targetWall.getType() == Line.Type.HORIZONTAL) {
            isTargetY = true;
            target = targetWall.getPointGivenX(1).getY();
            side1 = sideWall1.getPointGivenY(1).getX();
            side2 = sideWall2.getPointGivenY(1).getX();
        } else {
            isTargetY = false;
            target  = targetWall.getPointGivenY(1).getX();
            
            side1 = sideWall1.getPointGivenX(1).getY();
            side2 = sideWall2.getPointGivenX(1).getY();
        }
            
        //Using graph.png
        
        /*
         * c / d  == a / b
         * cb = da
         * 
         * We also know that 
         * 
         * delta = k(b+d)
         * 
         * delta = kb + kd
         * d = (delta - kb) / k
         * 
         * substituting
         * 
         * cb = a(delta - kb) / k
         * kcb = a*delta - akb
         * kcb + akb = a*delta
         * b = a*delta / (kc + ak)
         *  
         */
        double a = isTargetY ? side1 - S.getX() : side1 - S.getY();
        double c = isTargetY ? side2 - S.getX() : side2 - S.getY();
        
        a = Math.abs(a);
        c = Math.abs(c);
        
        double delta = isTargetY ? target - S.getY() : target  - S.getX();
        
        delta = Math.abs(delta);
        
        double b = a * delta / (numTriangles * c + a*numTriangles);        
        double d = (delta - numTriangles* b) / numTriangles;
        
        Point tSide1 = null;
        Point tSide2 = null;
        
        if (isTargetY) {
            if (target > S.getY()) {
                tSide1 = new Point(side1, S.getY() + b);
                tSide2 = new Point(side2, S.getY() + d);
            } else {
                tSide1 = new Point(side1, S.getY() - b);
                tSide2 = new Point(side2, S.getY() - d);
            }
        } else {
            if (target > S.getX()) {
                tSide1 = new Point(S.getX() + b, side1);
                tSide2 = new Point(S.getX() + d, side2);
            } else {
                tSide1 = new Point(S.getX() - b, side1);
                tSide2 = new Point(S.getX() - d, side2);
            }
        }
        
        return new Point[] {tSide1, tSide2};
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
    public Point[] getIntersectionPointsCorner(Point C, Point S, Line targetWall, int numTriangles) {
        
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
         * a / b == c / d
         * da == cb
         * d/b ==c /a 
         * 
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
        
        Line l = new Line(ret[0], S);
        Fraction f = new Fraction(l.getM());
        
        return ret;
    }
    
    
    private static class Corner {
        public Point location;
        
        //Within 45 degrees, in other words same component sines get absorbed
        Point absorbVector;
        public String toString() {
            return location.toString() + " absorb " + absorbVector;
        }
        
        Corner(double x, double y) {
            location = new Point(x, y);
            absorbVector = null;
        }
        
        Corner(double x, double y, int aX, int aY) {
            this(x, y);
            absorbVector = new Point(aX, aY);
        }
        
    }
    
    public static class LineObj {
           public Line line;
           
           public Direction orientation;
           
           LineObj(Line line, Direction dir) {
                this.line=line;
                orientation=dir;
           }
           
           public String toString() {
                return line.toString() + " orientation " + orientation;   
           }
    }
    
    CornerCase matchesCorner(List<Corner> corners, Point point, Point direction) 
    {
        for(Corner corner : corners) {
            if (!corner.location.equals(point))
                continue;
            
            if (corner.absorbVector == null)
                return CornerCase.REFLECT;
            
            int cmpY = Double.compare(0, direction.getY());
            int cmpY2 = Double.compare(0, corner.absorbVector.getY());
            
            int cmpX = Double.compare(0, direction.getX());
            int cmpX2 = Double.compare(0, corner.absorbVector.getX());
            
            if (cmpY == cmpY2 && cmpX == cmpX2) 
                return CornerCase.ABSORB;
            else
                return CornerCase.PASSTHRU;
        }
        
        return CornerCase.NOTHING;
    }
    
    private enum CornerCase {
        NOTHING,
        PASSTHRU,
        ABSORB,
        REFLECT
    };
    
    
    public void parseWalls(GridChar grid, List<LineObj> walls) {
        
        int x = grid.getIndexesOf('X').iterator().next();
        grid.setEntry(x, '.');
        
        /*
         * east wall
         * ##
         * #.
         * #.
         * #.
         * ##
         * 
         * r,c == 1,1 to (stops at) 4,1
         * Line  1,1 to 4, 1
         * #######
         *  ###.
         *  .##.
         *  .###
         *  #.#.#
         *  #.#..
         *  ######
         *  
         *  ###
         *  ..#
         *  .##
         *  ..#
         *  ###  
         *  west wall
         */
        Integer startRowEastWall = null;
        Integer startRowWestWall = null;
        
        for(int c = 0; c < grid.getCols(); ++c) {
            for(int r = 0; r < grid.getRows(); ++r) {
              
                //Pattern #.
                if (startRowEastWall == null && grid.getEntry(r,c) == '#' &&
                        grid.getEntry(r,c,Direction.EAST) == '.') {
                    startRowEastWall = r;
                } else 
                
                if (startRowEastWall != null && 
                        ((grid.getEntry(r,c,Direction.EAST) != '.') ||
                        (grid.getEntry(r,c) != '#')))
                        {
                    walls.add(new LineObj(new Line(
                            new Point(c+1,startRowEastWall),
                            new Point(c+1, r)),
                            Direction.EAST));
                    startRowEastWall = null;
                }
                
                if (startRowWestWall == null && grid.getEntry(r,c) == '#' &&
                        grid.getEntry(r,c,Direction.WEST) == '.') {
                    startRowWestWall = r;
                }
                else
                if (startRowWestWall != null && ((
                        grid.getEntry(r,c,Direction.WEST) != '.') ||
                        (grid.getEntry(r,c) != '#')))
                {
                    walls.add(new LineObj(new Line(
                            new Point(c,startRowWestWall),
                            new Point(c, r)),
                            Direction.WEST));
                    startRowWestWall = null;
                }
            }
        }
        
        Integer startColNorthWall = null;
        Integer startColSouthWall = null;
                
        for (int r = 0; r < grid.getRows(); ++r) {
            for (int c = 0; c < grid.getCols(); ++c) {

                if (startColNorthWall == null &&
                        grid.getEntry(r, c) == '#' && 
                        grid.getEntry(r, c, Direction.NORTH) == '.') {
                    startColNorthWall = c;
                } else if (startColNorthWall != null &&
                        ((grid.getEntry(r, c, Direction.NORTH) != '.') 
                                || (grid.getEntry(r, c) != '#'))) {
                    walls.add(new LineObj(new Line(new Point(startColNorthWall,r+1), new Point(c, r+1)), Direction.NORTH));
                    startColNorthWall = null;
                }

                if (startColSouthWall == null &&
                        grid.getEntry(r, c) == '#' &&
                        grid.getEntry(r, c, Direction.SOUTH) == '.') {
                    startColSouthWall = c;
                } else if (startColSouthWall != null &&
                        ((grid.getEntry(r, c, Direction.SOUTH) != '.') ||
                                (grid.getEntry(r, c) != '#'))) {
                    walls.add(new LineObj(new Line(new Point(startColSouthWall, r), new Point(c, r)), Direction.SOUTH));
                    startColSouthWall = null;
                }
            }
        }
        
        grid.setEntry(x, 'X');
    }
    

    public void parseGrid(InputData in, List<Corner> corners, List<LineObj> walls) 
    {
        GraphAdjList graph = new GraphAdjList(in.grid.getRows() * in.grid.getCols());
        
        for(int index = 0; index < graph.getMaxNodes(); ++index) {
            if (in.grid.getEntry(index) != '#') 
                continue;
            
            graph.addConnection(index, index);
            
            Integer eastIdx = in.grid.getIndex(index, Direction.EAST);
            
            if (eastIdx != null && in.grid.getEntry(eastIdx) == '#') {
                graph.addConnection(index, eastIdx);
            }
            
            Integer southIdx = in.grid.getIndex(index, Direction.SOUTH);
            
            if (southIdx != null && in.grid.getEntry(southIdx) == '#') {
                graph.addConnection(index, southIdx);   
            }
        }
        
        List<List<Integer>> cc = graph.getConnectedComponents();
        
        for(List<Integer> gridIndexes : cc) {
            List<Corner> subCorners = Lists.newArrayList();
            
            parseCorners(in, subCorners, gridIndexes);
            
            corners.addAll(subCorners);
            
            
        
        }
        
        parseWalls(in.grid, walls);
        
    }
    
        
    public void parseCorners(InputData in, List<Corner> corners, List<Integer> gridIndexes) {
        for(int gi : gridIndexes) {
            int[] rc = in.grid.getRowCol(gi);
            int r = rc[0];
            int c = rc[1];
                            
            //Not a wall, so no corners
            if (in.grid.getEntry(r,c) != '#') {
                continue;
            }
            
            for(int d = 0; d < 4; ++d) {
                //get corner direction
                Direction dir = Direction.NORTH_EAST.turn(2 * d);
                
                if(in.grid.getEntry(r,c,dir) == '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) == '#' &&
                in.grid.getEntry(r,c,dir.turn(-1)) != '#') {
                    //a reglular corner, but will be handled by the other class
                    continue;
                } else if(in.grid.getEntry(r,c,dir) == '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) != '#' &&
                in.grid.getEntry(r,c,dir.turn(-1)) == '#') {
                //a reglular corner, but will be handled by the other class
                    continue;
                } 
                if(in.grid.getEntry(r,c,dir) == '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) == '#' &&
                in.grid.getEntry(r,c,dir.turn(-1)) == '#') {
                    //not a corner, completely encased in wall
                    continue;
                } else if (in.grid.getEntry(r,c,dir) != '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) == '#' &&
                in.grid.getEntry(r,c,dir.turn(-1)) == '#')
                {
                    //log.debug("Regular corner r {} c {} dir {}", r, c, dir);
                    //regular corner
                    //##
                    //#.
                    corners.add(new Corner( c+ (dir.getDeltaX() + 1) / 2, r+ 
                        (dir.getDeltaY() + 1) / 2) );
                } else if (in.grid.getEntry(r,c,dir) != '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) == '#' &&
                    in.grid.getEntry(r,c,dir.turn(-1)) != '#') {
                    //flat, no corner
                    continue;
                }                        
                else if (in.grid.getEntry(r,c,dir) != '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) != '#' &&
                in.grid.getEntry(r,c,dir.turn(-1)) == '#') {
                    //other case that is flat
                    continue;
                } else if (in.grid.getEntry(r,c,dir) != '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) != '#' &&
                    in.grid.getEntry(r,c,dir.turn(-1)) != '#') {
                    //isolated corner
                    corners.add( new Corner( c+(dir.getDeltaX() + 1) / 2, 
                        r+(dir.getDeltaY() + 1) / 2, -dir.getDeltaX(),
                        -dir.getDeltaY()) );
                } else
                if (in.grid.getEntry(r,c,dir) == '#' &&
                    in.grid.getEntry(r,c,dir.turn(1)) != '#' &&
                in.grid.getEntry(r,c,dir.turn(-1)) != '#') {
                    corners.add( new Corner( c+(dir.getDeltaX() + 1) / 2, 
                        r+(dir.getDeltaY() + 1) / 2, -dir.getDeltaX(),
                        -dir.getDeltaY()) );
                } else {
                    log.debug("Dir {} left {} right {} sq {} {} {}",
                        dir,
                        dir.turn(1),
                        dir.turn(-1),
                        in.grid.getEntry(r,c,dir),
                        in.grid.getEntry(r,c,dir.turn(1)),
                        in.grid.getEntry(r,c,dir.turn(-1)));
                    Preconditions.checkState(false);   
                }
                
            }
            
            
        
        }
           
    }
    
    public String handleCase(InputData in) {
        
        List<Corner> corners = Lists.newArrayList();
        List<LineObj> walls = Lists.newArrayList();
        
        parseGrid(in, corners, walls);
        
        
        /*
        //SW
        corners.add(new Point(1,1));
        //SE
        corners.add(new Point(in.W - 1,1));
        //NE
        corners.add(new Point(in.W - 1,in.H - 1));
        //NW
        corners.add(new Point(1,in.H - 1));*/
        
        for(Corner corner : corners) {
            log.debug("Corner {}", corner);
        }
        
        
        int idx = in.grid.getIndexesOf('X').iterator().next();
        int[] rowCol = in.grid.getRowCol(idx);
        
        Point self = new Point(rowCol[1] + .5 , rowCol[0] + .5);
        /*
        List<Line> walls = Lists.newArrayList();
        for(Corner corner : corners) {            
            Point closestVert = closestVertical(corners, corner); 
            Point closestHor = closestHorizontal(corners, corner);
            log.debug("Corner {} ver {} hor {}", corner, closestVert, closestHor);
            if (closestVert.getY() > corner.location.getY())
                walls.add(new Line(corner.location, closestVert));
            
            if (closestHor.getX() > corner.location.getX()) 
                walls.add(new Line(corner.location, closestHor));
        }*/
        
        for(LineObj wall : walls) {
            log.debug("Wall {}", wall);   
        }

        //wall [0]  SW SE  -- south
        //wall [1]  SE NE  -- east
        //wall [2]  NE NW  -- north
        //wall [3]  NW SW  -- west
        
        //Hit walls and corners directly
        
        int count = 0;
        
        count = count(corners, walls, self, in);
        
        return String.format("Case #%d: %d", in.testCase, count);
        
        
/*        
        for(int w = 0; w < 4; ++w) {
            Line wall = walls.get(w);
            Line perp = wall.getLinePerpendicular(self);
            Point intersection = perp.getIntersection(wall);
            if (2*intersection.distance(self) <= in.D) {
                log.debug("Direct reflection with wall {}.  Int point {}", wall, intersection);
                ++count;
            }
            
            Line side1 = walls.get( (w+3) % 4);
            Line side2 = walls.get( (w+1) % 4);
            for(int triangles = 1; triangles <= 50; ++triangles) {
                Point[] intPoints = getIntersectionPoints(self,wall,side1,side2,triangles);
                
                double dSide1 = intPoints[0].distance(self);
                double dSide2 = intPoints[1].distance(self);
                double distance = 2 * triangles * ( intPoints[0].distance(self) + intPoints[1].distance(self));
                
                if (distance <= in.D) {
                    count += 2;
                    
                    log.debug("Bounced off wall to {}.  Int points {} {}", wall, intPoints[0], intPoints[1]);
                    
                    if (Line.dc.compare(dSide1,dSide2) != 0) {
                        //log.debug("Non symmetric, get another 2");
                       // count += 2;
                    }
                } else {
                    break;
                }
            }
        }
        
        for(Point corner : corners) {
            if (2*self.distance(corner) <= in.D) {
                log.debug("Hit corner {}", corner);
                ++count;
            }
            
            for(Line wall : walls) {
                
            triangleLoop:
            for(int triangles = 1; triangles <= 50; ++triangles) {
                
                
                    
                    if (wall.onLine(corner))
                        continue;
                    
                    Point[] intP = getIntersectionPointsCorner(corner,self,wall, triangles);
                                
                    boolean one = false;
                    
                    double distance = 2* ( (2 * triangles - 1) * intP[1].distance(corner) + intP[0].distance(self));
                    
                    if (distance <= in.D) {
                        log.debug("Hitting (opp. wall first) corner {} from wall {} triangles {}  distance {}", corner,wall,triangles, distance);
                        ++count;
                        one = true;
                    }
                    
                    double d3 = intP[3].distance(corner);
                       double d4 = intP[2].distance(self);
                    distance = 2 * ( (2 * triangles ) * intP[3].distance(corner) + intP[2].distance(self) );
                    
                    if (distance <= in.D) {
                        log.debug("Hitting (corner wall first) corner {} from wall {} triangles {}  distance {}", corner,wall,triangles, distance);                        
                        ++count;
                        one = true;
                    }
                    
                    if (!one)
                        break triangleLoop;
                }
                
                
            }
            
        }*/
        
        //return String.format("Case #%d: %d", in.testCase, count);
        
    }
    
    public Point[] getNextIntersectionAndDirection(List<Corner> corners,  List<LineObj> walls, 
        Point point, Point direction, Point self, InputData in)
    {
        //Create a line with point and direction
        Line line = new Line(point, point.add(direction));
        
        log.debug("Get intersection.  pt {} dir {} line {}", point, direction, line);

        Point[] retVal = null;
        double closestDis = Double.MAX_VALUE;
        //double closestAbsorbDis = Double.MAX_VALUE;        
        //List<Point> iPoints = new ArrayList<>();
        
        //Intersect with all lines
        for(LineObj wallObj : walls)
        {
            Line wall = wallObj.line;
            
            if (wallObj.orientation.getDeltaY() != 0 && 
                Double.compare(wallObj.orientation.getDeltaY(), 0) ==
            Double.compare(direction.getY(), 0)) {
                continue;
            }
            
            if (wallObj.orientation.getDeltaX() != 0 && 
                Double.compare(wallObj.orientation.getDeltaX(), 0) ==
                Double.compare(direction.getX(), 0)) {
                    continue;
            }
            
            
            
            Point intersection = wall.getIntersection(line);
            
            //iPoints.add(intersection);
            if (intersection == null)
                continue;
            
            if (!wall.isBetween(intersection)) {
               // log.debug("Intersection {} not between {}", intersection, wall);
                continue;
            }
            
            if (point.equals(intersection)) {
                continue;
            }
            
            //Check that the slope is the same as direction
            Point checkDir = intersection.translate(point);
            
            /*
            if (Line.dc.compare( Math.atan2(checkDir.getY(), checkDir.getX()),
                Math.atan2(direction.getY(), direction.getX())) != 0) 
            {
                continue;
            }*/
            
            if (!checkDir.normalize().equals(direction.normalize())) {
                continue;
            }
            
            Point newDirection = null;
            log.debug("Intersection {} dir {} check dir {}  wall {}", intersection, direction, checkDir, wall);
            
            Line checkLine = new Line(point, intersection);
            if (checkLine.isBetween(self) && !point.equals(self)) {
                //log.debug("Self {} checkline {}", self, checkLine);
                intersection = self;
            }
            
            CornerCase cc = matchesCorner(corners, intersection, direction);
            
            if (cc == CornerCase.REFLECT) {
                log.debug("Hit corner {} {}", intersection.getX(), intersection.getY());                
                newDirection = new Point(-direction.getX(), -direction.getY()) ;
            } else if (cc == CornerCase.NOTHING) {
               newDirection = wall.getType() == Line.Type.HORIZONTAL ?
                new Point(direction.getX(), -direction.getY()) :
                new Point(-direction.getX(), direction.getY());
            } else if (cc == CornerCase.ABSORB) {
                log.debug("Absorb corner {} {}", intersection.getX(), intersection.getY());
                newDirection = new Point(0,0);
            } else if (cc == CornerCase.PASSTHRU) {
                log.debug("Pass thru corner {} {}", intersection.getX(), intersection.getY());
                continue;   
            }
            
            double dis = intersection.distance(point);
            log.debug("Wall {} type {} dis {}", wall, wall.getType(), dis);
            if (dis < closestDis) {
                retVal = new Point[] { intersection, newDirection };    
                closestDis = dis;
            }
        }
        
        if (retVal == null) {
            log.error("Intersection not found.  pt {}  dir {}", point, direction);
        }
        /*
        if (closestAbsorbDis < closestDis) {
            log.debug("Absorbed  pt {}  dir {}", point, direction);
            return null;
        }*/
        
        /*for(Point iPoint : iPoints) {
            log.error("Intersection point potential {}", iPoint);   
        }*/
        return retVal;
    }
    
    public boolean tracePath(List<Corner> corners,  List<LineObj> walls, Point self, Point initialDir, InputData in)
    {
        List<Point> points = Lists.newArrayList();
        double distance = 0;
        
        points.add(self);
        
        Point dir = initialDir;
        Point point = self;
        
        int iter = 0;
        //log.debug("Beginning tracePath point {}  direction {}", point, dir);
        while(distance <= in.D && iter < 100)
        {
            ++iter;
            Point[] ptDir = getNextIntersectionAndDirection(corners, walls, point, dir, self, in);
            
            if (ptDir == null) {
                log.error ("Null path dir.  self {}  initial dir {}", self, initialDir);
                for(Point p : points) {
                    log.debug("Error path {}", p);
                }
                return false;
            }
        
            if (ptDir[1].equals(new Point(0,0))) {
                log.debug("Absorbed at {} starting pt {} dir {}", ptDir[0], self, initialDir);
                return false;                
            }
            
            
            points.add(ptDir[0]);
            double addDistance = points.get(points.size() - 1).distance(points.get(points.size() - 2));
            Preconditions.checkState(addDistance > 0);
            distance += addDistance;
            
            //log.debug("tracepath intersection.  Point {}  direction {} distance + {} = {}", ptDir[0], ptDir[1], df.format(addDistance), df.format(distance));
            
            if (ptDir[0].equals(self)) {
          //      log.debug("Line crosses self");
                break;                
            }
            
            point = ptDir[0];
            dir = ptDir[1];
        }
        
        //log.debug("Tracepath done, distance {}", distance);
        if (distance <= in.D) {
            log.debug("Printing winning path distance {}  self {}  initial dir {}", distance, self, initialDir);
            for(Point p : points) {
                log.debug("Winning path {} {}", p.getX(), p.getY());
            }
            return true;   
        }
        
        return false;
    }
    
    
    static DecimalFormat df;
    
    
    static {
        df = new DecimalFormat("0.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
    }
    
    
    public int count(List<Corner> corners,  List<LineObj> walls, Point self, InputData in) 
    {
            
        int count = 0;
        int max = in.D;
        
        Set<Point> dirs = new TreeSet<>();
        dirs.add(new Point(0,0));
        
        for(int y = -max; y <= max; ++y) {
            //log.info("count y {}", y);
            for(int x = -max; x <= max; ++x) {
                Point dir = new Point(x,y);
                Point norm = dir.normalize();
                if (dirs.contains(norm) || dirs.contains(dir)) {
                    continue;
                }
                dirs.add(norm);

                if (tracePath(corners, walls, self, dir, in)) {
                   // log.debug("Path counts !");
                    ++count;
                }                
            }
        }
        

        return count;
    }

    
    /**
     * 
     * @param corners SW, SE, NE, NW
     * @param self
     * @param firstPoint
     * @return
     */
    public List<Point> simulateLight(List<Point> corners,  List<Line> walls, Point self, Point firstPoint, int hops) {
     
        Point iP = firstPoint;
        Line vec = new Line(self, iP);

        Point from = self;
        double fromAngle = Math.atan2(iP.getY() - from.getY(), iP.getX() - from.getX());
        fromAngle = Angle.makeAnglePositive(fromAngle);

        DoubleComparator dc = new DoubleComparator(0.0001);


        List<Point> points = Lists.newArrayList();

        for (int j = 0; j < hops; ++j) {

            boolean foundWall = false;

            for (Line wall : walls) {

                Point intersection = wall.getIntersection(vec);
                if (intersection == null)
                    continue;
                
                if (j==0 && !wall.onLine(firstPoint))
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

                    points.add(intersection);

                    foundWall = true;
                    from = intersection;
                    fromAngle = -fromAngle;
                    fromAngle = Angle.makeAnglePositive(fromAngle);

                    // log.debug("New angle {}", fromAngle * 180 / Math.PI);

                    double y = Math.sin(fromAngle) + from.getY();
                    double x = Math.cos(fromAngle) + from.getX();

                    vec = new Line(from, new Point(x, y));

                    break;

                }

            }

            Preconditions.checkState(foundWall);

        }

        return points;
        
    }

}
