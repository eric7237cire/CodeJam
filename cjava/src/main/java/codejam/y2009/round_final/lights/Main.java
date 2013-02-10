package codejam.y2009.round_final.lights;

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
import com.google.common.math.DoubleMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        //super();
        super("F", true, true);
    }
    
       
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.redLight = new PointInt(scanner.nextInt(), scanner.nextInt());
        in.greenLight = new PointInt(scanner.nextInt(), scanner.nextInt());
        in.N = scanner.nextInt();
        
        in.pillars = Lists.newArrayList();
        for(int i = 0; i < in.N; ++i) {
            in.pillars.add(new Circle(scanner.nextInt(),scanner.nextInt(),scanner.nextInt()));
        }
        return in;
    }

   
    /**
     * Intersection of 2 quasi triangles.  Remove all points in the circle
     * @param polygon
     * @param circle
     * @return
     */
    public double getAreaPolygonIntersectCircle(List<Point> polygon, Circle circle)
    {
        List<Point> clipped = Lists.newArrayList();
        List<Point> tanPoints = Lists.newArrayList();
        
        for(int p1Idx = 0; p1Idx < polygon.size(); ++p1Idx) {
                        
            Point p1 = polygon.get(p1Idx);
            
            double d1ToCenter = p1.distance(circle.getCenter());
            
            if (DoubleMath.fuzzyCompare(d1ToCenter,circle.r(),0.0001) >= 0) {
                clipped.add(p1);
            }
            
            if (DoubleMath.fuzzyCompare(d1ToCenter,circle.r(),0.0001) == 0) {
                tanPoints.add(p1);
            }
        }
        
        double pArea = Polygon.area(clipped);
        
        /**
         * It is possible for the clipped area to not at all touch the circle, even if
         * both lights are shining on it.  There can be other circles in the way for 
         * example
         */
        Preconditions.checkState(tanPoints.size() == 2 || tanPoints.size() == 0);
        if (tanPoints.size() == 2) {
            double segDistance = tanPoints.get(0).distance(tanPoints.get(1));
            double segArea = circle.findSegmentArea(segDistance);
            
            return pArea - segArea;
        } else {
            return pArea;
        }
        
    }
  
    public String handleCase(InputData in) {
        
        
        List<ExTriangle> redTriangles = getTriangles(in.redLight.toPoint(), in);
        
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
                    continue;
                }
                
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
        sb.append(DoubleFormat.df7.format(blackArea)).append("\n");
        sb.append(DoubleFormat.df7.format(redArea)).append("\n");
        sb.append(DoubleFormat.df7.format(greenArea)).append("\n");
        sb.append(DoubleFormat.df7.format(yellowArea));
        return sb.toString();
    }
    
    /**
     * Go through rays in polar angle order, creating
     * Triangles or QuasiTriangles (see solution for more explanation)
     * 
     */
    
    public List<ExTriangle> getTriangles(Point light, InputData in) {
       
        List<Ray> rays = processRays(light, in);
        for(int r1 = 0; r1 < rays.size(); ++r1) {
            log.debug("Ray {} = {}", r1, rays.get(r1));
        }
        List<ExTriangle> triList = Lists.newArrayList();
        
        for(int r1 = 0; r1 < rays.size(); ++r1) {
            int r2 = r1 == rays.size() - 1 ? 0 : r1+1;
                        
            Ray ray1 = rays.get(r1);
            Ray ray2 = rays.get(r2);
            
           // log.debug("Calculating\n ray1 {}\nray 2 {} \n", ray1,ray2);
            
        
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
                 * We have circle1, then ray1 tangent to it on it's counter clockwise side
                 * then ray2 which is it's counter-clockwise side
                 * 
                 * Because there is no ray hitting the beginning of circle2, circle1 must
                 * be hiding it.  So ray1 hits circle2
                 */
                Point I1 = ray2.pillar.getClosestPointIntersectingLine(ray1.line);
                
                Point T2 = ray2.line.getP2();
                
                triList.add(new ExTriangle(ray2.pillar, light, I1, T2));
               
               log.debug("Adding different circle " );
            } else if (ray1.pillar != null && ray2.pillar != null && 
                    !(Objects.equal(ray1.pillar, ray2.pillar)) &&
                    !ray1.first && ray2.first
                    )
            {
                
                Point I1 = ray1.pointBehind;
                Point I2 = ray2.pointBehind;
                
                
                if (ray1.circleBehind != null && ray1.circleBehind.equals(ray2.circleBehind)) {

                    /**
                     * Both rays hit a circle behind them
                     */
                    log.debug("Circle behind");
                    triList.add(new ExTriangle(ray1.circleBehind, light,I1,I2));
                } else if (ray1.circleBehind == null && ray2.circleBehind == null) {
                    log.debug("Wall behind");
                    triList.add(new ExTriangle(null, light,I1,I2));
                } else {
                    Preconditions.checkState(false);
                }
                
                
                
                
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
    
    static Line[] walls = new Line[] {
        new Line(corners[0], corners[1]),
        new Line(corners[1], corners[2]),
        new Line(corners[2], corners[3]),
        new Line(corners[3], corners[0])
    };
    
    
    /**
     * For each circle, calculate the tangent lines.  
     * Also because the ray's are processed in polar angle order,
     * we need to know which tangent line comes first (lower angle)
     * in a counter clockwise polar angle ordering.
     */
    List<Ray> generateAllRays(Point light, InputData in) {
        List<Ray> rays = Lists.newArrayList();
        
        
        for(Circle c : in.pillars) {
            Point[] tanPoints = c.getPointsTangentToLine(light);
            Ray ray1 = new Ray(new Line(light, tanPoints[0]), light, c);
            Ray ray2 = new Ray(new Line(light, tanPoints[1]), light, c);
            
            //one exception, if ray1 angle is near 0, and ray2 near 2pi, then ray2 is in fact first
            ray1.first = false;
            
            //Normal case, ray1 is less than ray2.ang and they are within PI of each other
            if (ray1.ang < ray2.ang && 
                    DoubleMath.fuzzyCompare(ray2.ang - ray1.ang, Math.PI, 0.00001) <= 0) {
                ray1.first = true;
            }
            
            //The special case, ray1 is very close to 0, ray2 close to 2PI.  
            if (ray1.ang > ray2.ang &&
                    DoubleMath.fuzzyCompare(ray1.ang - ray2.ang, Math.PI, 0.00001) > 0)
            {
                ray1.first = true;
            }
            
            
            ray2.first = !ray1.first;
            rays.add(ray1);
            rays.add(ray2);
        }
        
        //Light to each corner
        for(int c = 0; c < 4; ++c) {
            rays.add(new Ray(light, c));
        }
        
        return rays;
    }
    
    /**
     * Deterimine if a ray intersects anything before it
     * 
     * @param light (red or green)
     * @param in
     * @return
     */
    List<Ray> processRays(Point light, InputData in) {
        
        List<Ray> lines = generateAllRays(light, in);
        
        Iterator<Ray> lineIt = lines.iterator();
        
        rayLoop:
        while(lineIt.hasNext()) {
            Ray ray = lineIt.next();
            Line line = ray.line;
            
            double rayLen = line.getP1().distance(line.getP2());
            
            /**
             * Check every circle, seeing if it intersects the ray before the ray ends
             */
            for(Circle c : in.pillars) {
                
                //Only check other pillars
                if (ray.pillar != null && ray.pillar.equals(c))
                    continue;
                
                Point[] intPoints = c.getPointsIntersectingLine(line);
                
                //Does not intersect
                if (intPoints == null)
                    continue;
                
                double d1 = light.distance(intPoints[0]);
                double d2 = light.distance(intPoints[1]);
                
                //only care about the closer one
                Point intersection = d1 < d2 ? intPoints[0] : intPoints[1];
                
                double intDis = Math.min(d1, d2);
                
                double intAng = new Line(light, intersection).getVector().polarAngle();
                
                //Don't care if it is behind the ray
                if (!DoubleMath.fuzzyEquals(intAng, ray.ang, 0.00001)) {
                    Preconditions.checkArgument(DoubleMath.fuzzyEquals(Math.PI,Math.abs(intAng-ray.ang), 0.00001));
                    continue;
                }
                
                if (intDis < rayLen) {
                    log.debug("Removing line {} to pillar {}, intersection with circle {} at {} was closer",
                            line, ray.pillar, c, intersection);
                    lineIt.remove();
                    continue rayLoop;
                }

                /**
                 * Later on, it will be helpful to know what the ray its 
                 * afterwards
                 */
                //Find the smallest distance > lineLen
                if (ray.distBehind > intDis) {
                    ray.distBehind = intDis;
                    ray.pointBehind = intersection;
                    ray.circleBehind = c;
                }
            }
            
            /**
             * If the ray hit nothing after it was tangent to it's pillar,
             * it must have hit a wall
             */
            if (ray.circleBehind == null) {
            //If there is no circle behind the ray, it must hit a wall
            for(Line wall : walls) {
                Point intersection = ray.line.getIntersection(wall);
                
                if (intersection == null)
                    continue;
                
                if (!wall.onLineSegment(intersection))
                    continue;
                
                double intAng = new Line(light, intersection).getVector().polarAngle();
                
                //Don't care if it is behind the ray (angle = PI)
                if (!DoubleMath.fuzzyEquals(intAng, ray.ang, 0.00001)) {
                    Preconditions.checkArgument(DoubleMath.fuzzyEquals(Math.PI,Math.abs(intAng-ray.ang), 0.00001));
                    continue;
                }
                
                ray.pointBehind = intersection;
                break;
            }
            }
            
            Preconditions.checkState(ray.pointBehind != null);
        }
        
        final List<Ray> toDelete = Lists.newArrayList();
        
        //Now order the lines in polar order
        Collections.sort(lines, new Comparator<Ray>(){

            @Override
            public int compare(Ray o1, Ray o2)
            {
                
                int angCmp = DoubleMath.fuzzyCompare(o1.ang,o2.ang, 0.000001);
                
                if (angCmp == 0) {
                    
                    /**
                     * We have 2 rays with the same polar angle.  Only keep
                     * the closer one.
                     */
                    double aC1 = o1.line.getP1().distance( o1.line.getP2());
                    
                    double aC2 = o2.line.getP1().distance( o2.line.getP2());
                    
                    if (aC1 > aC2) {
                        toDelete.add(o1);
                    } else {
                        toDelete.add(o2);
                    }
                    return DoubleMath.fuzzyCompare(aC1, aC2, 0.000001);
                } else {
                    return angCmp;
                }
            }
            
        });
        
        lines.removeAll(toDelete);
        
                
        return lines;
    }
    
}