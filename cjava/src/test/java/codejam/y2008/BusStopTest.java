package codejam.y2008;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.Preconditions;

import codejam.y2008.round_emea.bus_stops.Main;

public class BusStopTest {

    @Test
    public void testBruteForce() {
      int[] start = new int[] { 1,1,1,0 };
      int[] current = Arrays.copyOf(start, 4);
      int[] end = new int[] {0, 1,1,1 };
      
      int c = Main.countBruteForce(start, end, current, 3);
      Preconditions.checkState(c == 1);
      
      start = new int[] { 1,1,1,0,0,0,0,0 };
      current = Arrays.copyOf(start, start.length);
      end = new int[] {0,0,0,0,0, 1,1,1 };
      
      c = Main.countBruteForce(start, end, current, 3);
      Preconditions.checkState(c == 1);
      
      c = Main.countBruteForce(start, end, current, 4);
      Preconditions.checkState(c == 13);
      
      c = Main.countBruteForce(start, end, current, 5);
      Preconditions.checkState(c == 31);
      
      start = new int[] { 1,1,1,0,0,0,0,0,0,0,0,0,0 };
      current = Arrays.copyOf(start, start.length);
      end = new int[] {0,0,0,0,0,0,0,0,0,0, 1,1,1 };
      
      c = Main.countBruteForce(start, end, current, 10);
      assertEquals(12355, c);
      
      start = new int[] { 1,1,1,1,1,0,0,0,0,0,0,0,0,0,0 };
      current = Arrays.copyOf(start, start.length);
      end = new int[] {0,0,0,0,0,0,0,0,0,0,1,1,1,1,1 };
      
      c = Main.countBruteForce(start, end, current, 10);
      assertEquals(14576, c % 30031);
      
      start = new int[] { 1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0 };
      current = Arrays.copyOf(start, start.length);
      end = new int[] {0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1 };
      
      c = Main.countBruteForce(start, end, current, 10);
      assertEquals(12893, c % 30031);
    }

}
