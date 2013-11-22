package chess;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.rmi.runtime.Log;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;

import chess.parsing.Fen;
import chess.parsing.PgnParser;
import chess.parsing.Ply;
import codejam.utils.utils.GridChar;
import codejam.y2008.KingTest;

public class Board {
    private static final Logger log = LoggerFactory.getLogger(Board.class);
    
    public GridChar grid;

    boolean whiteToMove;
    // white kingside / queenside black kingside / queenside
    boolean[] castling;

    int moveNumber;

    int enPassantSquare;

    int lastCapture;

    public Board() {
        grid = GridChar.buildEmptyGrid(8, 8, '.');
        grid.setyZeroOnTop(false);
        castling = new boolean[] { true, true, true, true };
        enPassantSquare = -1;
    }

    public Board(Board rhs) {
        grid = new GridChar(rhs.grid);
        castling = new boolean[4];
        System.arraycopy(rhs.castling, 0, castling, 0, rhs.castling.length);
        moveNumber = rhs.moveNumber;
        enPassantSquare = rhs.enPassantSquare;
        whiteToMove = rhs.whiteToMove;
    }

    public Board(String fenStr) {

        this();

        // Index of square on board (0-63)
        int fenIdx = 0;
        // Index in string
        int cIdx = 0;
        for (cIdx = 0; cIdx < fenStr.length(); ++cIdx) {
            if (fenIdx >= 64)
                break;
            int rank = 7 - fenIdx / 8;
            int file = fenIdx % 8;

            char c = fenStr.charAt(cIdx);

            // log.debug("Char {} fenIdx {} rank {} file {}", c,
            // fenIdx, rank+1, (char) ('a' + file));

            if (c == '/') {
                Preconditions.checkState(file == 0);
                continue;
            }

            if (Character.isDigit(c)) {
                fenIdx += Character.digit(c, 10);
                continue;
            }

            grid.setEntry(rank, file, c);
            ++fenIdx;

            // log.debug("\n" + b.toString());
        }

        fenStr = fenStr.substring(cIdx).trim();

        Iterator<String> tokens = Splitter.on(Pattern.compile("\\s+"))
                .split(fenStr).iterator();

        setWhiteToMove(tokens.next().equals("w"));

        String castle = tokens.next();
        String ep = tokens.next();
        if (ep.equals("-")) {
            setEnPassantSquare(-1);
        } else {
            Preconditions.checkState(ep.length() == 2);
            setEnPassantSquare(grid.getIndex(ep.charAt(1) - '1',
                    ep.charAt(0) - 'a'));
        }
        String lastPawnMoveOrCap = tokens.next();

        lastCapture = Integer.parseInt(lastPawnMoveOrCap);

        String moveNum = tokens.next();

        moveNumber = Integer.parseInt(moveNum);
        // w KQkq - 0 1

    }

    public static String initialFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public Board makeMove(Ply ply) {
        Board b = new Board(this);

        int[] pieceCounts = b.tallyPiecePointCount();
        
        switch (ply.getMovedPiece().getPiece()) {
        case Pawn:
            b.movePawn(ply);

            break;
        case Queen:
            b.moveQueen(ply);
            break;
        case Knight:
            b.moveKnight(ply);
            break;
        case Bishop:
            b.moveBishop(ply);
            break;
        case King:
            b.moveKing(ply);
            break;
        case Rook:
            b.moveRook(ply);
            break;
        default:
            Preconditions.checkState(false, ply);
        }
        
        b.whiteToMove = !b.whiteToMove;

        if (b.whiteToMove) {
            b.moveNumber++;
        }
        
        int[] newPieceCounts = b.tallyPiecePointCount();
        
        if (ply.isCapture()) {
            
        } else {
            Preconditions.checkState( pieceCounts[0] == newPieceCounts[0]);
            Preconditions.checkState( pieceCounts[1] == newPieceCounts[1]);
        }

        return b;
    }
    
