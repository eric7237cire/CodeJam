package codejam.y2012.round_qual.hall_of_mirrors;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.GraphAdjList;
import codejam.utils.geometry.Angle;
import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.DoubleComparator;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
     //  return new String[] {"sample.in"};
       //return new String[] {"D-small-practice.in"};
       //return new String[] {"D-large-practice.in"};
        return new String[] {"D-small-practice.in", "D-large-practice.in"};
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        in.H = scanner.nextInt();
        in.W = scanner.nextInt();
        in.D  = scanner.nextInt();
        in.grid  = GridChar.buildFromScannerYZeroBottom(scanner,in.H,in.W, '#');
        
        //log.info("TestCase {} Grid {}", testCase, in.grid);
        return in;
    }
    
  
    
    
    private static class Corner {
        public Point location;
        
        //Within 45 degrees, in other words same component sines get absorbed
        PointInt absorbVector;
        public String toString() {
            return location.toString() + " absorb " + absorbVector;
        }
        
        Corner(double x, double y) {
            location = new Point(x, y);
            absorbVector = null;
        }
        
        Corner(double x, double y, int aX, int aY) {
            this(x, y);
            absorbVector = new PointInt(aX, aY);
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
    
    CornerCase matchesCorner(List<Corner> corners, Point point, PointInt direction) 
    {
        for(Corner corner : corners) {
            if (!corner.location.equals(point))
                continue;
            
            if (corner.absorbVector == null)
                return CornerCase.REFLECT;
            
            int cmpY = Integer.compare(0, direction.getY());
            int cmpY2 = Integer.compare(0, corner.absorbVector.getY());
            
            int cmpX = Integer.compare(0, direction.getX());
            int cmpX2 = Integer.compare(0, corner.absorbVector.getX());
            
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
        
        
        
        //return String.format("Case #%d: %d", in.testCase, count);
        
    }
    
    public MutablePair<Point, PointInt> getNextIntersectionAndDirection(List<Corner> corners,  List<LineObj> walls, 
        Point point, PointInt direction, Point self, InputData in)
    {
        //Create a line with point and direction
        Line line = new Line(point, point.add(direction.toPoint()));
        
        log.debug("Get intersection.  pt {} dir {} line {}", point, direction, line);

        MutablePair<Point, PointInt> retVal = new MutablePair<>();
        double closestDis = Double.MAX_VALUE;
        //double closestAbsorbDis = Double.MAX_VALUE;        
        //List<Point> iPoints = new ArrayList<>();
        
        //Intersect with all lines
        for(LineObj wallObj : walls)
        {
            Line wall = wallObj.line;
            
            if (wallObj.orientation.getDeltaY() != 0 && 
                Integer.compare(wallObj.orientation.getDeltaY(), 0) ==
                Integer.compare(direction.getY(), 0)) {
                continue;
            }
            
            if (wallObj.orientation.getDeltaX() != 0 && 
                Integer.compare(wallObj.orientation.getDeltaX(), 0) ==
                Integer.compare(direction.getX(), 0)) {
                    continue;
            }
            
            
            
            Point intersection = wall.getIntersection(line);
            
            //iPoints.add(intersection);
            if (intersection == null)
                continue;
            
            if (!wall.onLineSegment(intersection)) {
               // log.debug("Intersection {} not between {}", intersection, wall);
                continue;
            }
            
            if (point.equals(intersection)) {
                continue;
            }
            
            //Check that the slope is the same as direction
            Point checkDir = intersection.translate(point);
            
            if (!checkDir.normalize().equals(direction.toPoint().normalize())) {
                continue;
            }
            
            PointInt newDirection = null;
            log.debug("Intersection {} dir {} wall {}", intersection, direction, wall);
            
            Line checkLine = new Line(point, intersection);
            if (checkLine.onLineSegment(self) && !point.equals(self)) {
                //log.debug("Self {} checkline {}", self, checkLine);
                intersection = self;
            }
            
            CornerCase cc = matchesCorner(corners, intersection, direction);
            
            if (cc == CornerCase.REFLECT) {
                log.debug("Hit corner {} {}", intersection.x(), intersection.y());                
                newDirection = new PointInt(-direction.getX(), -direction.getY()) ;
            } else if (cc == CornerCase.NOTHING) {
               newDirection = wall.getType() == Line.Type.HORIZONTAL ?
                new PointInt(direction.getX(), -direction.getY()) :
                new PointInt(-direction.getX(), direction.getY());
            } else if (cc == CornerCase.ABSORB) {
                log.debug("Absorb corner {} {}", intersection.x(), intersection.y());
                newDirection = new PointInt(0,0);
            } else if (cc == CornerCase.PASSTHRU) {
                log.debug("Pass thru corner {} {}", intersection.x(), intersection.y());
                continue;   
            }
            
            double dis = intersection.distance(point);
            log.debug("Wall {} type {} dis {}", wall, wall.getType(), dis);
            if (dis < closestDis) {
                retVal.setLeft(intersection);
                retVal.setRight(newDirection );    
                closestDis = dis;
            }
        }
        
        
        return retVal;
    }
    
    
    
    public boolean tracePath(List<Corner> corners,  List<LineObj> walls, Point self, PointInt initialDir, InputData in)
    {
        List<Point> points = Lists.newArrayList();
        double distance = 0;
        
        points.add(self);
        
        PointInt dir = initialDir;
        Point point = self;
        
       
        //log.debug("Beginning tracePath point {}  direction {}", point, dir);
        while(distance <= in.D )
        {
       
            MutablePair<Point, PointInt> ptDir = getNextIntersectionAndDirection(corners, walls, point, dir, self, in);
            
            if (ptDir == null) {
                log.error ("Null path dir.  self {}  initial dir {}", self, initialDir);
                for(Point p : points) {
                    log.debug("Error path {}", p);
                }
                return false;
            }
        
            if (ptDir.getValue().equals(new PointInt(0,0))) {
                log.debug("Absorbed at {} starting pt {} dir {}", ptDir.getKey(), self, initialDir);
                return false;                
            }
            
            
            points.add(ptDir.getKey());
            double addDistance = points.get(points.size() - 1).distance(points.get(points.size() - 2));
            Preconditions.checkState(addDistance > 0);
            distance += addDistance;
            
            //log.debug("tracepath intersection.  Point {}  direction {} distance + {} = {}", ptDir[0], ptDir[1], df.format(addDistance), df.format(distance));
            
            if (ptDir.getKey().equals(self)) {
          //      log.debug("Line crosses self");
                break;                
            }
            
            point = ptDir.getKey();
            dir = ptDir.getValue();
        }
        
        //log.debug("Tracepath done, distance {}", distance);
        if (DoubleMath.fuzzyCompare(distance, in.D, Point.tolerance) <= 0) {
            log.debug("Printing winning path distance {}  self {}  initial dir {}", distance, self, initialDir);
            for(Point p : points) {
                log.debug("Winning path {} {}", p.x(), p.y());
            }
            return true;   
        }
        
        return false;
    }
    
    
    
    public int count(List<Corner> corners,  List<LineObj> walls, Point self, InputData in) 
    {
            
        int count = 0;
        int max = in.D;
        
        Set<PointInt> dirs = new HashSet<>();
                
        for(int y = -max; y <= max; ++y) {
            //log.info("count y {}", y);
            for(int x = -max; x <= max; ++x) {
                PointInt direction = new PointInt(x, y);
                if (x==0 && y==0) {
                    continue;
                } else if (x == 0) {
                    direction.setY(direction.getY() / Math.abs(direction.getY()));
                } else if (y==0) {
                    direction.setX(direction.getX() / Math.abs(direction.getX()));
                } else {
                    int gcd = ArithmeticUtils.gcd(direction.getX(), direction.getY());
                    direction.setX(direction.getX() / gcd);
                    direction.setY(direction.getY() / gcd);
                }
                
                if (dirs.contains(direction))                    
                    continue;
                
                dirs.add(direction);
                               

                if (tracePath(corners, walls, self, direction, in)) {
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
        double fromAngle = Math.atan2(iP.y() - from.y(), iP.x() - from.x());
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

                double angleIntersection = Math.atan2(intersection.y() - from.y(), intersection.x() - from.x());
                angleIntersection = Angle.makeAnglePositive(angleIntersection);

                // other side of line
                double angleIntersection2 = angleIntersection + Math.PI;
                angleIntersection2 = Angle.makeAnglePositive(angleIntersection2);

                if (dc.compare(angleIntersection, fromAngle) != 0 && dc.compare(angleIntersection2, fromAngle) != 0)
                    continue;

                if (wall.onLineSegment(intersection)) {
                    log.debug("Intersection found with wall {} = {}.  Angle is {}", wall, intersection, angleIntersection * 180d / Math.PI);

                    points.add(intersection);

                    foundWall = true;
                    from = intersection;
                    fromAngle = -fromAngle;
                    fromAngle = Angle.makeAnglePositive(fromAngle);

                    // log.debug("New angle {}", fromAngle * 180 / Math.PI);

                    double y = Math.sin(fromAngle) + from.y();
                    double x = Math.cos(fromAngle) + from.x();

                    vec = new Line(from, new Point(x, y));

                    break;

                }

            }

            Preconditions.checkState(foundWall);

        }

        return points;
        
    }

}
