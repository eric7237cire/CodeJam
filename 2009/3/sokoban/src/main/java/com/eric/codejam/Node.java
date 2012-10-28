package com.eric.codejam;

import java.util.List;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class Node implements Comparable<Node> {
    List<Integer> boxes;
    int steps;
    Grid<SquareType> grid;
    boolean mustBeConnectedThisTurn;

    @Override
    public int hashCode() {
        
        return Objects.hashCode(boxes, steps);
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
        return Objects.equal(boxes, other.boxes) && Objects.equal(steps, other.steps) && Objects.equal(mustBeConnectedThisTurn,  other.mustBeConnectedThisTurn);
    }

    public Node(List<Integer> boxes, int steps, Grid<SquareType> grid) {
        super();
        this.boxes = boxes;
        this.steps = steps;
        this.grid = grid;
        mustBeConnectedThisTurn = false;
    }

    @Override
    public int compareTo(Node o) {
        return ComparisonChain.start().compare(steps, o.steps).compareFalseFirst(mustBeConnectedThisTurn, o.mustBeConnectedThisTurn).result();
    }

    @Override
    public String toString() {
        return "Node [boxes=" + boxes + ", steps=" + steps + ", grid=" + grid
                + ", mustBeConnectedThisTurn=" + mustBeConnectedThisTurn + "]";
    }
}
