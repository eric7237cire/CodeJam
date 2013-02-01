package codejam.y2009.round_final.lights;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Circle;
import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.geometry.Polygon;
import codejam.utils.geometry.Triangle;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super("F", true, false);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
        /*
         * One line containing the coordinates x, y of the 
         * red light source.
    One line containing the coordinates x, y of the green light 
    source.
    One line containing the number of pillars n.
    n lines describing the pillars. 
    Each contains 3 numbers x, y, r. 
    The pillar is a disk with the center (x, y) and radius r.

         */
        in.redLight = new PointInt(scanner.nextInt(), scanner.nextInt());
        in.greenLight = new PointInt(scanner.nextInt(), scanner.nextInt());
        in.N = scanner.nextInt();
        
        in.pillars = Lists.newArrayList();
        for(int i = 0; i < in.N; ++i) {
            in.pillars.add(new Circle(scanner.nextInt(),scanner.nextInt(),scanner.nextInt()));
        }
        return in;
    }

    public static class ExTriangle extends Triangle {
        Circle intCircle;
        
        public ExTriangle(Circle intCircle, Point p1, Point p2, Point p3) {
            //p1 is light
            super(p1,p2,p3);
            this.intCircle = intCircle;
            
            Preconditions.checkArgument(p1 != null);
            Preconditions.checkArgument(p2 != null);
            Preconditions.checkArgument(p3 != null);
        }


        double getArea() {
            double tArea = Polygon.area(Arrays.asList(p1,p2,p3));
            
            if (intCircle != null) {
                double segDist = p2.distance(p3);
                
                double segArea = intCircle.findSegmentArea(segDist);
                
                return tArea - segArea;
            }
            
            return tArea;
            
            
        }


        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "ExTriangle [intCircle=" + intCircle +
            ", p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + "]";
        }
    }
    
    public double getAreaPolygonIntersectCircle(List<Point> polygon, Circle circle)
    {
        List<Point> clipped = Lists.newArrayList();
        List<Point> tanPoints = Lists.newArrayList();
        
        for(int p1Idx = 0; p1Idx < polygon.size(); ++p1Idx) {
            int p2Idx = p1Idx + 1;
            if (p2Idx >= polygon.size())
                p2Idx = 0;
            
            Point p1 = polygon.get(p1Idx);
            Point p2 = polygon.get(p2Idx);
            
            double d1ToCenter = p1.distance(circle.getCenter());
            double d2ToCenter = p2.distance(circle.getCenter());
            
            if (DoubleMath.fuzzyCompare(d1ToCenter,circle.getR(),0.0001) >= 0) {
                clipped.add(p1);
            }
            if (DoubleMath.fuzzyCompare(d2ToCenter,circle.getR(),0.0001) >= 0) {
                //clipped.add(p2);
            }
            if (DoubleMath.fuzzyCompare(d1ToCenter,circle.getR(),0.0001) == 0) {
                tanPoints.add(p1);
            }
            if (DoubleMath.fuzzyCompare(d2ToCenter,circle.getR(),0.0001) == 0) {
              //   tanPoints.add(p2);
            }
            
            
        }
        
        Preconditions.checkState(tanPoints.size() == 2);
        
        double pArea = Polygon.area(clipped);
        double segDistance = tanPoints.get(0).distance(tanPoints.get(1));
        double segArea = circle.findSegmentArea(segDistance);
        
        return pArea - segArea;
    }
  
    public String handleCase(InputData in) {
        
        
        List<ExTriangle> redTriangles = getTriangles(in.redLight.toPoint(), in);
        //List<Triangle> redTriangles = Lists.newArrayList();
        
        double redArea = 0;
        
        for(ExTriangle redTriangle : redTriangles) {
            redArea += redTriangle.getArea();
        }
        
        List<ExTriangle> greenTriangles = getTriangles(in.greenLight.toPoint(), in);
        
        double greenArea = 0;
        
        for(ExTriangle greenTriangle : greenTriangles) {
            greenArea += greenTriangle.getArea();
        }
        
        double yellowArea = 0;
        
        for(ExTriangle red : redTriangles) {
            for(ExTriangle green : greenTriangles) {
                List<Point> pts = red.getTriangleIntersection(green);
                if (pts == null || pts.size() < 3) {
                    //Preconditions.checkState(pts.size() == 0);
                    continue;
                }
                
                //For now ignore this case
                if (red.intCircle != null && red.intCircle.equals(green.intCircle)) {
                    double quasiArea = getAreaPolygonIntersectCircle(pts,red.intCircle);
                    yellowArea += quasiArea;
                    continue;
                    
                }
                
                double polyArea = Polygon.area(pts);
                yellowArea += polyArea;
            }
        }
        
        redArea -= yellowArea;
        greenArea -= yellowArea;
        
        double blackArea = 10000 - redArea-greenArea-yellowArea;
        
        for(Circle pillar : in.pillars) {
            blackArea -= pillar.getArea();
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        sb.append(blackArea).append("\n");
        sb.append(redArea).append("\n");
        sb.append(greenArea).append("\n");
        sb.append(yellowArea);
        return sb.toString();
      //9985.392182804038400000
    }
    
    public List<ExTriangle> getTriangles(Point light, InputData in) {
       
        List<Ray> rays = getLines(light, in);
        for(int r1 = 0; r1 < rays.size(); ++r1) {
            log.debug("Ray {} = {}", r1, rays.get(r1));
        }
        List<ExTriangle> triList = Lists.newArrayList();
        
        for(int r1 = 0; r1 < rays.size(); ++r1) {
            int r2 = r1 == rays.size() - 1 ? 0 : r1+1;
                        
            Ray ray1 = rays.get(r1);
            Ray ray2 = rays.get(r2);
            
            log.debug("Calculating\n ray1 {}\nray 2 {} \n", ray1,ray2);
            
        
            if (ray1.pillar == null && ray2.pillar == null) {
                /**
                 * Both rays hit corners
                 */
                triList.add(new ExTriangle(null,
                        light,
                        corners[ray1.cornerIdx],
                                corners[ray2.cornerIdx]));
                log.debug("Adding corner to corner {}", triList.get(triList.size()-1));
            } else if (ray1.pillar != null && ray2.pillar == null) {

                /**
                 * ray1 tangent to circle1, ray2 hits a corner.
                 * 
                 * Need the previous corner to get the line represeting the wall
                 */
                
                //Clockwise corner
                int prevCornerIdx = ray2.cornerIdx == 0 ? 3 : ray2.cornerIdx - 1;
                Line wall = new Line(corners[prevCornerIdx], corners[ray2.cornerIdx]);
                
                Point wallInt = ray1.line.getIntersection(wall);
                
                triList.add(new ExTriangle(null,light, 
                                wallInt,
                               corners[ray2.cornerIdx]));
                
                log.debug("Adding circle/corner.  wall {} intersection {} area {}",
                        wall, wallInt);
                
            } else if (ray1.pillar == null && ray2.pillar != null) {
                
                /**
                 * ray1 hits a corner, ray2 tangent to circle2.
                 * 
                 * This time we need the next corner
                 */
                //Counter Clockwise corner (+ degree direction)
                int nextCornerIdx = ray1.cornerIdx == 3 ? 0 : ray1.cornerIdx + 1;
                Line wall = new Line(corners[nextCornerIdx], corners[ray1.cornerIdx]);
                
                Point wallInt = ray2.line.getIntersection(wall);
                
                triList.add(new ExTriangle(null,light,
                        corners[ray1.cornerIdx],
                        wallInt));
                
                log.debug("Adding corner/circle.  wall {} intersection {}",
                        wall, wallInt);
                
            } else if (Objects.equal(ray1.pillar, ray2.pillar)) {
                
                /**
                 * Same circle
                 */
                Point T1 = ray1.line.getP2();
                Point T2 = ray2.line.getP2();
                
                triList.add(new ExTriangle(ray1.pillar, light, T1, T2));
                
               log.debug("Adding same circle ");
            } else if (ray1.pillar != null && ray2.pillar != null && 
                    !(Objects.equal(ray1.pillar, ray2.pillar)) &&
                    ray2.first && ray1.first
                    )
            {
                /**
                 * Ray2 is first on it's circle, so it must intersect circle 1 
                 */
                Point I2 = ray1.pillar.getClosestPointIntersectingLine(ray2.line);
               
                Point T1 = ray1.line.getP2();
                
                triList.add(new ExTriangle(ray1.pillar, light, T1, I2));
               log.debug("Adding different circle " );
        
            } else if (ray1.pillar != null && ray2.pillar != null && 
                    !(Objects.equal(ray1.pillar, ray2.pillar)) &&
                    !ray2.first && !ray1.first
                    )
            {
                /**
                 * Ray2 is second, so ray1 must intersect circle 2 
                 */
                Point I1 = ray2.pillar.getClosestPointIntersectingLine(ray1.line);
                
                Point T2 = ray2.line.getP2();
                
                triList.add(new ExTriangle(ray1.pillar, light, I1, T2));
               
               log.debug("Adding different circle " );
            } else if (ray1.pillar != null && ray2.pillar != null && 
                    !(Objects.equal(ray1.pillar, ray2.pillar)) &&
                    !ray1.first && ray2.first
                    )
            {
                /**
                 * Both rays hit a circle behind them
                 */
                log.debug("Circle behind");
                Preconditions.checkState(ray1.circleBehind.equals(ray2.circleBehind));
                
                Point I1 = ray1.pointBehind;
                Point I2 = ray2.pointBehind;
                
                triList.add(new ExTriangle(ray1.circleBehind, light,I1,I2));
            } else {
                log.debug("Case not covered");
            }
        }
        
        return triList;
        
        
    }
    
    
    //Counter clockwise
    static Point[] corners = new Point[] {
            new Point(100,100),
            new Point(0, 100),
            new Point(0,0),
            new Point(100,0)            
    };
    
    
    static class Ray
    {
        Line line;
        double ang;
        Point light;
        
        
        Circle pillar;
        /**
         * There are 2 rays to each pillar, first means that it comes
         * first in the ordering
         */
        boolean first; 
        
        /**
         * What the ray hits afterwards
         */
        Circle circleBehind;
        Point pointBehind;
        double distBehind;
        
        int cornerIdx;

        /**
         * 
         * @param line Point 1 is the light, Point 2 is the end of the ray
         * @param light
         * @param pillar
         */
        public Ray(Line line, Point light, Circle pillar) {
            super();
            this.line = line;
            this.ang = getAng(getVec(line));
            this.light = light;
            this.pillar = pillar;
            
            this.distBehind = Double.MAX_VALUE;
        }
        
        public Ray(Point light, int cornerIndex) {
            super();
            this.line = new Line(light, corners[cornerIndex]);
            this.cornerIdx = cornerIndex;
            this.ang = getAng(getVec(line));
            this.light = light;
            this.pillar = null;
        }

        @Override
        public String toString()
        {
            return "Ray [line=" + line + ", ang=" + ang + ", light=" + light + ", pillar=" + pillar + "]";
        }
        
        
    }
    
    List<Ray> getLines(Point p, InputData in) {
        List<Ray> lines = Lists.newArrayList();
        
        for(Circle c : in.pillars) {
            Point[] tanPoints = c.getPointsTangentToLine(p);
            Ray ray1 = new Ray(new Line(p, tanPoints[0]), p, c);
            Ray ray2 = new Ray(new Line(p, tanPoints[1]), p, c);
            
            ray1.first = ray1.ang < ray2.ang;
            ray2.first = !ray1.first;
            lines.add(ray1);
            lines.add(ray2);
        }
        
        for(int c = 0; c < 4; ++c) {
            lines.add(new Ray(p, c));
        }
        
        
        Iterator<Ray> lineIt = lines.iterator();
        
        rayLoop:
        while(lineIt.hasNext()) {
            Ray ray = lineIt.next();
            Line line = ray.line;
            
            double lineLen = line.getP1().distance(line.getP2());
            
            for(Circle c : in.pillars) {
                
                //ONly check other pillars
                if (ray.pillar != null && ray.pillar.equals(c))
                    continue;
                
                Point[] intPoints = c.getPointsIntersectingLine(line);
                
                //Does not intersect
                if (intPoints == null)
                    continue;
                
                double d1 = p.distance(intPoints[0]);
                double d2 = p.distance(intPoints[1]);
                
                //only care about the closer one
                Point intersection = d1 < d2 ? intPoints[0] : intPoints[1];
                
                double intDis = Math.min(d1, d2);
                
                double intAng = getAng(getVec(new Line(p, intersection)));
                
                //Don't care if it is behind the ray
                //if (!line.onLineSegment(intersection))
                if (!DoubleMath.fuzzyEquals(intAng, ray.ang, 0.00001)) {
                    //TODO test this case
                    Preconditions.checkArgument(DoubleMath.fuzzyEquals(Math.PI,Math.abs(intAng-ray.ang), 0.00001));
                    continue;
                }
                
                if (intDis < lineLen) {
                    log.debug("Removing line {} to pillar {}, intersection with circle {} at {} was closer",
                            line, ray.pillar, c, intersection);
                    lineIt.remove();
                    continue rayLoop;
                }
                
                //Find the smallest distance > lineLen
                if (ray.distBehind > intDis) {
                    ray.distBehind = intDis;
                    ray.pointBehind = intersection;
                    ray.circleBehind = c;
                }
            }
        }
        
        //Now order the lines in polar order
        Collections.sort(lines, new Comparator<Ray>(){

            @Override
            public int compare(Ray o1, Ray o2)
            {
                
                int angCmp = DoubleMath.fuzzyCompare(o1.ang,o2.ang, 0.000001);
                
                if (angCmp == 0) {
                    //Can be the case that line is tangent to 2 circles in the same place,
                    //so the tie breaker is the polar angle of the light and the centers of the pillars
                    double aC1 = getAng(getVec(new Line(o1.line.getP1(), o1.pillar.getCenter())));
                    
                    double aC2 = getAng(getVec(new Line(o2.line.getP1(), o2.pillar.getCenter())));
                    
                    return DoubleMath.fuzzyCompare(aC1, aC2, 0.000001);
                } else {
                    return angCmp;
                }
            }
            
        });
        
        return lines;
    }
    
    /**
     * Make p1 the origin
     * @param p
     * @return
     */
    static Point getVec(Line p) {
        return p.getP2().translate(p.getP1());
    }
    
    static double getAng(Point vec) {
        double ang = Math.atan2(vec.getY(), vec.getX());
        if (ang < 0) {
            ang += 2 * Math.PI;
        }
        
        return ang;
    }
}