    private void moveKing(Ply ply) {
        char kingChar = ply.isWhiteMove() ? 'K' : 'k';
        
        //final int targetRank = ply.getTargetRank();
        //final int targetFile = ply.getTargetFile();
        
        int[] kingPos = getMovedPieceIndex(kingChar, ply, new Predicate<Integer>() {

            @Override
            public boolean apply(Integer piecePosGridIndex) {
                
                    return true;
            }
            
        });
        
        
        
        if (ply.getSanTxt().equals("O-O")) {
            int rank = ply.isWhiteMove() ? 0 : 7;
                
            Preconditions.checkState(grid.getEntry(rank,4) ==  (ply.isWhiteMove() ? 'K' : 'k'));
            Preconditions.checkState(grid.getEntry(rank,5) ==  grid.getInvalidSquare());
            Preconditions.checkState(grid.getEntry(rank,6) ==  grid.getInvalidSquare());
            Preconditions.checkState(grid.getEntry(rank,7) ==  (ply.isWhiteMove() ? 'R' : 'r'));
                
                
                
            move(rank, 7, rank, 5);
            
            move(kingPos[0], kingPos[1], rank, 6);
                
            Preconditions.checkState(grid.getEntry(0,4) ==  grid.getInvalidSquare());
            Preconditions.checkState(grid.getEntry(0,5) ==  (ply.isWhiteMove() ? 'R' : 'r'));
            Preconditions.checkState(grid.getEntry(0,6) ==  (ply.isWhiteMove() ? 'K' : 'k'));
            Preconditions.checkState(grid.getEntry(0,7) ==  grid.getInvalidSquare());
        } else if (ply.getSanTxt().equals("O-O-O")) {
                
                Preconditions.checkState(grid.getEntry(0,4) ==  (ply.isWhiteMove() ? 'K' : 'k'));
                Preconditions.checkState(grid.getEntry(0,3) ==  grid.getInvalidSquare());
                Preconditions.checkState(grid.getEntry(0,2) ==  grid.getInvalidSquare());
                Preconditions.checkState(grid.getEntry(0,1) ==  grid.getInvalidSquare());
                Preconditions.checkState(grid.getEntry(0,0) ==  (ply.isWhiteMove() ? 'R' : 'r'));
                
                move(7, 7, 7, 5);
                
                Preconditions.checkState(grid.getEntry(0,4) ==  grid.getInvalidSquare());
                Preconditions.checkState(grid.getEntry(0,3) ==  (ply.isWhiteMove() ? 'R' : 'r'));
                Preconditions.checkState(grid.getEntry(0,2) ==  (ply.isWhiteMove() ? 'K' : 'k'));
                Preconditions.checkState(grid.getEntry(0,1) ==  grid.getInvalidSquare());
                Preconditions.checkState(grid.getEntry(0,0) ==  grid.getInvalidSquare());
            
        } else {
            move(kingPos[0], kingPos[1], ply.getTargetRank(), ply.getTargetFile());
        }
        
        
    }
    
    private void moveBishop(Ply ply) {
        char pieceChar = ply.isWhiteMove() ? 'B' : 'b';
        
        final int targetRank = ply.getTargetRank();
        final int targetFile = ply.getTargetFile();
        
        int[] piecePos = getMovedPieceIndex(pieceChar, ply, new Predicate<Integer>() {

            @Override
            public boolean apply(Integer piecePosGridIndex) {
                int[] rankFile = grid.getRowCol(piecePosGridIndex);
                
                if (emptyBetween(rankFile[0], rankFile[1], targetRank, targetFile, false, true)) 
                    return true;
                
                return false;
            }
            
        });
        
        move(piecePos[0], piecePos[1], ply.getTargetRank(), ply.getTargetFile());
        
    }
    
