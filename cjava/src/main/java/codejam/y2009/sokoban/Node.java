package codejam.y2009.sokoban;

import java.util.Set;

import codejam.utils.utils.Grid;

import com.google.common.base.Objects;

public class Node  {
    Set<Integer> boxes;
    int steps;
    Grid<SquareType> grid;
    boolean isConnected;
    
    

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

    

//    @Override
//    public String toString() {
//        List<Integer> boxesSorted = new ArrayList<>();
//        boxesSorted.addAll(boxes);
//        Collections.sort(boxesSorted);
//        return "Node [boxes=" + boxesSorted + ", steps=" + steps + ", grid=" + grid
//                + ", isConnected=" + isConnected + "]";
//    }
}
