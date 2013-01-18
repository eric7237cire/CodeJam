package codejam.y2010.round_final.city_tour;

import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       return new String[] {"sample.in"};
        //return new String[] {"B-small-practice.in"};
      // return new String[] {"B-small-practice.in", "B-large-practice.in"};
    }
    

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        
        in.connections = new int[in.N][in.N];
        
        in.edges = Lists.newArrayList();
        
        for(int i = 0; i < 3; ++i)
            for(int j = i+1; j < 3; ++j)
            {
        in.connections[i][j] = 1;
        in.connections[j][i] = 1;
            }
        
        in.edges.add(new ImmutablePair<>(0, 1));
        in.edges.add(new ImmutablePair<>(0, 2));
        in.edges.add(new ImmutablePair<>(1, 2));
        
        for(int n = 3; n < in.N; ++n) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
        
            in.edges.add(new ImmutablePair<>( Math.min(x,y)-1, Math.max(x,y)-1));
            
            in.connections[n][x-1] = 1;
            in.connections[n][y-1] = 1;
            in.connections[x-1][n] = 1;
            in.connections[y-1][n] = 1;
            
            
        }
        
        return in;
    }
    
  
    int best_so_far = 0;

    int best(int x, int y, int N, int[][] a) {
        int max_len = 2;
        int second_max_len = -1;
        
        log.debug("best x={}, y={} start", x+1, y+1);
        /*
         * Look for a third node that is directly connected with x and y
         * with a greater index.  This is to go down the tree decomposition.
         */
        for (int i = Math.max(x, y) + 1; i < N; i++) {
            /*
             * the intermediate node z.  Find a path that goes
             * x .... z and then z back up to y
             */
          if (a[x][i] * a[y][i] > 0) {
              log.debug("best x={}, y={} checking intermediate node z {}", x+1, y+1, i+1);
              /**
               * Because the way the tree decomposition works, these 2 paths do not intersect, each new node
               * is attached to 2...?
               */
              int lenXZ = best(x, i, N, a);
              int lenZY = best(y, i, N, a);
              
           //   log.debug("best x={}, y={} best path x={} -> z={} is {}", x+1, y+1, x+1, i+1, lenXZ);
           //   log.debug("best x={}, y={} best path z={} -> y={} is {}", x+1, y+1, i+1, y+1, lenZY);
              
            int len =  lenXZ + lenZY  - 1;
            if (len > max_len) {
              second_max_len = max_len;
              max_len = len;
              log.debug("best x={}, y={}  z={} Updated max_len.  xz {} zy {} - 1 = {}" +
              		" Now second max = {}, max = {}",
                      x+1, y+1, i+1, lenXZ, lenZY, len, second_max_len, max_len);
            } else if (len > second_max_len) {
              second_max_len = len;
              log.debug("best x={}, y={} Updated second max_len.  Now second max = {}, max = {}", x+1, y+1, second_max_len, max_len);
            }
          }
        }
        
        /**
         * Here we merge the 2 paths together.  One that contains x-y
         * and another that does not contain x-y (explaining the - 2)
         * 
         * We know they do not intersect because the lengths of the paths are not
         * the same.  Or perhaps it seems like one path goes down one end of
         * the tree decomposition and the other path goes.
         * 
         * In the tree decomposition, each "node" has three nodes, like a triangle.
         * Given we always look for nodes > i, we know we are going down different
         * paths in the decomp tree...
         */
        best_so_far = Math.max(max_len, best_so_far);
        best_so_far = Math.max(max_len + second_max_len - 2,
                               best_so_far);
        
        log.debug("best x={}, y={} done.  Best so far = {}", x+1, y+1, best_so_far);
        
        return max_len;
    }
    
        @Override
    public String handleCase(InputData in) {
            best_so_far = 0;
            
            for(Pair<Integer, Integer> edge : in.edges) {
                best(edge.getLeft(), edge.getRight(), in.N, in.connections);
            }
            

            return String.format("Case #%d: %d", in.testCase, best_so_far);
    
    }
}