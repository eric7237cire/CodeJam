package chess;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

import chess.parsing.Fen;
import chess.parsing.Ply;
import codejam.utils.utils.GridChar;

public class Board {
    public GridChar grid;
    
    boolean whiteToMove;
    boolean whiteCastleKingside;
    boolean whiteCastleQueenside;
    boolean blackCastleKingside;
    boolean blackCastleQueenside;
    
    int moveNumber;
    
    int enPassantSquare;
    
    public Board() {
        grid = GridChar.buildEmptyGrid(8, 8, '.');
        grid.setyZeroOnTop(false);
        
        enPassantSquare = -1;
    }
    
    public Board(String fenStr) {
        
        this();
            
            //Index of square on board (0-63)
            int fenIdx = 0;
            //Index in string
            int cIdx = 0;
            for(cIdx = 0; cIdx < fenStr.length(); ++cIdx)
            {
                if (fenIdx >= 64)
                    break;
                int rank = 7 - fenIdx / 8;
                int file =  fenIdx % 8;
                
                char c = fenStr.charAt(cIdx);
                
               // log.debug("Char {} fenIdx {} rank {} file {}", c,
                //        fenIdx, rank+1, (char) ('a' + file));
                        
                if (c == '/')
                {
                    Preconditions.checkState(file == 0);
                    continue;
                }
                
                if (Character.isDigit(c)) 
                {
                    fenIdx += Character.digit(c, 10);
                    continue;
                }
                
                grid.setEntry(rank, file, c);
                ++fenIdx;
                
                //log.debug("\n" + b.toString());
            }
            
            fenStr  = fenStr.substring(cIdx).trim();
            
            Iterator<String> tokens = Splitter.on(Pattern.compile("\\s+")).split(fenStr).iterator();
            
            setWhiteToMove(tokens.next().equals("w"));
            
            String castle = tokens.next();
            String ep = tokens.next();
            if (ep.equals("-")) {
                setEnPassantSquare(-1);
            } else {
                Preconditions.checkState(ep.length() == 2);
                setEnPassantSquare(grid.getIndex(ep.charAt(1) - '1', ep.charAt(0) - 'a'));
            }
            String lastPawnMoveOrCap = tokens.next();
            String moveNum = tokens.next();
            //w KQkq - 0 1
            
           
        
    }
    
    public static String initialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    public Board makeMove(Ply ply) {
        Board b = new Board();
        b.grid = new GridChar(this.grid);
        
        switch(ply.getMovedPiece().getPiece())
        {
        case Pawn:
            b.movePawn(ply);
            
            break;
            default:
                Preconditions.checkState(false);
        }
        
        return b;
    }
    
    private void movePawn(Ply ply)
    {
        int rankDir = ply.isWhiteMove() ? 1 : -1;
        int initialTwoMovesRank = ply.isWhiteMove() ? 3 : 4;
        
        int sourceRank = -1;
        int sourceFile = -1;
        
        if (!ply.isCapture()) {
            
            Preconditions.checkState(ply.getSourceRank() == -1);
            Preconditions.checkState(ply.getSourceFile() == -1);
            
            //square is empty
            Preconditions.checkState(grid.getInvalidSquare() == grid.getEntry(ply.getTargetRank(), ply.getTargetFile()));
            
            if (ply.getTargetRank() == initialTwoMovesRank && 
                    grid.getEntry(initialTwoMovesRank - rankDir, ply.getTargetFile()) == grid.getInvalidSquare()) {
                sourceRank = initialTwoMovesRank - 2 * rankDir;
                sourceFile = ply.getTargetFile();
                enPassantSquare = grid.getIndex(sourceRank + rankDir, sourceFile);
            } else {
            
                //1 square
                sourceRank = ply.getTargetRank() -  rankDir;
                sourceFile = ply.getTargetFile();
            }
            
            
        } else {
            Preconditions.checkState(ply.getSourceRank() == -1);
            Preconditions.checkState(ply.getSourceFile() != -1);
            
            sourceRank = ply.getTargetRank() -  rankDir;
            sourceFile = ply.getSourceFile();
        }
        
        Preconditions.checkState(grid.getEntry(sourceRank, 
                sourceFile) == ply.getMovedPiece().getCh(), "Ply %s", 
                ply.toString()
                );
        move(sourceRank, sourceFile, ply.getTargetRank(), ply.getTargetFile());
    }
    
