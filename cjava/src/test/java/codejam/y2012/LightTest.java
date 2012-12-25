package codejam.y2012;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

   
    
    final static Logger log = LoggerFactory.getLogger(Main.class);
   
}
