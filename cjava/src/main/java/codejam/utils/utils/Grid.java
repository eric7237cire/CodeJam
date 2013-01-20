package codejam.utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;

public class Grid<SquareType> {
    
    final static Logger log = LoggerFactory.getLogger(Grid.class);
    
    private int rows;
    private final int cols;
    
    public final int maxIndex;
    
    public final Direction[] directions = new Direction[] { Direction.NORTH, 
            Direction.EAST, Direction.SOUTH, Direction.WEST };
    
    private final List<SquareType> grid;
    
    private SquareType invalidSquare;
    
    final BiMap<Character, SquareType> mapping;
    
    public final static FromScanner<Integer> fromScannerInt = new FromScanner<Integer>() {

        @Override
        public Integer getFromScanner(Scanner scanner) {
            return scanner.nextInt();
        }
        
    };
    
    public final static FromScanner<Long> fromScannerLong = new FromScanner<Long>() {

        @Override
        public Long getFromScanner(Scanner scanner) {
            return scanner.nextLong();
        }
        
    };

    //Builds character grid
    //..#
    //.O#
    //.OO
    public static<SquareType> Grid<SquareType>  buildFromScanner(Scanner scanner, int rows, int cols, 
            final BiMap<Character, SquareType> mapping, SquareType invalidSq) {
        
        
        
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
    
    public static<SquareType> Grid<SquareType>  buildFromScanner(Scanner scanner, int rows, int cols, 
            final FromScanner<SquareType> reader, SquareType invalidSq) {
        
        
        
        Grid<SquareType> g = new Grid<>(rows, cols, invalidSq, null);
        
        for (int r = 0; r < rows; ++r) {
            
            
            for(int c = 0; c < cols; ++c) {
                SquareType sq = reader.getFromScanner(scanner);
                g.grid.add(g.getIndex(r,c), sq );
            }            
        }
        
        return g;
    }
    
    public static interface Converter<SquareType> {
        SquareType convert(char c);
    }
    
    public static interface FromScanner<SquareType> {
        SquareType getFromScanner(Scanner scanner);
    }
    
    public static<SquareType> Grid<SquareType>  buildFromBufferedReader(BufferedReader br, int rows, int cols, final BiMap<Character, SquareType> mapping, SquareType invalidSq) {
        
        
        Grid<SquareType> g = new Grid<>(rows, cols, invalidSq, mapping);
        
        for (int r = 0; r < rows; ++r) {
            try {
            String rowStr = br.readLine();
           // log.debug(rowStr);
            for(int c = 0; c < cols; ++c) {
                char ch = rowStr.charAt(c);
                
                g.grid.add(g.getIndex(r,c), mapping.get(ch) );
               
            }
            } catch (IOException ex) {
                log.error("ex", ex);
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
        this.maxIndex = rhs.maxIndex;
        this.invalidSquare = rhs.invalidSquare;
        this.mapping = rhs.mapping;
    }
    
    public void addRow(SquareType valueDefault) {
        ++rows;
        for(int c = 0; c < cols; ++c) {
            this.grid.add(valueDefault);
        }
    }
    
    private Grid(int rows, int cols, SquareType invalidSq, BiMap<Character, SquareType> mapping) {
        super();
        this.rows = rows;
        this.cols = cols;
        this.invalidSquare = invalidSq;
        this.mapping = mapping;
        this.maxIndex = rows*cols - 1;
        grid = new ArrayList<>(maxIndex + 1);
        
    
        
    }
    
    /**
     * 
     * @return total number of cells
     */
    public int getSize() {
        return grid.size();
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    
    public int getIndex(int row, int col) {
        return col + row * cols;
    }
    
    public int[] getRowCol(int index) {
        return new int[] { index / cols,  index % cols };
    }
    
    public int minDistanceToEdge(int index) {
        int row = index / cols;
        int col = index % cols;
        return Collections.min( Arrays.asList( row - 0, rows - 1 - row, col - 0, cols - 1 - col));
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cols;
        
        result = prime * result + ((grid == null) ? 0 : grid.hashCode());
        result = prime * result + ((invalidSquare == null) ? 0 : invalidSquare.hashCode());
        
        result = prime * result + rows;
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Grid other = (Grid) obj;
        if (cols != other.cols)
            return false;
        
        return Objects.equal(rows, other.rows)
                && Objects.equal(cols, other.cols) &&
                Objects.equal(grid, other.grid) &&
                Objects.equal(invalidSquare, other.invalidSquare);
                
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
        if (index < 0 || index > maxIndex)
            return invalidSquare;
        
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


/*
    private List<SquareType> getGrid() {
        return grid;
    }
*/

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
            String indexStr = "Idx " + index ;
            gridStr.append(StringUtils.rightPad("" + indexStr, 6) + " | ");
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