    private void moveRook(Ply ply) {
        char pieceChar = ply.isWhiteMove() ? 'R' : 'r';
        
        final int targetRank = ply.getTargetRank();
        final int targetFile = ply.getTargetFile();
        
        int[] piecePos = getMovedPieceIndex(pieceChar, ply, new Predicate<Integer>() {

            @Override
            public boolean apply(Integer piecePosGridIndex) {
                int[] rankFile = grid.getRowCol(piecePosGridIndex);
                
                if (emptyBetween(rankFile[0], rankFile[1], targetRank, targetFile, true, false)) 
                    return true;
                
                return false;
            }
            
        });
        
        move(piecePos[0], piecePos[1], ply.getTargetRank(), ply.getTargetFile());
        
    }

    private void moveQueen(Ply ply) {
        char queenChar = ply.isWhiteMove() ? 'Q' : 'q';
        
        final int targetRank = ply.getTargetRank();
        final int targetFile = ply.getTargetFile();
        
        int[] queenPos = getMovedPieceIndex(queenChar, ply, new Predicate<Integer>() {

            @Override
            public boolean apply(Integer piecePosGridIndex) {
                int[] rankFile = grid.getRowCol(piecePosGridIndex);
                
                if (emptyBetween(rankFile[0], rankFile[1], targetRank, targetFile, true, true)) 
                    return true;
                
                return false;
            }
            
        });
        
        move(queenPos[0], queenPos[1], ply.getTargetRank(), ply.getTargetFile());
        
    }
    
    private boolean emptyBetween(int sourceRank, int sourceFile, int targetRank, int targetFile, boolean horVer, boolean diag)
    {
        /*
        if (sourceRank == targetRank)
        {
            for(int file = Math.min(targetFile, sourceFile) + 1; file < Math.max(targetFile, sourceFile); ++file)
            {
                if (grid.getEntry(sourceRank, file) != grid.getInvalidSquare()) {
                    return false;
                }
            }
        } else if (sourceFile == targetFile) {
            for(int rank = Math.min(targetRank, sourceRank) + 1; rank < Math.max(targetRank, sourceRank); ++rank)
            {
                if (grid.getEntry(rank, sourceFile) != grid.getInvalidSquare()) {
                    return false;
                }
            }
        } else {
        */
        int slopeRank = targetRank - sourceRank;
        int slopeFile = targetFile - sourceFile;
        
        log.debug("slopeRank {} slopeFile {}", slopeRank, slopeFile);
        
        if (slopeRank * slopeFile == 0 && horVer) {
            //hor or vertical
        } else if (Math.abs(slopeRank) == Math.abs(slopeFile) && diag) {
            //diag
        } else {
            return false;
        }
            
        int div = Math.max( Math.abs(slopeFile), Math.abs(slopeRank));
        
        slopeFile /= div;
        slopeRank /= div;
        
        Preconditions.checkState(slopeRank >= -1 && slopeRank <= 1);
        Preconditions.checkState(slopeFile >= -1 && slopeFile <= 1);
        
        int rank = sourceRank + slopeRank;
        int file = sourceFile + slopeFile;
        
        int loopCheck = 0;
        
        while(rank != targetRank || file != targetFile)
        {
            ++loopCheck;
            Preconditions.checkState(loopCheck  < 10);
            
            if (grid.getEntry(rank, file) != grid.getInvalidSquare()) {
                return false;
            }
            
            rank += slopeRank;
            file += slopeFile;
        }
        
        return true;
    }
    
    private void moveKnight(Ply ply) {
        char knightChar = ply.isWhiteMove() ? 'N' : 'n';
        
        final int targetRank = ply.getTargetRank();
        final int targetFile = ply.getTargetFile();
        
        int[] knightPos = getMovedPieceIndex(knightChar, ply, new Predicate<Integer>() {

            @Override
            public boolean apply(Integer piecePosGridIndex) {
                int[] rankFile = grid.getRowCol(piecePosGridIndex);
                
                int dFile = Math.abs(targetFile - rankFile[1]);
                int dRank = Math.abs(targetRank - rankFile[0]);
                if ( dFile + dRank == 3 && (dFile == 1 || dRank == 1) )
                    return true;
                
                return false;
            }
            
        });
        
        move(knightPos[0], knightPos[1], ply.getTargetRank(), ply.getTargetFile());

        
    }
    
