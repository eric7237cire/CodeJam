package com.eric.codejam.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class Grid<SquareType> {
    
    private final int rows;
    private final int cols;
    
    final List<SquareType> grid;
    
    public static<SquareType> Grid<SquareType>  buildFromScanner(Scanner scanner, int rows, int cols, final Map<Character, SquareType> mapping) {
        
        Grid<SquareType> g = new Grid<>(rows, cols);
        for (int r = 0; r < rows; ++r) {
            String rowStr = scanner.next();
            for(int c = 0; c < cols; ++c) {
                char ch = rowStr.charAt(c);
                g.grid.add(g.getIndex(r,c), mapping.get(ch) );
            }
            
        }
        
        return g;
    }
    
 
    public Grid(Grid<SquareType> rhs) {
        this.rows = rhs.rows;
        this.cols = rhs.cols;
        this.grid = new ArrayList<>(rhs.grid);   
    }
    
    private Grid(int rows, int cols) {
        super();
        this.rows = rows;
        this.cols = cols;
        
        
        grid = new ArrayList<>(rows*cols);
        
    }
    
    public Grid(int rows, int cols, SquareType defaultSq) {
        this(rows, cols);
                
        for(int r = 0; r < rows; ++r) {
            for(int c = 0; c < cols; ++c) {
                grid.add(defaultSq);
            }
        }
    }
    
    private int getIndex(int row, int col) {
        return col + row * cols;
    }
    
    public Integer getIndex(int index, Direction dir) {
        int row = index % cols - dir.getDeltaY();
        int col = index / cols + dir.getDeltaX();
        
        if (row < 0 || row >= rows) {
            return null;
        }
        
        if (col < 0 || col >= cols) {
            return null;
        }
        
        return getIndex(row, col);
    }
    
    public List<Integer> getIndexesOf(SquareType type) {
        List<Integer> ret = new ArrayList<>();
        for(int i = 0; i < grid.size(); ++i) {
            if (Objects.equal(type, grid.get(i))) {
                ret.add(i);
            }
        }
        return ret;
    }
    
    public void setRow(int row, String rowStr, ConvertSquare<SquareType> converter) {
        Preconditions.checkArgument(rowStr.length() == cols);
        for(int c = 0; c < cols; ++c) {
            char ch = rowStr.charAt(c);
            grid.set(getIndex(row,c), converter.convert(ch) );
        }
    }
    
    public SquareType getEntry(int row, int col, Direction dir) {
        return getEntry(getIndex(row,col), dir);
    }
    
    public SquareType getEntry(final int index, Direction dir) {
        int row = index % cols - dir.getDeltaY();
        int col = index / cols + dir.getDeltaX();
        
        if (row < 0 || row >= rows) {
            return null;
        }
        
        if (col < 0 || col >= cols) {
            return null;
        }
        
        return getEntry(row,col);
        
    }
    
    public SquareType getEntry(int row, int col) {
        Preconditions.checkArgument(row >= 0 && row < rows);
        Preconditions.checkArgument(col >= 0 && col < cols);
        
        return grid.get(getIndex(row,col));
    }
    
    
    
    public interface ConvertSquare<SquareType> {
        SquareType convert(Character c);
    }



    public List<SquareType> getGrid() {
        return grid;
    }

    
    
}
