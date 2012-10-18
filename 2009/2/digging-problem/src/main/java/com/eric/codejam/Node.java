package com.eric.codejam;

import com.google.common.collect.ComparisonChain;

public class Node implements Comparable<Node> {
	 int level;
     int left;
     int right;
     
     
     public Node(int level, int left, int right) {
		super();
		this.level = level;
		this.left = left;
		this.right = right;
	}


	/* (non-Javadoc)
      * @see java.lang.Comparable#compareTo(java.lang.Object)
      */
     @Override
     public int compareTo(Node o) {
         return ComparisonChain.start()
                 .compare(level, o.level)
                 .compare(left, o.left)
                 .compare(right,  o.right)
                 .result();
     }
}
