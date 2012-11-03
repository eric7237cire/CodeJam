package com.eric.codejam.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;

public class Grid<SquareType> {
    
    private final int rows;
    private final int cols;
    
    private final List<SquareType> grid;
    
    private SquareType invalidSquare;
    
    final BiMap<Character, SquareType> mapping;
    
    //Builds character grid
    //..#
    //.O#
    //.OO
    public static<SquareType> Grid<SquareType>  buildFromScanner(Scanner scanner, int rows, int cols, final BiMap<Character, SquareType> mapping, SquareType invalidSq) {
        
        
        Grid<SquareType> g = new Grid<>(rows, cols, invalidSq, mapping);
        
        for (int r = 0; r < rows; ++r) {
            String rowStr = scanner.next();
            for(int c = 0; c < cols; ++c) {
                char ch = rowStr.charAt(c);
                g.grid.add(g.getIndex(r,c), mapping.get(ch) );
            }
            
        }
        
        return g;
    }
    
    public static<SquareType> Grid<SquareType>  buildEmptyGrid( int rows, int cols, SquareType invalidSq) {
        
        
        Grid<SquareType> g = new Grid<>(rows, cols, invalidSq,null);
        
        for (int r = 0; r < rows; ++r) {
            for(int c = 0; c < cols; ++c) {
                g.grid.add(g.getIndex(r,c), invalidSq );
            }            
        }
        
        return g;
    }
 
    public Grid(Grid<SquareType> rhs) {
        this.rows = rhs.rows;
        this.cols = rhs.cols;
        this.grid = new ArrayList<>(rhs.grid);
        this.invalidSquare = rhs.invalidSquare;
        this.mapping = rhs.mapping;
    }
    
    private Grid(int rows, int cols, SquareType invalidSq, BiMap<Character, SquareType> mapping) {
        super();
        this.rows = rows;
        this.cols = cols;
        this.invalidSquare = invalidSq;
        this.mapping = mapping;
        
        grid = new ArrayList<>(rows*cols);
        
    }
    
    
    private int getIndex(int row, int col) {
        return col + row * cols;
    }
    
    public Integer getIndex(int index, Direction dir) {
        int row = index / cols - dir.getDeltaY();
        int col = index % cols + dir.getDeltaX();
        
        if (row < 0 || row >= rows) {
            return null;
        }
        
        if (col < 0 || col >= cols) {
            return null;
        }
        
        return getIndex(row, col);
    }
    
    public Set<Integer> getIndexesOf(SquareType type) {
        Set<Integer> ret = new HashSet<>();
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
    
    public void setEntry(int index, SquareType square) {
        Preconditions.checkArgument(invalidSquare == null || square != null);
        
        grid.set(index, square);
    }
    
    public void setEntry(int row, int col, SquareType square) {
        setEntry(getIndex(row, col), square);
    }
    
    public SquareType getEntry(int row, int col, Direction dir) {
        return getEntry(getIndex(row,col), dir);
    }
    
    public SquareType getEntry(final int index) {
        return grid.get(index);
    }
    
    public SquareType getEntry(final int index, Direction dir) {
        int row = index / cols - dir.getDeltaY();
        int col = index % cols + dir.getDeltaX();
        
        if (row < 0 || row >= rows) {
            return invalidSquare;
        }
        
        if (col < 0 || col >= cols) {
            return invalidSquare;
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



    private List<SquareType> getGrid() {
        return grid;
    }


    public SquareType getInvalidSquare() {
        return invalidSquare;
    }


    public void setInvalidSquare(SquareType invalidSquare) {
        this.invalidSquare = invalidSquare;
    }

    private static int printWidth = 4; 
    private boolean yZeroOnTop = true;
    
    

    public boolean isyZeroOnTop() {
        return yZeroOnTop;
    }

    public void setyZeroOnTop(boolean yZeroOnTop) {
        this.yZeroOnTop = yZeroOnTop;
    }

    @Override
    public String toString() {
        StringBuffer gridStr = new StringBuffer();
        for(int rIdx=0; rIdx<rows; ++rIdx) {
            int r = rIdx;
            if (!yZeroOnTop) {
                r = rows - rIdx - 1;
            }
            int index = getIndex(r,0);
            gridStr.append(StringUtils.rightPad("" + index, 4));
            for(int c=0; c<cols; ++c) {
                if (mapping != null) {
                    gridStr.append(mapping.inverse().get(getEntry(r,c) ));
                } else {
                    String s = "";
                    Object o = getEntry(r,c);
                    if (o != null) {
                        s += o.toString();
                    }
                    s = StringUtils.rightPad(s,printWidth); 
                    gridStr.append( s );
                }
            
            }
            gridStr.append("\n");
        }
        return "Grid [rows=" + rows + ", cols=" + cols 
                + ", invalidSquare=" + invalidSquare + "]\n\n" + gridStr;
        
        
    }

    
    
}
