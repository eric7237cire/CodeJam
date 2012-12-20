package codejam.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import codejam.utils.utils.PermutationWithRepetition;

public class PermTest {
    @Test
    public void testPermsRepetition() {
        Integer[] possible = new Integer[] {3, 7, 12};
        
        Integer[] perm = new Integer[2];
        
        PermutationWithRepetition<Integer> pr = PermutationWithRepetition.create(possible, perm);
        
        pr.next();
        assertArrayEquals(new Integer[] {3,3}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {7,3}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {12,3}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {3,7}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {7,7}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {12,7}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {3,12}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {7,12}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {12,12}, perm);
        
        assertEquals(false, pr.hasNext());
    }
    
    @Test
    public void testSumProduct() {
        Integer[] possible = new Integer[] {2,3};
        
        int mod = 1000000007;
        
        int s = codejam.y2008.round_emea.bus_stops.Main.sumOfProductAllPermutationsBruteForce(possible,1,mod);
        
        assertEquals(5, s);
        
        s = codejam.y2008.round_emea.bus_stops.Main.sumOfProductAllPermutationsBruteForce(possible,2,mod);
        assertEquals(25, s);
        
        s = codejam.y2008.round_emea.bus_stops.Main.sumOfProductAllPermutations(possible, 2,mod);
        assertEquals(25, s);
        
        possible = new Integer[] {4, 0, 3, 27, 14, 18, 0, 4, 99, 17, 20, 30};
        for(int i = 1; i < 5; ++i) {
            int s1 = codejam.y2008.round_emea.bus_stops.Main.sumOfProductAllPermutationsBruteForce(possible,i,mod);
            int s2 = codejam.y2008.round_emea.bus_stops.Main.sumOfProductAllPermutations(possible, i,mod);
            assertEquals(s1,s2);
        }
        
        int a1 = codejam.y2008.round_emea.bus_stops.Main.sumOfProductAllPermutations(possible, 4,mod);
        int a2 = codejam.y2008.round_emea.bus_stops.Main.sumOfProductAllPermutations(possible, 8,mod);
        
        int a3 = (a1*a1) % mod;
    }
}
