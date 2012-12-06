package codejam.y2009.football_team;

public class Node<DataType> {
    Node lhs;
    Node rhs;
    
    DataType data;

    public Node(DataType data) {
        super();
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString() + " child " + lhs + " branch " + rhs;
    }
    
}
