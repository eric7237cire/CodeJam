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
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super("E");
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

  
    @Override
    public String handleCase(InputData in) {
       
        double area = 0;
        
        List<Ray> rays = getLines(in.redLight.toPoint(), in);
        for(int r1 = 0; r1 < rays.size(); ++r1) {
            int r2 = r1 == rays.size() - 1 ? 0 : r1+1;
                        
            Ray ray1 = rays.get(r1);
            Ray ray2 = rays.get(r2);
            
            log.debug("Calculating\n ray1 {}\nray 2 {} \n", ray1,ray2);
            
        
            if (ray1.pillar == null && ray2.pillar == null) {
                //2 corners
                List<Point> points =
                         Arrays.<Point>asList(corners[ray1.cornerIdx],
                                corners[ray2.cornerIdx],
                                in.redLight.toPoint());
                double tArea = Polygon.area(points);
                area += tArea;
                log.debug("Adding corner to corner {}", tArea);
            } else if (ray1.pillar != null && ray2.pillar == null) {
                
                //Find intersection with wall
                
                //Clockwise corner
                int prevCornerIdx = ray2.cornerIdx == 0 ? 3 : ray2.cornerIdx - 1;
                Line wall = new Line(corners[prevCornerIdx], corners[ray2.cornerIdx]);
                
                Point wallInt = ray1.line.getIntersection(wall);
                
                List<Point> points =
                        Arrays.<Point>asList(
                                wallInt,
                               corners[ray2.cornerIdx],
                               in.redLight.toPoint());
                double tArea = Polygon.area(points);
                
                log.debug("Adding circle/corner.  wall {} intersection {} area {}",
                        wall, wallInt, tArea);
               
               area += tArea;
               
                
            } else if (ray1.pillar == null && ray2.pillar != null) {
                
                //Find intersection with wall
                
                //Counter Clockwise corner (+ degree direction)
                int nextCornerIdx = ray1.cornerIdx == 3 ? 0 : ray1.cornerIdx + 1;
                Line wall = new Line(corners[nextCornerIdx], corners[ray1.cornerIdx]);
                
                Point wallInt = ray2.line.getIntersection(wall);
                
                List<Point> points =
                        Arrays.<Point>asList(
                                wallInt,
                               corners[ray1.cornerIdx],
                               in.redLight.toPoint());
                double tArea = Polygon.area(points);
                
                log.debug("Adding corner/circle.  wall {} intersection {} area {}",
                        wall, wallInt, tArea);
               
               area += tArea;
               
                
            } else if (Objects.equal(ray1.pillar, ray2.pillar)) {
                Point T1 = ray1.line.getP2();
                Point T2 = ray2.line.getP2();
                
                List<Point> points =
                        Arrays.<Point>asList(
                                T1,
                               T2,
                               in.redLight.toPoint());
               double tArea = Polygon.area(points);
               
               double segDist = T1.distance(T2);
               
               double segArea = ray1.pillar.findSegmentArea(segDist);
               
               log.debug("Adding same circle {}", tArea-segArea);
               area += tArea - segArea;
            } else if (ray1.pillar != null && ray2.pillar != null && 
                    !(Objects.equal(ray1.pillar, ray2.pillar)) &&
                    ray2.first
                    )
            {
                /**
                 * Ray2 is first, so it must intersect circle 1 
                 */
                Point I2 = ray1.pillar.getClosestPointIntersectingLine(ray2.line);
               
                Point T1 = ray1.line.getP2();
                
                List<Point> points =
                        Arrays.<Point>asList(
                               T1,
                               I2,                               
                               in.redLight.toPoint());
               double tArea = Polygon.area(points);
               
               double segDist = I2.distance(T1);
               
               double segArea = ray1.pillar.findSegmentArea(segDist);
               
               log.debug("Adding different circle {}", tArea-segArea);
               area += tArea - segArea;
        
            }if (ray1.pillar != null && ray2.pillar != null && 
                    !(Objects.equal(ray1.pillar, ray2.pillar)) &&
                    !ray2.first
                    )
            {
                /**
                 * Ray2 is second, so ray1 must intersect circle 2 
                 */
                Point I1 = ray2.pillar.getClosestPointIntersectingLine(ray1.line);
                
                Point T2 = ray2.line.getP2();
                
                List<Point> points =
                        Arrays.<Point>asList(
                               T2,
                               I1,                               
                               in.redLight.toPoint());
               double tArea = Polygon.area(points);
               
               double segDist = I1.distance(T2);
               
               double segArea = ray1.pillar.findSegmentArea(segDist);
               
               log.debug("Adding different circle {}", tArea-segArea);
               area += tArea - segArea;
        
            } else {
                log.debug("Case not covered");
            }
        }
        
        //9985.392182804038400000
        return String.format("Case #%d: %s", in.testCase, DoubleFormat.df7.format(area));
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
         * There are 2 rays, first means that it comes
         * first in the ordering
         */
        boolean first; 
        
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
                
                //Don't care if it is behind
                if (!line.onLineSegment(intersection))
                    continue;
                
                if (intDis < lineLen) {
                    log.debug("Removing line {} to pillar {}, intersection with circle {} at {} was closer",
                            line, ray.pillar, c, intersection);
                    lineIt.remove();
                    continue rayLoop;
                }
            }
        }
        
        //Now order the lines in polar order
        Collections.sort(lines, new Comparator<Ray>(){

            @Override
            public int compare(Ray o1, Ray o2)
            {
                
                return Double.compare(o1.ang,o2.ang);
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