    private void move(int srcRank, int srcFile, int tarRank, int tarFile)
    {
        grid.setEntry(tarRank,tarFile, grid.getEntry(srcRank,srcFile));
        grid.setEntry(srcRank, srcFile, grid.getInvalidSquare());
    }
    
    public String toString2()
    {
        StringBuffer gridStr = new StringBuffer();
        gridStr.append("  a b c d e f g h\n");
        for (int rIdx = 0; rIdx < grid.getRows(); ++rIdx) {
            int r = grid.getRows() - rIdx - 1;
            
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
    
    //Dense form
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
        
        gridStr.append(whiteToMove ? "White to move\n" : "Black to move\n");
        gridStr.append("EP ");
        if (enPassantSquare == -1) {
            gridStr.append("-");
        } else {
            int[] rowCol = grid.getRowCol(enPassantSquare);
            gridStr.append( (char) (rowCol[1] + 'a'));
            gridStr.append( (char) (rowCol[0] + '1'));
        }
        gridStr.append("\n");
        return gridStr.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (blackCastleKingside ? 1231 : 1237);
        result = prime * result + (blackCastleQueenside ? 1231 : 1237);
        result = prime * result + ((grid == null) ? 0 : grid.hashCode());
        result = prime * result + moveNumber;
        result = prime * result + (whiteCastleKingside ? 1231 : 1237);
        result = prime * result + (whiteCastleQueenside ? 1231 : 1237);
        result = prime * result + (whiteToMove ? 1231 : 1237);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        return blackCastleKingside == other.blackCastleKingside
                && blackCastleQueenside == other.blackCastleQueenside
                && Objects.equal(grid, other.grid)
                && whiteCastleKingside == other.whiteCastleKingside
                && whiteCastleQueenside == other.whiteCastleQueenside
                && whiteToMove == other.whiteToMove
                && enPassantSquare == other.enPassantSquare;
            
    }

    /**
     * @return the whiteToMove
     */
    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    /**
     * @param whiteToMove the whiteToMove to set
     */
    public void setWhiteToMove(boolean whiteToMove) {
        this.whiteToMove = whiteToMove;
    }

    /**
     * @return the whiteCastleKingside
     */
    public boolean isWhiteCastleKingside() {
        return whiteCastleKingside;
    }

    /**
     * @param whiteCastleKingside the whiteCastleKingside to set
     */
    public void setWhiteCastleKingside(boolean whiteCastleKingside) {
        this.whiteCastleKingside = whiteCastleKingside;
    }

    /**
     * @return the whiteCastleQueenside
     */
    public boolean isWhiteCastleQueenside() {
        return whiteCastleQueenside;
    }

    /**
     * @param whiteCastleQueenside the whiteCastleQueenside to set
     */
    public void setWhiteCastleQueenside(boolean whiteCastleQueenside) {
        this.whiteCastleQueenside = whiteCastleQueenside;
    }

    /**
     * @return the blackCastleKingside
     */
    public boolean isBlackCastleKingside() {
        return blackCastleKingside;
    }

    /**
     * @param blackCastleKingside the blackCastleKingside to set
     */
    public void setBlackCastleKingside(boolean blackCastleKingside) {
        this.blackCastleKingside = blackCastleKingside;
    }

    /**
     * @return the blackCastleQueenside
     */
    public boolean isBlackCastleQueenside() {
        return blackCastleQueenside;
    }

    /**
     * @param blackCastleQueenside the blackCastleQueenside to set
     */
    public void setBlackCastleQueenside(boolean blackCastleQueenside) {
        this.blackCastleQueenside = blackCastleQueenside;
    }

    /**
     * @return the moveNumber
     */
    public int getMoveNumber() {
        return moveNumber;
    }

    /**
     * @param moveNumber the moveNumber to set
     */
    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    /**
     * @return the enPassantSquare
     */
    public int getEnPassantSquare() {
        return enPassantSquare;
    }

    /**
     * @param enPassantSquare the enPassantSquare to set
     */
    public void setEnPassantSquare(int enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }
}
