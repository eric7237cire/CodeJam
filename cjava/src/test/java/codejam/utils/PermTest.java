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
}
