package chess;

public enum ColoredPiece {
    BPawn('p', Piece.Pawn, 1, false),
    BKnight('n', Piece.Knight, 3, false),
    BBishop('b', Piece.Bishop, 3, false),    
    BRook('r', Piece.Rook, 5, false),
    BQueen('q', Piece.Queen, 9, false),
    BKing('k', Piece.King, 0, false),    
    WPawn('P', Piece.Pawn, 1, true),
    WKnight('N', Piece.Knight, 3, true),
    WBishop('B', Piece.Bishop, 3, true),    
    WRook('R', Piece.Rook, 5, true),
    WQueen('Q', Piece.Queen, 9, true),
    WKing('K', Piece.King, 0, true);
    
    private char ch;
    private Piece piece;
    
    boolean isWhite;
    int value;
    
    ColoredPiece(char pieceCh, Piece piece, int value, boolean isWhite) {
        this.ch = pieceCh;
        this.piece = piece;
    }

    /**
     * @return the ch
     */
    public char getCh() {
        return ch;
    }

    
    
    public static ColoredPiece ToPiece(String str) {
        
        switch(str.charAt(0)) {
        case 'p':
            return BPawn;
        case 'P':
            return WPawn;
        case 'Q':
            return WQueen;
        case 'q':
            return BQueen;
        case 'n':
            return BKnight;
        case 'N':
            return WKnight;
        case 'b':
            return BBishop;
        case 'B':
            return WBishop;
        case 'r':
            return BRook;
        case 'R':
            return WRook;
        case 'k':
            return BKing;
        case 'K':
            return WKing;
        }
            return null;
    }

    /**
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    
}
