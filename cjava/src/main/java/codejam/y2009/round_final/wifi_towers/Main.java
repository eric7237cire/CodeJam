package codejam.y2009.round_final.wifi_towers;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.FlowEdge;
import codejam.utils.datastructures.FlowNetwork;
import codejam.utils.datastructures.FordFulkerson;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
     //  return new String[] {"sample.in"};
      //  return new String[] {"D-small-practice.in"};
        return new String[] {"D-small-practice.in", "D-large-practice.in"};
    }
    

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        

        /*
         * Each test case starts with the number of towers, n. 
         * The following n lines each contain 4 integers: x, y, r, s. 
         * They describe a tower at coordinates x, y having a range of r
         *  and a score (value of updating to the new protocol) of s
         */
        
        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        in.towerLoc = new PointInt[in.N];
        in.towerRange = new int[in.N];
        in.towerScore = new int[in.N];
                
        for(int t = 0; t < in.N; ++t) {
        in.towerLoc[t] = new PointInt(scanner.nextInt(), scanner.nextInt());
        in.towerRange[t] = scanner.nextInt();
        in.towerScore[t] = scanner.nextInt();
        }
        
        return in;
    }

    @Override
    public String handleCase(InputData in) {
        

        /*
         * To reduce the problem to an instance of MIN-CUT, we create a
         *  flow network as follows. Create a source vertex, 
         *  a sink vertex, and one vertex for each tower. 
         *  Suppose a tower has score s. If s > 0, create an
         *   edge from the vertex to the sink with capacity s.
         *    If s < 0, create an edge from the source to this vertex with capacity 
         *    |s|. Finally, for every edge in the connection graph, create
         *     a similar edge in the flow network with infinite capacity.
         *      The network has V + 2 = O(V) vertices and O(V + E) edges.

Now every finite cut in the graph represents a choice of towers 
- we choose every tower on the same side of the cut as the source.
 The infinite capacity edges enforce that the choice follows the
  given constraints (otherwise we get a cut of infinite weight).
   The edges from the source and to the sink penalize the solution
    appropriately for choosing towers with negative scores and for
     not choosing towers with positive scores. If the value of the 
     best cut is C, the answer is S - C, where S is the sum of positive tower scores.
         */
        
        FlowNetwork fn = new FlowNetwork(in.N + 2);
        int source = in.N;
        int sink = in.N + 1;
        
        int S = 0;
        
        for(int t = 0; t < in.N; ++t) {
            if (in.towerScore[t] > 0) {
                fn.addEdge(new FlowEdge(t, sink, in.towerScore[t]));
                S += in.towerScore[t];
            } else if (in.towerScore[t] < 0) {
                fn.addEdge(new FlowEdge(source, t, -in.towerScore[t]));
            }
        }
        
        int inf = 10000000;
        for(int t = 0; t < in.N; ++t) {
            for(int t2 = t+1; t2 < in.N; ++t2) {
                double d = in.towerLoc[t].distance(in.towerLoc[t2]);

                /**
                 * The one tricky thing here is that if tower 1 is in sending
                 * range of tower 2, the flow goes from tower 2.  This represents
                 * the ability of the 2nd tower to flow its cost of upgrading its
                 * tower.
                 */
                if (DoubleMath.fuzzyCompare(d, (double) in.towerRange[t], 1e-5) <= 0
                      //  && in.towerScore[t2] <= 0
                ) {
                    fn.addEdge(new FlowEdge(t2, t, inf));
                }

                if (DoubleMath.fuzzyCompare(d, (double) in.towerRange[t2], 1e-5) <= 0

                ) {
                    fn.addEdge(new FlowEdge(t, t2, inf));
                }
            }
        }
        
        /*
         * 17 16
case 44

0

0 0 1 0
2 3 1 1

2

1 0 1 -1
3 0 1 -3

4

0 2 1 2
0 1 1 1

6

3 2 1 -1
0 3 1 3

8

2 0 1 -2
1 3 1 2

10

3 3 1 1
3 1 1 -2

0:  0->5 0.0/1.0E7  0->2 0.0/1.0E7  
1:  1->10 0.0/1.0E7  1->9 0.0/1.0E7  1->13 0.0/1.0  
2:  2->8 0.0/1.0E7  2->0 0.0/1.0E7  
3:  3->11 0.0/1.0E7  3->8 0.0/1.0E7  
4:  4->7 0.0/1.0E7  4->5 0.0/1.0E7  4->13 0.0/2.0  
5:  5->4 0.0/1.0E7  5->0 0.0/1.0E7  5->13 0.0/1.0  
6:  6->11 0.0/1.0E7  6->10 0.0/1.0E7  
7:  7->9 0.0/1.0E7  7->4 0.0/1.0E7  7->13 0.0/3.0  
8:  8->3 0.0/1.0E7  8->2 0.0/1.0E7  
9:  9->7 0.0/1.0E7  9->1 0.0/1.0E7  9->13 0.0/2.0  
10:  10->6 0.0/1.0E7  10->1 0.0/1.0E7  10->13 0.0/1.0  
11:  11->6 0.0/1.0E7  11->3 0.0/1.0E7  
12:  12->11 0.0/2.0  12->8 0.0/2.0  12->6 0.0/1.0  12->3 0.0/3.0  12->2 0.0/1.0  
13:  


         */
        
        FordFulkerson ff = new FordFulkerson(fn, source, sink);
        
        int ans = S - (int) ff.value();
        
        return String.format("Case #%d: %d", in.testCase, ans);
    }
}