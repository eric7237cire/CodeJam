package com.eric.codejam;

import com.google.common.collect.ComparisonChain;

public class Node implements Comparable<Node> {
	 int row;
     int left;
     int right;
     
     
     public Node(int level, int left, int right) {
		super();
		this.row = level;
		this.left = left;
		this.right = right;
	}


	/* (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
     @Override
     public int compareTo(Node o) {
         return ComparisonChain.start()
                 .compare(row, o.row)
                 .compare(left, o.left)
                 .compare(right,  o.right)
                 .result();
     }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Node [row=" + row + ", left=" + left + ", right=" + right + "]";
    }
     
     
}
