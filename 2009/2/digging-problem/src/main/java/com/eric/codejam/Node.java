package com.eric.codejam;

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