    private int[] getMovedPieceIndex(char pieceChar, Ply ply, Predicate<Integer> sourcePieceCheck)
    {
        Set<Integer> piecePos = grid.getIndexesOf(pieceChar);
        
        Preconditions.checkState(piecePos.size() > 0);
        
        if (piecePos.size() == 1) {
            return grid.getRowCol(piecePos.iterator().next());
        }
        
        int[] retVal = null;
        
        for(Integer piecePosGridIndex : piecePos)
        {
            int[] rankFile = grid.getRowCol(piecePosGridIndex);
            
            if (ply.getSourceRank() != -1 && ply.getSourceRank() != rankFile[0])
                continue;
            
            if (ply.getSourceFile() != -1 && ply.getSourceFile() != rankFile[1])
                continue;
            
            if (!sourcePieceCheck.apply(piecePosGridIndex))
                continue;
            
            Preconditions.checkState(retVal == null);
            retVal = rankFile;
        }
        
        Preconditions.checkState(retVal != null);
        return retVal;
        
    }
    
    private void movePawn(Ply ply) {
        Preconditions.checkState(ply.isWhiteMove() == this.whiteToMove);

        int rankDir = ply.isWhiteMove() ? 1 : -1;
        int initialTwoMovesRank = ply.isWhiteMove() ? 3 : 4;

        int sourceRank = -1;
        int sourceFile = -1;

        if (!ply.isCapture()) {

            Preconditions.checkState(ply.getSourceRank() == -1);
            Preconditions.checkState(ply.getSourceFile() == -1);

            // square is empty
            Preconditions.checkState(grid.getInvalidSquare() == grid.getEntry(
                    ply.getTargetRank(), ply.getTargetFile()));

            if (ply.getTargetRank() == initialTwoMovesRank
                    && grid.getEntry(initialTwoMovesRank - rankDir,
                            ply.getTargetFile()) == grid.getInvalidSquare()) {
                sourceRank = initialTwoMovesRank - 2 * rankDir;
                sourceFile = ply.getTargetFile();
                enPassantSquare = grid.getIndex(sourceRank + rankDir,
                        sourceFile);
            } else {

                // 1 square
                sourceRank = ply.getTargetRank() - rankDir;
                sourceFile = ply.getTargetFile();

                enPassantSquare = -1;
            }

        } else {
            Preconditions.checkState(ply.getSourceRank() == -1);
            Preconditions.checkState(ply.getSourceFile() != -1);

            int targetSquare = grid.getIndex(ply.getTargetRank(),
                    ply.getTargetFile());

            if (enPassantSquare == targetSquare) {
                Preconditions.checkState('P' == Character.toUpperCase( grid.getEntry(ply.getTargetRank() - rankDir, ply.getTargetFile())));
                                        
                //Remove enemy pawn
                grid.setEntry(ply.getTargetRank() - rankDir, ply.getTargetFile(),
                        grid.getInvalidSquare());
            } else {
                Preconditions.checkState(grid
                                .getEntry(targetSquare) != grid
                                .getInvalidSquare());
            }
            

            enPassantSquare = -1;
            
            sourceRank = ply.getTargetRank() - rankDir;
            sourceFile = ply.getSourceFile();
        }

        Preconditions.checkState(grid.getEntry(sourceRank, sourceFile) == ply
                .getMovedPiece().getCh(), "Ply %s", ply.toString());
        move(sourceRank, sourceFile, ply.getTargetRank(), ply.getTargetFile());

        
    }

    private void move(int srcRank, int srcFile, int tarRank, int tarFile) {
        grid.setEntry(tarRank, tarFile, grid.getEntry(srcRank, srcFile));
        grid.setEntry(srcRank, srcFile, grid.getInvalidSquare());
    }

