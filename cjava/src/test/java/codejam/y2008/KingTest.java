package codejam.y2008;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.ArticulationPoint;
import codejam.utils.datastructures.BitSetInt;
import codejam.utils.datastructures.Bridge;
import codejam.utils.datastructures.GraphInt;
import codejam.utils.utils.GridChar;
import codejam.y2008.round_amer.king.InputData;
import codejam.y2008.round_amer.king.Main;

public class KingTest {
    final static Logger log = LoggerFactory.getLogger(KingTest.class);
    
  //  @Test
    public void testBridge() {
        GraphInt g = new GraphInt();
        g.addConnection(0,1);
        g.addConnection(0,2);
        
        g.addConnection(1,2);
        g.addConnection(2,3);
        g.addConnection(3,4);
        
        g.addConnection(4,5);
        g.addConnection(5,6);
        g.addConnection(4,6);
        
        g.addConnection(3,7);
        g.addConnection(7,8);
        g.addConnection(3,8);
        
        Bridge b = new Bridge(g);
        
        List<Pair<Integer,Integer>> bridges = b.getBridges();
        
        ArticulationPoint ap = new ArticulationPoint(g);
        
        for(int v = 0; v <=8; ++v) {
            log.info("Articulation point {} = {}", v, ap.isArticulation(v));
        }
        
    }
    
    @Test
    public void testOddTheory() {
        log.debug("Starting");
               
        GridChar grid = GridChar.buildEmptyGrid(4,4,'#');
        InputData input = new InputData(1);
        input.grid = grid;
        input.row = 4;
        input.col = 4;
        
        Main m = new Main();
        
        int max = 1 << 16;
        for(int perm = 0; perm < max; ++perm) {
            BitSetInt bs = new BitSetInt(perm);
            bs.setBit(15);
            bs.setBit(14);
            List<Integer> openSquares = new ArrayList<>(16);
            
            for(int sq = 0; sq < 16; ++sq) {
                if (bs.isSet(sq)) {
                    grid.setEntry(sq,'#');                    
                } else {
                    grid.setEntry(sq, '.');
                    openSquares.add(sq);
                }
            }
           // log.debug("Grid prem{} {}", perm, grid);
            
            for(int i = 0; i < openSquares.size(); ++i) {
                if (i > 0) {
                    grid.setEntry(openSquares.get(i-1), '.');
                }
                
                grid.setEntry(openSquares.get(i), 'K');
                
                String r2 = m.bruteForce(input);
                InputData id = new InputData(1);
                id.col=4;
                id.row =4;
                id.grid = new GridChar(grid);
                String r1 = m.awinsIfEven(id);
                
                //
                //assertEquals(r2,r1);
                if (!r1.equals(r2)) {
                    log.debug("Grid {}\n r1 {} r2 {}", grid, r1, r2);  
                }
            }
        }
    }
}
