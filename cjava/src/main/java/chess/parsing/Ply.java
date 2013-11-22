package chess.parsing;

import java.util.List;

import chess.Board;
import chess.ColoredPiece;

public class Ply {
    boolean isWhiteMove;

    ColoredPiece movedPiece;
    
    int targetFile;
    int targetRank;
    
    int sourceFile;
    int sourceRank;
    
    boolean isCapture;

    int moveNumber;

    List<List<Move>> variations;
    
    String sanTxt;
    
    String comment;
    
    //http://en.wikipedia.org/wiki/Numeric_Annotation_Glyphs
    String nagComment; //like ! or ??
    Integer nagValue;
    
    Board boardBeforeMove;
    Board boardAfterMove;
    

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(moveNumber);
        if (isWhiteMove) {
            ret.append(". ");
        } else {
            ret.append("... ");
        }
        ret.append(sanTxt);
        if (comment != null) {
            ret.append(" ");
            ret.append("Comment : ");
            ret.append(comment);
        }
        return ret.toString();
    }


    /**
     * @return the isWhiteMove
     */
    public boolean isWhiteMove() {
        return isWhiteMove;
    }


    /**
     * @param isWhiteMove the isWhiteMove to set
     */
    public void setWhiteMove(boolean isWhiteMove) {
        this.isWhiteMove = isWhiteMove;
    }


    /**
     * @return the movedPiece
     */
    public ColoredPiece getMovedPiece() {
        return movedPiece;
    }


    /**
     * @param movedPiece the movedPiece to set
     */
    public void setMovedPiece(ColoredPiece movedPiece) {
        this.movedPiece = movedPiece;
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
     * @return the variations
     */
    public List<List<Move>> getVariations() {
        return variations;
    }


    /**
     * @param variations the variations to set
     */
    public void setVariations(List<List<Move>> variations) {
        this.variations = variations;
    }


    /**
     * @return the sanTxt
     */
    public String getSanTxt() {
        return sanTxt;
    }


    /**
     * @param sanTxt the sanTxt to set
     */
    public void setSanTxt(String sanTxt) {
        this.sanTxt = sanTxt;
    }


    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }


    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
     * @return the nagComment
     */
    public String getNagComment() {
        return nagComment;
    }


    /**
     * @param nagComment the nagComment to set
     */
    public void setNagComment(String nagComment) {
        this.nagComment = nagComment;
    }


    /**
     * @return the nagValue
     */
    public Integer getNagValue() {
        return nagValue;
    }


    /**
     * @param nagValue the nagValue to set
     */
    public void setNagValue(Integer nagValue) {
        this.nagValue = nagValue;
    }


    /**
     * @return the boardBeforeMove
     */
    public Board getBoardBeforeMove() {
        return boardBeforeMove;
    }


    /**
     * @param boardBeforeMove the boardBeforeMove to set
     */
    public void setBoardBeforeMove(Board boardBeforeMove) {
        this.boardBeforeMove = boardBeforeMove;
    }


    /**
     * @return the boardAfterMove
     */
    public Board getBoardAfterMove() {
        return boardAfterMove;
    }


    /**
     * @param boardAfterMove the boardAfterMove to set
     */
    public void setBoardAfterMove(Board boardAfterMove) {
        this.boardAfterMove = boardAfterMove;
    }


    /**
     * @return the targetFile
     */
    public int getTargetFile() {
        return targetFile;
    }


    /**
     * @param targetFile the targetFile to set
     */
    public void setTargetFile(int targetFile) {
        this.targetFile = targetFile;
    }


    /**
     * @return the targetRank
     */
    public int getTargetRank() {
        return targetRank;
    }


    /**
     * @param targetRank the targetRank to set
     */
    public void setTargetRank(int targetRank) {
        this.targetRank = targetRank;
    }


    /**
     * @return the isCapture
     */
    public boolean isCapture() {
        return isCapture;
    }


    /**
     * @param isCapture the isCapture to set
     */
    public void setCapture(boolean isCapture) {
        this.isCapture = isCapture;
    }


    /**
     * @return the sourceFile
     */
    public int getSourceFile() {
        return sourceFile;
    }


    /**
     * @param sourceFile the sourceFile to set
     */
    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }


    /**
     * @return the sourceRank
     */
    public int getSourceRank() {
        return sourceRank;
    }


    /**
     * @param sourceRank the sourceRank to set
     */
    public void setSourceRank(int sourceRank) {
        this.sourceRank = sourceRank;
    }
}
