package codejam.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.Gaps;
import codejam.utils.datastructures.Gaps.Gap;

public class GapTest {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Test 
    public void testSimple() {
        Gaps gaps = new Gaps();
        gaps.mergeGap(3,3);
        int i = 0;
        
        List<Gap> listGaps = gaps.getAllGaps();
        
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(3,3), listGaps.get(0));
        
        gaps.mergeGap(4,4);
        listGaps = gaps.getAllGaps();
        
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(3,4), listGaps.get(0));
     
        gaps.mergeGap(-2,-2);
        listGaps = gaps.getAllGaps();
        assertEquals(2, listGaps.size());
        assertEquals(new Gap(-2,-2), listGaps.get(0));
        assertEquals(new Gap(3,4), listGaps.get(1));
     
        gaps.mergeGap(0,1);
        listGaps = gaps.getAllGaps();
        assertEquals(3, listGaps.size());
        assertEquals(new Gap(-2,-2), listGaps.get(0));
        assertEquals(new Gap(0,1), listGaps.get(1));
        assertEquals(new Gap(3,4), listGaps.get(2));
        
        gaps.mergeGap(-2,-1);
        listGaps = gaps.getAllGaps();
        assertEquals(2, listGaps.size());
        assertEquals(new Gap(-2,1), listGaps.get(i=0));
        assertEquals(new Gap(3,4), listGaps.get(++i));
        
        gaps.mergeGap(2,3);
        listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-2,4), listGaps.get(i=0));
        
     
        
    }
    
    @Test
    public void testAddDisjoint() {
        Gaps gaps = new Gaps();
               
        
        gaps.mergeGap(-60, 17);
        gaps.mergeGap(19, 99);
        gaps.mergeGap(-78, -62);
        gaps.mergeGap(101,150);
        gaps.mergeGap(-140, -80);
        
        List<Gap> listGaps = gaps.getAllGaps();
        assertEquals(5, listGaps.size());
        
    }
    
    public Gaps getAddMergeBase() {
        Gaps gaps = new Gaps();
        gaps.mergeGap(-30, -20);
        gaps.mergeGap(-10, 0);
        gaps.mergeGap(10, 20);
        gaps.mergeGap(30,40);
        
        List<Gap> listGaps = gaps.getAllGaps();
        int i;
        assertEquals(4, listGaps.size());
        assertEquals(new Gap(-30,-20), listGaps.get(i=0));
        assertEquals(new Gap(-10,0), listGaps.get(++i));
        assertEquals(new Gap(10,20), listGaps.get(++i));
        assertEquals(new Gap(30,40), listGaps.get(++i));

        return gaps;
    }
    
    @Test
    public void testAddMerge() {
        Gaps gaps = new Gaps();
        List<Gap> listGaps;
        int i = 0;
        
        gaps = getAddMergeBase();
        
        //before all 
        gaps.mergeGap(-32, 42);
        listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-32,42), listGaps.get(i=0));

        gaps = getAddMergeBase();
        
        //
        gaps.mergeGap(-31, 9);
        listGaps = gaps.getAllGaps();
        assertEquals(2, listGaps.size());
        assertEquals(new Gap(-31,20), listGaps.get(i=0));
        assertEquals(new Gap(30,40), listGaps.get(++i));


        gaps = getAddMergeBase();
        
        gaps.mergeGap(-30, 1);
        listGaps = gaps.getAllGaps();
        assertEquals(3, listGaps.size());
        assertEquals(new Gap(-30,1), listGaps.get(i=0));
        assertEquals(new Gap(10,20), listGaps.get(++i));
        assertEquals(new Gap(30,40), listGaps.get(++i));

        //starting In gap
        
        gaps = getAddMergeBase();
        
        gaps.mergeGap(-8, 41);
        listGaps = gaps.getAllGaps();
        assertEquals(2, listGaps.size());
        assertEquals(new Gap(-30,-20), listGaps.get(i=0));
        assertEquals(new Gap(-10,41), listGaps.get(++i));
        
        gaps = getAddMergeBase();
        
        gaps.mergeGap(10, 30);
        listGaps = gaps.getAllGaps();
        assertEquals(3, listGaps.size());
        assertEquals(new Gap(-30,-20), listGaps.get(i=0));
        assertEquals(new Gap(-10,0), listGaps.get(++i));
        assertEquals(new Gap(10,40), listGaps.get(++i));
        
        gaps = getAddMergeBase();
        
        gaps.mergeGap(-20, 1);
        listGaps = gaps.getAllGaps();
        assertEquals(3, listGaps.size());
        assertEquals(new Gap(-30,1), listGaps.get(i=0));
        assertEquals(new Gap(10,20), listGaps.get(++i));
        assertEquals(new Gap(30,40), listGaps.get(++i));

       
        //starting Between 2
        
        gaps = getAddMergeBase();
        
        gaps.mergeGap(-18, 41);
        listGaps = gaps.getAllGaps();
        assertEquals(2, listGaps.size());
        assertEquals(new Gap(-30,-20), listGaps.get(i=0));
        assertEquals(new Gap(-18,41), listGaps.get(++i));
        
        gaps = getAddMergeBase();
        
        gaps.mergeGap(-11, 15);
        listGaps = gaps.getAllGaps();
        assertEquals(3, listGaps.size());
        assertEquals(new Gap(-30,-20), listGaps.get(i=0));
        assertEquals(new Gap(-11,20), listGaps.get(++i));
        assertEquals(new Gap(30,40), listGaps.get(++i));

        gaps = getAddMergeBase();
        
        gaps.mergeGap(2, 22);
        listGaps = gaps.getAllGaps();
        assertEquals(4, listGaps.size());
        assertEquals(new Gap(-30,-20), listGaps.get(i=0));
        assertEquals(new Gap(-10,0), listGaps.get(++i));
        assertEquals(new Gap(2,22), listGaps.get(++i));
        assertEquals(new Gap(30,40), listGaps.get(++i));

        
        
        
    }
    
    @Test 
    public void testRemove() {
        Gaps gaps = new Gaps();
        int i = 0;
        
        //Removing nothing just before or after
        gaps.mergeGap(-100, 100);
        gaps.removeGap(-300,-101);
        
        List<Gap> listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-100,100), listGaps.get(i=0));
        
        gaps.removeGap(101,200);
        
        listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-100,100), listGaps.get(i=0));
        
        //Removing just before
        gaps.removeGap(-101,-100);
        
        listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-99,100), listGaps.get(i=0));
        
        gaps.removeGap(-99,-99);
        
        listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-98,100), listGaps.get(i=0));
        
        //Removing just after
        gaps.removeGap(100,100);
        
        listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-98,99), listGaps.get(i=0));
        
        gaps.removeGap(99,100);
        
        listGaps = gaps.getAllGaps();
        assertEquals(1, listGaps.size());
        assertEquals(new Gap(-98,98), listGaps.get(i=0));

        
        //Remove middle
        gaps.removeGap(-97,-97);
        
        listGaps = gaps.getAllGaps();
        assertEquals(2, listGaps.size());
        assertEquals(new Gap(-98,-98), listGaps.get(i=0));
        assertEquals(new Gap(-96,98), listGaps.get(++i));
        
        gaps.removeGap(10,20);
        
        listGaps = gaps.getAllGaps();
        assertEquals(3, listGaps.size());
        assertEquals(new Gap(-98,-98), listGaps.get(i=0));
        assertEquals(new Gap(-96,9), listGaps.get(++i));
        assertEquals(new Gap(21,98), listGaps.get(++i));


        gaps.removeGap(25,45);
        
        listGaps = gaps.getAllGaps();
        assertEquals(4, listGaps.size());
        assertEquals(new Gap(-98,-98), listGaps.get(i=0));
        assertEquals(new Gap(-96,9), listGaps.get(++i));
        assertEquals(new Gap(21,24), listGaps.get(++i));
        assertEquals(new Gap(46,98), listGaps.get(++i));

        //Removing entire sub sets
        gaps.removeGap(-96,45);
        
        listGaps = gaps.getAllGaps();
        assertEquals(2, listGaps.size());
        assertEquals(new Gap(-98,-98), listGaps.get(i=0));
        assertEquals(new Gap(46,98), listGaps.get(++i));
        
    }
    
    int getRandom(Random rand, int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }
    
    @Test
    public void testRandom() {
        final int TOTAL_LEN = 10000;
        
        final int MAX_ADD_LEN = 1000;
        final int MAX_REMOVE_LEN = 3;
        
        final int ITERATIONS = 10000;
        //-LEN to LEN
        int[] array = new int[2 * TOTAL_LEN + 1];
        
        Gaps gaps = new Gaps();
        
        Random r = new Random(0);
        for(int iter = 0; iter < ITERATIONS; ++iter) {
            int addLb = getRandom(r, -TOTAL_LEN, TOTAL_LEN);
            int addUb = getRandom(r, addLb, Math.min(addLb + MAX_ADD_LEN - 1, TOTAL_LEN));
            
            for(int i = addLb; i <= addUb; ++i) {
                array[i+TOTAL_LEN] = 1;                
            }
            gaps.mergeGap(addLb,addUb);
            
            int removeLb = getRandom(r, -TOTAL_LEN, TOTAL_LEN);
            int removeUb = getRandom(r, removeLb, Math.min(removeLb + MAX_REMOVE_LEN - 1, TOTAL_LEN));
            
            for(int i = removeLb; i <= removeUb; ++i) {
                array[i+TOTAL_LEN] = 0;                
            }
            
            gaps.removeGap(removeLb, removeUb);
            
            List<Gap> checkGaps = new ArrayList<>();
            boolean inGap = false;
            int lastGapStart = 0;
            for(int i = -TOTAL_LEN; i <= TOTAL_LEN; ++i) {
                int val = array[i+TOTAL_LEN];
                if (val == 1 && !inGap) {
                    lastGapStart = i;
                    inGap=true;
                    continue;
                } else if (val == 0 && inGap) {
                    checkGaps.add(new Gap(lastGapStart, i-1));
                    inGap = false;
                }
            }
            if (inGap == true) {
                checkGaps.add(new Gap(lastGapStart, TOTAL_LEN));
            }
            
            List<Gap> listGaps = gaps.getAllGaps();
            //log.debug("{}",listGaps);
            assertEquals(checkGaps.size(), listGaps.size());
            
            for(int i = 0; i < checkGaps.size(); ++i) {
                assertEquals(checkGaps.get(i), listGaps.get(i));
            }
            
        }
    }
}
