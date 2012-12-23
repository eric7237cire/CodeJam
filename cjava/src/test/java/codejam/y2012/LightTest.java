package codejam.y2012;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.y2012.round_qual.hall_of_mirrors.Main;

import com.google.common.collect.Lists;

public class LightTest {


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
    }
}
