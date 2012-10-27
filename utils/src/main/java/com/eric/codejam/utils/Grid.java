package com.eric.codejam.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public class Grid<SquareType> {
    private final int rows;
    private final int cols;
    
    List<SquareType> grid;
    
    public Grid(int rows, int cols, SquareType defaultSq) {
        super();
        this.rows = rows;
        this.cols = cols;
        
        grid = new ArrayList<>(rows*cols);
        
        for(int r = 0; r < rows; ++r) {
            for(int c = 0; c < cols; ++c) {
                grid.add(defaultSq);
            }
        }
    }
    
    private int getIndex(int row, int col) {
        return col + row * cols;
    }
    
    public void setRow(int row, String rowStr, ConvertSquare<SquareType> converter) {
        Preconditions.checkArgument(rowStr.length() == cols);
        for(int c = 0; c < cols; ++c) {
            char ch = rowStr.charAt(c);
            grid.set(getIndex(row,c), converter.convert(ch) );
        }
    }
    
    public SquareType getEntry(int row, int col) {
        Preconditions.checkArgument(row >= 0 && row < rows);
        Preconditions.checkArgument(col >= 0 && col < cols);
        
        return grid.get(getIndex(row,col));
    }
    
    public interface ConvertSquare<SquareType> {
        SquareType convert(Character c);
    }
    
    
}
