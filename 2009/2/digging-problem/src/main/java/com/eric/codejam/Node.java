package com.eric.codejam;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class Node implements Comparable<Node> {
	 final int row;
	 final int col;
	 final int openLeft;
	 final int openRight;
     
     
     public Node(int row, int col, int left, int right) {
		super();
		this.col   = col;
		this.row = row;
		this.openLeft = left;
		this.openRight = right;
	}


	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node){
	        final Node other = (Node) obj;
	        return Objects.equal(col, other.col)
	            && Objects.equal(row, other.row)
	            && Objects.equal(openLeft, other.openLeft)
	            && Objects.equal(openRight, other.openRight);
	    } else{
	        return false;
	    }
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(col, row, openLeft, openRight);
	}


	/* (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
     @Override
     public int compareTo(Node o) {
         return ComparisonChain.start()
                 .compare(row, o.row)
                 .compare(col, o.col)
                 .compare(openLeft, o.openLeft)
                 .compare(openRight,  o.openRight)
                 .result();
     }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Node [row=" + row + ", col=" + col + ", openLeft=" + openLeft
                + ", openRight=" + openRight + "]";
    }


  
     
}
