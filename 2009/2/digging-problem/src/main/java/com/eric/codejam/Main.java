package com.eric.codejam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class Main {
    
    boolean [][] grid; //false #  true .
    int rows;
    int cols;
    int fallingDistance;
        
    int[] findRange(int row, int col) {
        return findRange(row, col, col, col);
    }
    int[] findRange(int row, int col, int initialLeft, int initialRight) {
    	
    	Preconditions.checkArgument(row < rows - 1);
    	Preconditions.checkArgument(col >= 0 && col < cols);
    	Preconditions.checkArgument(initialLeft >= 0 && initialLeft <= col);
    	Preconditions.checkArgument(initialRight < cols && initialRight >= col);
    	
    	int right = initialRight;
    	int left = initialLeft;
    	
    	for( ; right < cols - 1 && grid[row][right+1]; ++right) {
    		
    	}
    	
    	for( ; left > 0  && grid[row][left-1]; --left) {
    		
    	}
    	
    	return new int[] { left, right };
    }
    
    Integer getFallRow(int row, int col) {
    	int r = row;
    	
    	while(r < rows - 1) {
    		if (!grid[r + 1][col] ) {
    			break;
    		}
    		++ r;
    	}
    	
    	int diff = r - row + 1;
    	if (diff > fallingDistance) {
    		return null;
    	}
    	
    	return r;
    }
   
   
    Node getNewNodeAfterDigging(Node n, int position, int digEntryCol) {
        Integer row = getFallRow(n.row + 1, digEntryCol);             
        
        if (row == null) {
            log.debug("Fall rp mortel");
            return null;
        }
        
        if (row == rows - 1) {
            return new Node(row, digEntryCol, digEntryCol);
        }
        
        //If n+1, range is extented by what has been dug
        int[] range = null;
        
        if (row == n.row + 1) {
            if (digEntryCol >= position) {
                range = findRange(row, digEntryCol, position, digEntryCol);
            } else {
                range = findRange(row, digEntryCol, digEntryCol, position);
            }
        } else {
            range = findRange(row, digEntryCol);
        }
                
        Node newNode = new Node(row, range[0], range[1]);
        
        return newNode;
    }
    Integer getDepthOutOfCave(Node n) {
        
        int minCost = Integer.MAX_VALUE;
        
        if (n.row == rows - 1) {
            return 0;
        }
        
    	for(int position = n.left; position <= n.right; ++position) {
    		for(int rightPosition = position + 1; rightPosition <= n.right; ++rightPosition) {
    		
    		    
    			//p to rp - 1 dug ; fall in rp  - 1
    		    int digEntryCol = rightPosition - 1;
    		    
    		    Node newNode = getNewNodeAfterDigging(n, position, digEntryCol);
    		    
    		    if (newNode == null) {
    		        continue;
    		    }
    		    
    		    int dug = 1 + Math.abs(digEntryCol - position);
    	        
    	        Integer toDig = getDepthOutOfCave(newNode);
    	        
    	        if (toDig == null) {
    	            continue;
    	        }
    	        
    	        minCost =  Math.min(dug + toDig, minCost);
    		}
    	   	
    	
            for(int leftPosition = position - 1; leftPosition >= n.left; --leftPosition) {
            
                //p to lp + 1 dug ; fall in lp  + 1
                int digEntryCol = leftPosition + 1;
                
                Node newNode = getNewNodeAfterDigging(n, position, digEntryCol);
                
                if (newNode == null) {
                    continue;
                }
                
                int dug = 1 + Math.abs(digEntryCol - position);
                
                Integer toDig = getDepthOutOfCave(newNode);
                
                if (toDig == null) {
                    continue;
                }
                
                minCost =  Math.min(dug + toDig, minCost);
            }
        }
    	
    	if (n.right < cols - 1 && grid[n.row][n.right + 1]) {
    	    int col = n.right + 1;
    	    Integer row = getFallRow(n.row + 1, col);   
    	    
    	    if (row != null) {
    	        int[] range = findRange(row, col);
    	        Node newNode = new Node(row, range[0], range[1]);
    	        int cost = getDepthOutOfCave(newNode);
    	        minCost = Math.min(cost, minCost);
    	    }
    	}
    	
    	
    	if (n.left > 0 && grid[n.row][n.left - 1]) {
            int col = n.left - 1;
            Integer row = getFallRow(n.row + 1, col);   
            
            if (row != null) {
                int[] range = findRange(row, col);
                Node newNode = new Node(row, range[0], range[1]);
                int cost = getDepthOutOfCave(newNode);
                minCost = Math.min(cost, minCost);
            }
        }
    	
    	if (minCost == Integer.MAX_VALUE) {
    	    return null;
    	}
    	
    	return minCost;
    }


    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

    	Main m = new Main();
        m.rows = scanner.nextInt();
        m.cols = scanner.nextInt();
        m.fallingDistance = scanner.nextInt();
        
        m.grid = new boolean[m.rows][m.cols];

        //Pattern delim = scanner.delimiter();
        //scanner.useDelimiter("");

        for (int r = 0; r < m.rows; ++r) {
            String rowStr = scanner.next();
            for(int c = 0; c < m.cols; ++c) {
            	char ch = rowStr.charAt(c);
            	if (ch == '#') {
            		m.grid[r][c] = false;
            	} else {
            		m.grid[r][c] = true;
            	}
            }
        }
        
       // scanner.useDelimiter(delim);
        
        int[] range = m.findRange(0, 0);
        
        Node n = new Node(0, range[0], range[1]);

        //log.debug("Grid {}", grid);
        Integer cost = m.getDepthOutOfCave(n);
        
        //log.info("Real Case #" + caseNumber + ": " + cost);
        if (cost == null) {
            os.println("Case #" + caseNumber + ": No");
        } else {
            os.println("Case #" + caseNumber + ": Yes " + cost);
        }
    }

    Main() {

    }

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

        OutputStream os = new FileOutputStream("output.txt");

        PrintStream pos = new PrintStream(os);

        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, pos);

        }

        scanner.close();
    }
}