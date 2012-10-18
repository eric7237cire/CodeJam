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
    	
    	Preconditions.checkArgument(row < rows - 1);
    	Preconditions.checkArgument(col >= 0 && col < cols);
    	int right = col;
    	int left = col;
    	
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
   
    Integer getDepthOutOfCave(Node n) {
    	for(int position = n.left; position <= n.right; ++position) {
    		for(int rightPosition = position + 1; rightPosition <= n.right; ++rightPosition) {
    		
    			//p to rp dug
    		}
    	}
    }


    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

    	Main m = new Main();
        m.rows = scanner.nextInt();
        m.cols = scanner.nextInt();
        m.fallingDistance = scanner.nextInt();
        
        m.grid = new boolean[m.rows][m.cols];

        Pattern delim = scanner.delimiter();
        scanner.useDelimiter("");

        for (int r = 0; r < m.rows; ++r) {
            for(int c = 0; c < m.cols; ++c) {
            	char ch = scanner.next().charAt(0);
            	if (ch == '#') {
            		m.grid[r][c] = false;
            	} else {
            		m.grid[r][c] = true;
            	}
            }
        }
        
        scanner.useDelimiter(delim);
        
        int[] range = m.findRange(0, 0);
        
        Node n = new Node(0, range[0], range[1]);

        //log.debug("Grid {}", grid);
        Integer cost = m.getDepthOutOfCave(n);
        
        log.info("Real Case #" + caseNumber + ": " + cost);
        os.println("Case #" + caseNumber + ": " + cost);
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