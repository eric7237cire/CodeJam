package codejam.y2009.sokoban;

public enum SquareType {
    Box,
    BoxOnGoal,
    Goal,
    Empty,
    Wall,
    Invalid;
    
    boolean isEmpty() {
        return this == Empty || this == Goal;
    }
    boolean isBox() {
        return this == Box || this == BoxOnGoal;
    }
    
    SquareType removeBox() {
        switch(this) {
        case Box:
            return Empty;
        case BoxOnGoal:
            return Goal;
            default:
        }
        
        return null;
    }
    
    SquareType addBox() {
        switch(this) {
        case Empty:
            return Box;
        case Goal:
            return BoxOnGoal;
            default:
        }
        
        return null;
    }
}
