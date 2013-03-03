package codejam.y2010.round_final.city_tour;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class CityTour extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    public CityTour()
    {
        super("B", 0, 0);
        //(( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
        

    @Override
    public InputData readInput(Scanner scanner, int testCase) 
    {

        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();

        in.connections = new boolean[in.N][in.N];

        in.edges = Lists.newArrayList();

        for (int i = 0; i < 3; ++i)
            for (int j = i + 1; j < 3; ++j) {
                in.connections[i][j] = true;
                in.connections[j][i] = true;
            }

        //Three intial cities are connected
        in.edges.add(new ImmutablePair<>(0, 1));
        in.edges.add(new ImmutablePair<>(0, 2));
        in.edges.add(new ImmutablePair<>(1, 2));

        for (int n = 3; n < in.N; ++n) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            in.edges.add(new ImmutablePair<>(Math.min(x, y) - 1,
                    Math.max(x, y) - 1));

            in.connections[n][x - 1] = true;
            in.connections[n][y - 1] = true;
            in.connections[x - 1][n] = true;
            in.connections[y - 1][n] = true;

        }

        return in;
    }
    
  
    int best_so_far = 0;
    
    static int indentLevel = 0;

    int best(int x, int y, int N, boolean[][] connected) {
        ++indentLevel;
        
        int max_len = 2;
        int second_max_len = -1;

        String indent = StringUtils.repeat(' ', 4 * indentLevel - 4);
        
        log.debug("{}best x={}, y={} start", indent, x + 1, y + 1);
        /*
         * Look for a third node that is directly connected with x and y with a
         * greater index. This is to go down the tree decomposition.
         * 
         * The "tree composition node" of x, y, z is the root of a sub tree
         */
        for (int z = Math.max(x, y) + 1; z < N; z++) {
            /*
             * the intermediate node z. Find a path that goes x .... z and then
             * z back up to y
             */
            if (!connected[x][z] || !connected[y][z]) {
                continue;
            }
            
            log.debug("{}best x={}, y={} checking intermediate node z {}",
                    indent,
                    x + 1, y + 1, z + 1);
            /**
             * Because the way the tree decomposition works, these 2 paths
             * do not intersect, 
             * 
             *        [ x y z]
             *        /       \
             *    [x z z']    [y z z']
             *    
             *    Find the best of the 2 forks down the tree
             */
            int lenXZ = best(x, z, N, connected);
            int lenZY = best(y, z, N, connected);

            // log.debug("best x={}, y={} best path x={} -> z={} is {}",
            // x+1, y+1, x+1, i+1, lenXZ);
            // log.debug("best x={}, y={} best path z={} -> y={} is {}",
            // x+1, y+1, i+1, y+1, lenZY);

            int len = lenXZ + lenZY - 1;
            
            //Keep track of best 2 paths going through x and y
            if (len > max_len) {
                second_max_len = max_len;
                max_len = len;
                log.debug(
                        "{}best x={}, y={}  z={} Updated max_len.  xz {} zy {} - 1 = {}"
                                + " Now second max = {}, max = {}", indent, x + 1,
                        y + 1, z + 1, lenXZ, lenZY, len, second_max_len,
                        max_len);
            } else if (len > second_max_len) {
                second_max_len = len;
                log.debug(
                        "{}best x={}, y={} Updated second max_len.  Now second max = {}, max = {}",
                        indent,
                        x + 1, y + 1, second_max_len, max_len);
            }
        
        }

        /**
         * Here we merge the 2 paths together. One that contains x-y and another
         * that does not contain x-y (explaining the - 2)
         * 
         * We know they do not intersect because the lengths of the paths are
         * not the same. Or perhaps it seems like one path goes down one end of
         * the tree decomposition and the other path goes.
         * 
         * In the tree decomposition, each "node" has three nodes, like a
         * triangle. Given we always look for nodes > i, we know we are going
         * down different paths in the decomp tree...
         */

        best_so_far = Math.max(max_len + second_max_len - 2, best_so_far);

        log.debug("{}best x={}, y={} done.  Best so far = {}. Return {} 2nd best {} ", 
                indent, x + 1, y + 1,
                best_so_far,
                max_len, second_max_len);

        --indentLevel;
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