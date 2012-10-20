package com.eric.codejam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class Main {
    
    boolean [][] grid; //false #  true .
    int rows;
    int cols;
    int fallingDistance;
    
    Map<Node, Integer> minCostMap;
     
    int[] findOpenRange(int row, int col) {
    	return findOpenRange(row, col, col, col);
    }
    
    int[] findOpenRange(int row, int col, int dugLeft, int dugRight) {
        
        Preconditions.checkArgument(row < rows - 1);
        Preconditions.checkArgument(col >= 0 && col < cols);
        Preconditions.checkArgument(dugLeft >= 0 && dugLeft <= col);
        Preconditions.checkArgument(dugRight < cols && dugRight >= col);
        Preconditions.checkState(!grid[row+1][col]);
        int right = col;
        int left = col;
        
        while(true) {
            int next = right + 1;
            if (next >= cols) {
                break;
            }
            //space above
            if (next > dugRight && !grid[row][next]) {
                break;
            }
            
            
            right = next;
        }
                
        
        while(true) {
            int next = left - 1;
            if (next < 0) {
                break;
            }
            //space above
            if (next < dugLeft && !grid[row][next]) {
                break;
            }
            
            
            left = next;
        }
        
        return new int[] { left, right };
    }
    
    int[] findWalkableRange(int row, int col) {
        return findWalkableRange(row, col, col, col);
    }
    int[] findWalkableRange(int row, int col, int dugLeft, int dugRight) {
    	
    	Preconditions.checkArgument(row < rows - 1);
    	Preconditions.checkArgument(col >= 0 && col < cols);
    	Preconditions.checkArgument(dugLeft >= 0 && dugLeft <= col);
    	Preconditions.checkArgument(dugRight < cols && dugRight >= col);
    	Preconditions.checkState(!grid[row+1][col]);
    	int right = col;
    	int left = col;
    	
    	while(true) {
    	    int next = right + 1;
    	    if (next >= cols) {
    	        break;
    	    }
    	    //space above
    	    if (next > dugRight && !grid[row][next]) {
    	        break;
    	    }
    	    //space below
    	    if (grid[row+1][next]) {
    	        break;
    	    }
    	    
    	    right = next;
    	}
    		   	
    	
    	while(true) {
            int next = left - 1;
            if (next < 0) {
                break;
            }
            //space above
            if (next < dugLeft && !grid[row][next]) {
                break;
            }
            //cannot walk on space below
            if (grid[row+1][next]) {
                break;
            }
            
            left = next;
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
            return new Node(row, position, digEntryCol, digEntryCol);
        }
        
        //If n+1, range is extented by what has been dug
        int[] range = null;
        
        if (row == n.row + 1) {
            if (digEntryCol >= position) {
                range = findOpenRange(row, digEntryCol, position, digEntryCol);
            } else {
                range = findOpenRange(row, digEntryCol, digEntryCol, position);
            }
        } else {
            range = findOpenRange(row, digEntryCol, digEntryCol, digEntryCol);
        }
                
        Node newNode = new Node(row, digEntryCol, range[0], range[1]);
        
        return newNode;
    }
    
    private int getNewMin(int minCost, int costAddition, Node newNode) {
    	if (newNode == null) {
    		return minCost;
    	}
        Integer toDig = getDepthOutOfCave(newNode);
        
        if (toDig == null) {
            return minCost;
        }
        
        return Math.min(costAddition + toDig, minCost);
    }
    
    Integer getDepthOutOfCave(final Node n) {
        
        int minCost = Integer.MAX_VALUE;
        
        if (n.row == rows - 1) {
            return 0;
        }
        
        if (minCostMap.containsKey(n)) {
        	return minCostMap.get(n);
        }
        
        int[] walkableRange = findWalkableRange(n.row, n.col, n.openLeft, n.openRight);
        
    	for(int position = walkableRange[0]; position <= walkableRange[1]; ++position) {
    		for(int rightPosition = position + 1; rightPosition <= walkableRange[1]; ++rightPosition) {
    		
    		    
    			//p to rp - 1 dug ; fall in rp  - 1
    		    int digEntryCol = rightPosition - 1;
    		    
    		    Node newNode = getNewNodeAfterDigging(n, position, digEntryCol);
    		    
    		    int dug = 1 + Math.abs(digEntryCol - position);
    	        
    	        minCost = getNewMin(minCost, dug,  newNode);
    		}
    	   	
    	
            for(int leftPosition = position - 1; leftPosition >= walkableRange[0]; --leftPosition) {
            
                //p to lp + 1 dug ; fall in lp  + 1
                int digEntryCol = leftPosition + 1;
                
                Node newNode = getNewNodeAfterDigging(n, position, digEntryCol);
                
                int dug = 1 + Math.abs(digEntryCol - position);
                
                minCost = getNewMin(minCost, dug,  newNode);
            }
        }
    	
    	if (walkableRange[1] < cols - 1 &&
    	        (grid[n.row][walkableRange[1] + 1]
    	                || walkableRange[1] + 1 <= n.openRight
    	                )
    	        
    	        ){
    	    int col = walkableRange[1] + 1;
    	    Integer row = getFallRow(n.row + 1, col);   
    	    
    	    if (row != null) {
    	        if (row == rows - 1) {
    	        	minCostMap.put(n, 0);
    	            return 0;
    	        }
    	        Preconditions.checkState(grid[row][col]);
    	        Preconditions.checkState(!grid[row+1][col]);
    	        int[] range = findOpenRange(row, col);
    	        Node newNode = new Node(row, col, range[0], range[1]);
    	        minCost = getNewMin(minCost, 0,  newNode);
    	    }
    	}
    	
    	
    	if (walkableRange[0] > 0 && (
    	        grid[n.row][walkableRange[0] - 1]
    	        || walkableRange[0] - 1 >= n.openLeft        
    	        )) {
            int col = walkableRange[0] - 1;
            Integer row = getFallRow(n.row + 1, col);   
            
            if (row != null) {
                if (row == rows - 1) {
                	minCostMap.put(n, 0);
                    return 0;
                }
                Preconditions.checkState(grid[row][col]);
    	        Preconditions.checkState(!grid[row+1][col]);
                int[] range = findOpenRange(row, col);
                Node newNode = new Node(row, col, range[0], range[1]);
                minCost = getNewMin(minCost, 0,  newNode);
            }
        }
    	
    	if (minCost == Integer.MAX_VALUE) {
    		minCostMap.put(n, null);
    	    return null;
    	}
    	
    	log.debug("Returning {} for node {}", minCost, n);
    	minCostMap.put(n, minCost);
    	return minCost;
    }

    static Main buildMain(Scanner scanner) {
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
        return m;
    }

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

    	Main m = Main.buildMain(scanner);
        
       // scanner.useDelimiter(delim);
        
        int[] range = m.findOpenRange(0, 0, 0, 0);
        
        Node n = new Node(0, 0, range[0], range[1]);

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
    	minCostMap = new HashMap<>();
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