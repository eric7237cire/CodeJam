package codejam.y2012.round_1B.tide;

import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.DoubleFormat;
import codejam.utils.utils.Grid;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
       //return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.H = scanner.nextInt();
        in.N = scanner.nextInt();
        in.M = scanner.nextInt();

        in.ceiling = Grid.buildEmptyGrid(in.N, in.M, -1);
        in.floor = Grid.buildEmptyGrid(in.N, in.M, -1);
        
        for(int r = 0; r < in.N; ++r) {
            for(int c = 0; c < in.M; ++c) {
                in.ceiling.setEntry(r,c,scanner.nextInt());
            }
        }
        for(int r = 0; r < in.N; ++r) {
            for(int c = 0; c < in.M; ++c) {
                in.floor.setEntry(r,c,scanner.nextInt());
            }
        }

        return in;
    }

    private static class Node implements Comparable<Node>{
        public Node(double time, int row, int col) {
            super();
            this.time = time;
            this.row = row;
            this.col = col;
        }
        double time;
        int row;
        int col;
        @Override
        public int compareTo(Node o) {
            return ComparisonChain.start().compare(time,o.time).compare(o.row, row).compare(o.col, col).result();
        }
        @Override
        public int hashCode() {
            return Objects.hashCode(row,col);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            return Objects.equal(col, other.col) &&
                Objects.equal(row, other.row) ;
        }
        @Override
        public String toString() {
            return "Node [time=" + time + ", row=" + row + ", col=" + col + "]";
        }
    }
    
    public String handleCase(InputData in) {

        PriorityQueue<Node> toVisit = new PriorityQueue<>();
        Set<Node> visited = Sets.newHashSet();
        
        toVisit.add(new Node(0,0,0));
        
        int iterations = 0;
        
        while(!toVisit.isEmpty()) {
            Node node = toVisit.poll();
        
            ++iterations;
            
            if (iterations % 1000000 == 0) {
                log.info("Iterations {} Node {}", iterations, node);
            }
            log.debug("Node {}", node);
            if (visited.contains(node))
                continue;
            
            visited.add(node);
            
            if (node.row == in.N - 1 && node.col == in.M - 1) {
                return String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(node.time));
            }
            
            int floorCur = in.floor.getEntry(node.row,node.col);
            int ceilCur = in.ceiling.getEntry(node.row, node.col);
            double waterLevel = Math.max( in.H - node.time * 10, 0);
            
            for(int dirIdx = 0; dirIdx < 4; ++dirIdx) {
                Direction dir = Direction.NORTH.turn(dirIdx * 2);
                int adjRow = node.row - dir.getDeltaY();
                int adjCol = node.col + dir.getDeltaX();
                
                int floorAdj = in.floor.getEntry(node.row,node.col, dir);
                int ceilAdj = in.ceiling.getEntry(node.row, node.col, dir);
                
                //The floor height of the adjacent square must be at least 50 centimeters below the ceiling height of your current square as well.
                if (ceilCur - floorAdj < 50)
                    continue;
                
                /*
                 * The water level, the floor height of your current square, and the floor height 
                 * of the adjacent square must all be at least 50 centimeters lower than the ceiling height of the adjacent square.
                 */
                
                if (ceilAdj - floorCur < 50)
                    continue;
                
                if (ceilAdj - floorAdj < 50)
                    continue;
                
                //At this point, only the waterlevel can prevent a movement
                
                //Handle pre tide moves
                if (ceilAdj - waterLevel >= 50 && node.time == 0) {
                    //Free move
                    toVisit.add(new Node(0, adjRow, adjCol));
                    continue;
                }
                
                double adjTime = node.time;
                double leavingWaterLevel = waterLevel;
                
                if (ceilAdj - waterLevel < 50) {
                    double waterLevelNeeded = ceilAdj - 50;
                    double waterLevelDiff = waterLevel - waterLevelNeeded;
                    //10 cm / sec * t = diff
                    double timeToWait = waterLevelDiff / 10;
                    adjTime += timeToWait;
                    leavingWaterLevel = Math.max( in.H - adjTime * 10, 0);
                }
                
                //Do we have to drag the kayak?
                if (leavingWaterLevel - floorCur >= 20) {
                    adjTime += 1;
                } else {
                    adjTime += 10;
                }
                
                toVisit.add(new Node(adjTime, adjRow, adjCol));
                 
            }
        }
        
        return "Impossible";
    }

}
