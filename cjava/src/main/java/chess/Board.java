package chess;

import codejam.utils.utils.GridChar;

public class Board {
    public GridChar grid;
    
    public Board() {
        grid = GridChar.buildEmptyGrid(8, 8, '.');
        grid.setyZeroOnTop(false);
    }
    
    public String toString2()
    {
        StringBuffer gridStr = new StringBuffer();
        gridStr.append("  a b c d e f g h\n");
        for (int rIdx = 0; rIdx < grid.getRows(); ++rIdx) {
            int r = 
                r = grid.getRows() - rIdx - 1;
            
            gridStr.append(1+r);
            gridStr.append(" |");

            for (int c = 0; c < grid.getCols(); ++c) {

                gridStr.append(grid.getEntry(r, c));
            }
            if (rIdx != grid.getRows() - 1)
                gridStr.append("\n");
        }
        return gridStr.toString();
    }
    
    public String toString()
    {
        StringBuffer gridStr = new StringBuffer();
        gridStr.append("   abcdefgh\n");
        for (int rIdx = 0; rIdx < grid.getRows(); ++rIdx) {
            int  
                r = grid.getRows() - rIdx - 1;
            
            gridStr.append(1+r);
            gridStr.append(" |");;

            for (int c = 0; c < grid.getCols(); ++c) {

                gridStr.append(grid.getEntry(r, c));
            }
            if (rIdx != grid.getRows() - 1)
                gridStr.append("\n");
        }
        return gridStr.toString();
    }
}
