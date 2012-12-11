package codejam.y2009.round_2.digging_problem;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class Node implements Comparable<Node> {
	 final int row;
	 final int col;
	 final int dugToCol;
	 
	 enum Direction {
		 LEFT,
		 RIGHT;
	 }
	 final Direction direction;
     
     
     public Node(int row, int col, int dugToCol) {
		super();
		this.col   = col;
		this.row = row;
		this.dugToCol = dugToCol;
		
		if (this.dugToCol >= col) {
			this.direction = Direction.RIGHT;
		} else {
			this.direction = Direction.LEFT;
		}
		
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node){
	        final Node other = (Node) obj;
	        return Objects.equal(col, other.col)
	            && Objects.equal(row, other.row)
	            && Objects.equal(dugToCol, other.dugToCol)
	            && Objects.equal(direction, other.direction);
	    } else{
	        return false;
	    }
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(col, row, dugToCol, direction);
	}


	/* (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
     @Override
     public int compareTo(Node o) {
         return ComparisonChain.start()
                 .compare(row, o.row)
                 .compare(col, o.col)
                 .compare(dugToCol, o.dugToCol)
                 .compare(direction,  o.direction)
                 .result();
     }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Node [row=" + row + ", col=" + col + ", open=" + dugToCol
                + ", direction=" + direction + "]";
    }


  
     
}