    public String toString2() {
        StringBuffer gridStr = new StringBuffer();
        gridStr.append("  a b c d e f g h\n");
        for (int rIdx = 0; rIdx < grid.getRows(); ++rIdx) {
            int r = grid.getRows() - rIdx - 1;

            gridStr.append(1 + r);
            gridStr.append(" |");

            for (int c = 0; c < grid.getCols(); ++c) {

                gridStr.append(grid.getEntry(r, c));
            }
            if (rIdx != grid.getRows() - 1)
                gridStr.append("\n");
        }
        return gridStr.toString();
    }

    public String toFenString() {

        StringBuffer ret = new StringBuffer();

        for (int rank = 7; rank >= 0; --rank) {
            int empty = 0;
            for (int file = 0; file <= 7; ++file) {
                if (grid.getEntry(rank, file) == grid.getInvalidSquare()) {
                    empty++;
                } else {
                    if (empty > 0) {
                        ret.append(empty);
                        empty = 0;
                    }
                    ret.append(grid.getEntry(rank, file));
                }
            }

            if (empty > 0) {
                ret.append(empty);
            }
            empty = 0;
            if (rank > 0) {
                ret.append("/");
            }
        }

        if (whiteToMove)
            ret.append(" w ");
        else
            ret.append(" b ");

        if (castling[0])
            ret.append("K");
        if (castling[1])
            ret.append("Q");
        if (castling[2])
            ret.append("k");
        if (castling[3])
            ret.append("q");
        if (!castling[0] && !castling[1] && !castling[2] && !castling[3])
            ret.append("-");

        if (enPassantSquare == -1) {
            ret.append(" -");
        } else {
            int[] rowCol = grid.getRowCol(enPassantSquare);
            ret.append(" ");
            ret.append((char) (rowCol[1] + 'a'));
            ret.append((char) (rowCol[0] + '1'));
        }

        ret.append(" ");
        ret.append(this.lastCapture);

        ret.append(" ");
        ret.append(this.moveNumber);

        return ret.toString();
    }

    // Dense form
    public String toString() {
        StringBuffer gridStr = new StringBuffer();
        gridStr.append("   abcdefgh\n");
        for (int rIdx = 0; rIdx < grid.getRows(); ++rIdx) {
            int r = grid.getRows() - rIdx - 1;

            gridStr.append(1 + r);
            gridStr.append(" |");
            ;

            for (int c = 0; c < grid.getCols(); ++c) {

                gridStr.append(grid.getEntry(r, c));
            }
            if (rIdx != grid.getRows() - 1)
                gridStr.append("\n");
        }
        gridStr.append("\n");
        gridStr.append(whiteToMove ? "White to move\n" : "Black to move\n");
        gridStr.append("EP ");
        if (enPassantSquare == -1) {
            gridStr.append("-");
        } else {
            int[] rowCol = grid.getRowCol(enPassantSquare);
            gridStr.append((char) (rowCol[1] + 'a'));
            gridStr.append((char) (rowCol[0] + '1'));
        }
        gridStr.append("\n");
        gridStr.append("Castling : ");
        if (castling[0])
            gridStr.append("K");
        if (castling[1])
            gridStr.append("Q");
        if (castling[2])
            gridStr.append("k");
        if (castling[3])
            gridStr.append("q");

        gridStr.append("\n");
        gridStr.append(toFenString());
        return gridStr.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((grid == null) ? 0 : grid.hashCode());
        result = prime * result + moveNumber;
        result = prime * result + (whiteToMove ? 1231 : 1237);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        return Arrays.equals(castling, other.castling)
                && Objects.equal(grid, other.grid)
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
    
    public int[] tallyPiecePointCount()
    {
        int[] ret = new int[2];
        
        for(int i = 0; i < 64; ++i) {
            char c = grid.getEntry(i);
            
            if (c == grid.getInvalidSquare())
                continue;
            
            ColoredPiece p = ColoredPiece.ToPiece(Character.toString(c));
            
            ret[p.isWhite ? 0 : 1]+= p.value;
        }
        
        return ret;
    }
}
