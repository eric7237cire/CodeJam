package codejam.y2012;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.fraction.Fraction;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;
import codejam.y2012.round_qual.hall_of_mirrors.Main;

import com.google.common.collect.Lists;

public class LightTest {

    @Test
    public void testGetLines() {
        
        StringBuffer sb = new StringBuffer();
        sb.append("########## ");//9 -10
        sb.append("#........# "); //8-9
        sb.append("###...#### ");//7 - 8
        sb.append("#......#.# ");
        sb.append("#....#X#.# "); //5 - 6
        sb.append("#...###..# ");
        sb.append("#........# "); //3 - 4
        sb.append("#..#.....# ");
        sb.append("#..#.....# "); //1 - 2
        sb.append("########## ");
        /*         1234567890
         * 
         */
        
        Scanner scanner = new Scanner(sb.toString());
        
        GridChar grid = GridChar.buildFromScannerYZeroBottom(scanner, 10,10, '#');
        
        Main m = new Main();
        
        List<Main.LineObj> list = Lists.newArrayList();
        m.parseWalls(grid, list);
        
        //east then west
        int i = 0;
        assertEquals(new Point(1,1), list.get(i).line.getP1());
        assertEquals(new Point(1,7), list.get(i).line.getP2());
        assertEquals(Direction.EAST, list.get(i++).orientation);
        
        assertEquals(new Point(1,8), list.get(i).line.getP1());
        assertEquals(new Point(1,9), list.get(i).line.getP2());
        assertEquals(Direction.EAST, list.get(i++).orientation);
        
        i=3;
        assertEquals(new Point(4,1), list.get(i).line.getP1());
        assertEquals(new Point(4,3), list.get(i).line.getP2());
        assertEquals(Direction.EAST, list.get(i++).orientation);
        
        assertEquals(new Point(3,1), list.get(i).line.getP1());
        assertEquals(new Point(3,3), list.get(i).line.getP2());
        assertEquals(Direction.WEST, list.get(i++).orientation);
        
        
        //north then south
        
    }

    @Test
    public void testSampleCase() {

                
        List<Point> corners = Lists.newArrayList();
        
        //SW
        corners.add(new Point(1,1));
        //SE
        corners.add(new Point(2,1));
        //NE
        corners.add(new Point(2,3));
        //NW
        corners.add(new Point(1,3));
        
        List<Line> walls = Lists.newArrayList();
        for(int corner = 0; corner < 4; ++corner) {
            int nextCorner = corner + 1 == 4 ? 0 : corner + 1;
            walls.add(new Line(corners.get(corner), corners.get(nextCorner)));
        }
        
      //wall [0]  SW SE  -- south
        //wall [1]  SE NE  -- east
        //wall [2]  NE NW  -- north
        //wall [3]  NW SW  -- west
        

        Point self = new Point(1.5,2.5);

        Main m = new Main();
        Point[] iP = m.getIntersectionPoints(self, walls.get(1), walls.get(0), walls.get(2), 2);
        
        List<Point> points = null;
        
        points = m.simulateLight(corners,Arrays.asList(walls.get(1), walls.get(0), walls.get(2)),
                self, iP[0],  6);
        
        assertEquals(6, points.size());
        
        Line l = new Line(points.get(4), points.get(5));
        assertTrue(l.onLine(self));
        
        iP = m.getIntersectionPointsCorner(corners.get(2), self, walls.get(3), 2);
        
        points = m.simulateLight(corners, Arrays.asList(walls.get(1), walls.get(3)), self, iP[0], 4);
        
        assertEquals(4, points.size());
        assertEquals(corners.get(2), points.get(3));
        
        log.debug("Last");
        points = m.simulateLight(corners, Arrays.asList(walls.get(1), walls.get(3)), self, iP[2], 5);
        
        assertEquals(5, points.size());
        assertEquals(corners.get(2), points.get(4));
        
        
        iP = m.getIntersectionPointsCorner(corners.get(2), self, walls.get(3), 1);
        
        points = m.simulateLight(corners, Arrays.asList(walls.get(1), walls.get(3)), self, iP[2], 3);
        
        
        iP = m.getIntersectionPoints(self, walls.get(2), walls.get(1), walls.get(3), 2);
        points = m.simulateLight(corners, Arrays.asList(walls.get(1), walls.get(2), walls.get(3)), self, iP[0], 6);
        l = new Line(points.get(5), points.get(4));
        assertTrue(l.onLine(self));
        
        int numTriangles = 12;
        iP = m.getIntersectionPointsCorner(corners.get(2), self, walls.get(3), numTriangles);
        
        points = m.simulateLight(corners, Arrays.asList(walls.get(1), walls.get(3)), self, iP[0], 2 * numTriangles);
        
        points.add(0, self);
        double d = 0;
        
        for(int i = 0; i < points.size() - 1; ++i) {
            d += points.get(i).distance(points.get(i+1));
            l = new Line(points.get(i), points.get(i+1));
            Fraction f = new Fraction(l.getM());
            log.debug("Slope {}", f);
        }
        
        
        log.debug("D is {}", d);
        
        iP = m.getIntersectionPointsCorner(corners.get(2), self, walls.get(3), 25);
    }
    
    final static Logger log = LoggerFactory.getLogger(Main.class);
    @Test
    public void testReflectWalls() {
List<Point> corners = Lists.newArrayList();
        
        //SW
        corners.add(new Point(0,0));
        //SE
        corners.add(new Point(12,0));
        //NE
        corners.add(new Point(12,6));
        //NW
        corners.add(new Point(0,6));
        
        List<Line> walls = Lists.newArrayList();
        for(int corner = 0; corner < 4; ++corner) {
            int nextCorner = corner + 1 == 4 ? 0 : corner + 1;
            walls.add(new Line(corners.get(corner), corners.get(nextCorner)));
        }

        //wall [0]  SW SE  -- south
        //wall [1]  SE NE  -- east
        //wall [2]  NE NW  -- north
        //wall [3]  NW SW  -- west
        
        Point self = new Point(3,2);
        
        Main m = new Main();
        
        //Point[] iPs = getIntersectionPointsCorner( corners.get(1), self, walls.get(2), numTriangles);
        Point[] iP = m.getIntersectionPoints(self, walls.get(2), walls.get(3), walls.get(1), 1);
        
        List<Point> points = m.simulateLight(corners,Arrays.asList(walls.get(3), walls.get(1), walls.get(2)),
                self, iP[0],  4);
        
        assertEquals(new Point(0,3), points.get(0));
        assertEquals(new Point(9,6), points.get(1));
        assertEquals(new Point(12,5), points.get(2));
        assertEquals(new Point(0,1), points.get(3));
        
        assertEquals(new Point(0,3), iP[0]);
        assertEquals(new Point(12,5), iP[1]);
        
        iP = m.getIntersectionPoints(self, walls.get(2), walls.get(3), walls.get(1), 2);
        
        points = m.simulateLight(corners,Arrays.asList(walls.get(3), walls.get(1), walls.get(2)),
                self, iP[0],  2 * 2 + 2);
    }
}
