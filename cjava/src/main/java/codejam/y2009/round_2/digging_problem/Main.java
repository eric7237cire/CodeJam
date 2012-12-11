package codejam.y2009.round_2.digging_problem;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;
import codejam.y2009.round_2.digging_problem.Node.Direction;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {
    
    
    
    int iterations;
    
    
    int[][][][] memo;
     
   
    
    public int[] findWalkableRange(int row, int col, InputData input) {
        return findWalkableRange(row, col, col, col, input);
    }
    public int[] findWalkableRange(int row, int col, int dugLeft, int dugRight, InputData input) {
    	

        final int rows = input.rows;
        final int cols = input.cols;
        final Grid<Boolean> grid = input.grid;
        
    	Preconditions.checkArgument(row < rows - 1);
    	Preconditions.checkArgument(col >= 0 && col < cols);
    	Preconditions.checkArgument(dugLeft >= 0 && dugLeft <= col);
    	Preconditions.checkArgument(dugRight < cols && dugRight >= col);
    	Preconditions.checkState(!grid.getEntry(row+1, col));
    	int right = col;
    	int left = col;
    	
    	while(true) {
    	    int next = right + 1;
    	    if (next >= cols) {
    	        break;
    	    }
    	    //space above
    	    if (next > dugRight && !grid.getEntry(row, next)) {
    	        break;
    	    }
    	    //space below
    	    if (grid.getEntry(row+1, next)) {
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
            if (next < dugLeft && !grid.getEntry(row, next)) {
                break;
            }
            //cannot walk on space below
            if (grid.getEntry(row+1, next)) {
                break;
            }
            
            left = next;
        }
    	
    	return new int[] { left, right };
    }
    
    Integer getFallRow(int row, int col, InputData input) {

        final int rows = input.rows;
        final Grid<Boolean> grid = input.grid;
        final int fallingDistance = input.fallingDistance;
        
    	int r = row;
    	
    	while(r < rows - 1) {
    		if (!grid.getEntry(r + 1, col) ) {
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
   
   
    Node getNewNodeAfterDigging(Node n, int initialPosition, int digEntryCol, InputData input) {
        Integer row = getFallRow(n.row + 1, digEntryCol, input);             
        
        if (row == null) {
            log.debug("Fall rp mortel");
            return null;
        }
                
        //If n+1, range is extented by what has been dug
        int[] range = null;
        
        if (row == n.row + 1) {
        	//only fell 1 row
        	range = new int[] { digEntryCol, initialPosition };
            
            Preconditions.checkState(range[0] == digEntryCol || range[1] == digEntryCol);
            
            return new Node(row, digEntryCol, initialPosition);
        } else {
        	//fell many rows
            return new Node(row, digEntryCol, digEntryCol);
        }                
        
        
    }
    
    private int getNewMin(int minCost, int costAddition, Node newNode, InputData input) {
    	if (newNode == null) {
    		return minCost;
    	}
        Integer toDig = getDepthOutOfCave(newNode, input);
        
        if (toDig < 0 || toDig == Integer.MAX_VALUE) {
            return minCost;
        }
        
        return Math.min(costAddition + toDig, minCost);
    }
    
    Integer getDepthOutOfCave(final Node n, InputData input) {
        

        final int rows = input.rows;
        final int cols = input.cols;
        final Grid<Boolean> grid = input.grid;
        
        int minCost = Integer.MAX_VALUE;
        
        if (n.row == rows - 1) {
            return 0;
        }
        
        if (memo[n.row][n.col][n.dugToCol][n.direction == Direction.RIGHT ? 1 : 0] >= 0) {
            return memo[n.row][n.col][n.dugToCol][n.direction == Direction.RIGHT ? 1 : 0];
        }
        
        ++iterations;
        
        int[] walkableRange = n.direction == Direction.RIGHT ?
        	findWalkableRange(n.row, n.col, n.col, n.dugToCol, input) :
        	findWalkableRange(n.row, n.col, n.dugToCol, n.col, input);
        
        //For each position that is reachable on foot		
    	for(int position = walkableRange[0]; position <= walkableRange[1]; ++position) {
    	    //Dig everything possible, need one square extra to the right before falling in
    		for(int rightPosition = position + 1; rightPosition <= walkableRange[1]; ++rightPosition) {
    		    		    
    			//p to rp - 1 dug ; fall in rp  - 1
    		    int digEntryCol = rightPosition - 1;
    		    
    		    Node newNode = getNewNodeAfterDigging(n, position, digEntryCol, input);
    		    
    		    int dug = 1 + Math.abs(digEntryCol - position);
    	        
    	        minCost = getNewMin(minCost, dug,  newNode, input);
    		}
    	   	
    	
            for(int leftPosition = position - 1; leftPosition >= walkableRange[0]; --leftPosition) {
            
                //p to lp + 1 dug ; fall in lp  + 1
                int digEntryCol = leftPosition + 1;
                
                Node newNode = getNewNodeAfterDigging(n, position, digEntryCol, input);
                
                int dug = 1 + Math.abs(digEntryCol - position);
                
                minCost = getNewMin(minCost, dug,  newNode, input);
            }
        }
    	
    	//Try walking to the right
    	if (walkableRange[1] < cols - 1 &&
    	        (grid.getEntry(n.row, walkableRange[1] + 1)
    	                || walkableRange[1] + 1 <= Math.max(n.dugToCol, n.col)
    	                )
    	        
    	        ){
    	    int col = walkableRange[1] + 1;
    	    Integer row = getFallRow(n.row + 1, col, input);   
    	    
    	    if (row != null) {
    	        if (row == rows - 1) {
    	        	memo[n.row][n.col][n.dugToCol][n.direction == Direction.RIGHT ? 1 : 0] = 0;
    	            return 0;
    	        }
    	        Preconditions.checkState(grid.getEntry(row, col));
    	        Preconditions.checkState(!grid.getEntry(row+1, col));
    	        //int[] range = findOpenRange(row, col);
    	        Node newNode = new Node(row, col, col);
    	        minCost = getNewMin(minCost, 0,  newNode, input);
    	        
    	        //newNode = new Node(row, col, range[1]);
    	        //minCost = getNewMin(minCost, 0,  newNode);
    	    }
    	}
    	
    	//and left
    	if (walkableRange[0] > 0 && (
    	        grid.getEntry(n.row, walkableRange[0] - 1)
    	        || walkableRange[0] - 1 >= Math.min(n.dugToCol, n.col)        
    	        )) {
            int col = walkableRange[0] - 1;
            Integer row = getFallRow(n.row + 1, col, input);   
            
            if (row != null) {
                if (row == rows - 1) {
                	memo[n.row][n.col][n.dugToCol][n.direction == Direction.RIGHT ? 1 : 0] = 0;
                    return 0;
                }
                Preconditions.checkState(grid.getEntry(row, col));
    	        Preconditions.checkState(!grid.getEntry(row+1, col));
                
                Node newNode = new Node(row, col, col);
    	        minCost = getNewMin(minCost, 0,  newNode, input);
            }
        }
    	
    	if (minCost == Integer.MAX_VALUE) {
    	    memo[n.row][n.col][n.dugToCol][n.direction == Direction.RIGHT ? 1 : 0] =Integer.MAX_VALUE;
    	    return Integer.MAX_VALUE;
    	}
    	
    	log.debug("Returning {} for node {}", minCost, n);
    	memo[n.row][n.col][n.dugToCol][n.direction == Direction.RIGHT ? 1 : 0] = minCost;
    	return minCost;
    }

    public Main() {
        super();

        iterations = 0;
    }

	final static Logger log = LoggerFactory.getLogger(Main.class);

   

    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);

        input.rows = scanner.nextInt();
        input.cols = scanner.nextInt();
        input.fallingDistance = scanner.nextInt();

        BiMap<Character, Boolean> mapping = HashBiMap.create(2);
        mapping.put('#', false);
        mapping.put('.', true);

        Grid<Boolean> grid = Grid.buildFromScanner(scanner, input.rows,
                input.cols, mapping, null);
        input.grid = grid;
        return input;

    }

    @Override
    public String handleCase(InputData data) {
        
        memo = new int[data.rows][data.cols][data.cols][2];
        for(int r=0; r<data.rows; ++r) {
            for(int c=0; c<data.cols; ++c) {
                for(int dc=0; dc<data.cols; ++dc) {
                    memo[r][c][dc][0] = -1;
                    memo[r][c][dc][1] = -1;
                }
            }
        }
        iterations=0;
        
        int[] range = findWalkableRange(0, 0, 0, 0, data);
        
        Node n = new Node(0, 0, range[1]);

        log.info("Starting case {}", data.testCase);
        Integer cost = getDepthOutOfCave(n, data);
        
        log.info("Finished Starting case {}.  Iterations {}", data.testCase, iterations);
        
        if (cost == Integer.MAX_VALUE) {
            return("Case #" + data.testCase + ": No");
        } else {
            return("Case #" + data.testCase + ": Yes " + cost);
        }
        
    }
}