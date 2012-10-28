package com.eric.codejam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Objects;

public class Node  {
    Set<Integer> boxes;
    int steps;
    Grid<SquareType> grid;
    boolean isConnected;
    
    public static class PriorityCompare implements Comparator<Node> {
        

        @Override
        public int compare(Node o1, Node o2) {
            // TODO Auto-generated method stub
            return Integer.compare(o1.steps,o2.steps);
        }
        
    }

    @Override
    public int hashCode() {
        
        return Objects.hashCode(boxes, isConnected);
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
        return Objects.equal(boxes, other.boxes) && Objects.equal(isConnected,  other.isConnected);
    }

    public Node(Set<Integer> boxes, int steps, Grid<SquareType> grid) {
        super();
        this.boxes = boxes;
        this.steps = steps;
        this.grid = grid;
        isConnected = true;
    }

    /*
    public int compareTo(Node o) {
        return ComparisonChain.start().compare(steps, o.steps)
                .compareFalseFirst(mustBeConnectedThisTurn, o.mustBeConnectedThisTurn)
                .compare(StringUtils.join(boxes, ","),  StringUtils.join(o.boxes, ","))
                .result();
    }*/

    @Override
    public String toString() {
        List<Integer> boxesSorted = new ArrayList<>();
        boxesSorted.addAll(boxes);
        Collections.sort(boxesSorted);
        return "Node [boxes=" + boxesSorted + ", steps=" + steps + ", grid=" + grid
                + ", isConnected=" + isConnected + "]";
    }
}
