package codejam.y2009.round_3.football_team;

public class Node<DataType> {
    Node<DataType> lhs;
    Node<DataType> rhs;
    
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
