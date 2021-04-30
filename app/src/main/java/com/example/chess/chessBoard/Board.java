package com.example.chess.chessBoard;

/**
 * Represents a chessboard, sets up the chessboard at the start of the game
 * 
 * @author James Hadley, Sai Kalagotla
 *
 */
public class Board {
	
	/**
	 * The chessboard
	 */
	private Piece board[][] = new Piece[8][8];
	
	/**
	 * A black pawn
	 */
	private Piece pawnB0 = new Pawn("b");
	/**
	 * A black pawn
	 */
	private Piece pawnB1 = new Pawn("b");
	/**
	 * A black pawn
	 */
	private Piece pawnB2 = new Pawn("b");
	/**
	 * A black pawn
	 */
	private Piece pawnB3 = new Pawn("b");
	/**
	 * A black pawn
	 */
	private Piece pawnB4 = new Pawn("b");
	/**
	 * A black pawn
	 */
	private Piece pawnB5 = new Pawn("b");
	/**
	 * A black pawn
	 */
	private Piece pawnB6 = new Pawn("b");
	/**
	 * A black pawn
	 */
	private Piece pawnB7 = new Pawn("b");
	
	/**
	 * A black rook
	 */
	private Piece rookB0 = new Rook("b");
	/**
	 * A black rook
	 */
	private Piece rookB1 = new Rook("b");
	
	/**
	 * A black knight
	 */
	private Piece knightB0 = new Knight("b");
	/**
	 * A black knight
	 */
	private Piece knightB1 = new Knight("b");
	
	/**
	 * A black bishop
	 */
	private Piece bishopB0 = new Bishop("b");
	/**
	 * A black bishop
	 */
	private Piece bishopB1 = new Bishop("b");
	
	/**
	 * A black king
	 */
	private Piece kingB = new King("b");
	/**
	 * A black queen
	 */
	private Piece queenB = new Queen("b");
	
	/**
	 * A white pawn
	 */
	private Piece pawnW0 = new Pawn("w");
	/**
	 * A white pawn
	 */
	private Piece pawnW1 = new Pawn("w");
	/**
	 * A white pawn
	 */
	private Piece pawnW2 = new Pawn("w");
	/**
	 * A white pawn
	 */
	private Piece pawnW3 = new Pawn("w");
	/**
	 * A white pawn
	 */
	private Piece pawnW4 = new Pawn("w");
	/**
	 * A white pawn
	 */
	private Piece pawnW5 = new Pawn("w");
	/**
	 * A white pawn
	 */
	private Piece pawnW6 = new Pawn("w");
	/**
	 * A white pawn
	 */
	private Piece pawnW7 = new Pawn("w");
	
	/**
	 * A white rook
	 */
	private Piece rookW0 = new Rook("w");
	/**
	 * A white rook
	 */
	private Piece rookW1 = new Rook("w");
	
	/**
	 * A white knight
	 */
	private Piece knightW0 = new Knight("w");
	/**
	 * A white knight
	 */
	private Piece knightW1 = new Knight("w");
	
	/**
	 * A white bishop
	 */
	private Piece bishopW0 = new Bishop("w");
	/**
	 * A white bishop
	 */
	private Piece bishopW1 = new Bishop("w");
	
	/**
	 * A white king
	 */
	private Piece kingW = new King("w");
	/**
	 * A white queen
	 */
	private Piece queenW = new Queen("w");
	
	/**
	 * Keeps track if an enPassant is possible
	 */
	private Pawn enPassant = null;
	
	/**
	 * Constructor for the Board class
	 */
	public Board() {
		
		board[1][0] = pawnB0;
		board[1][1] = pawnB1;
		board[1][2] = pawnB2;
		board[1][3] = pawnB3;
		board[1][4] = pawnB4;
		board[1][5] = pawnB5;
		board[1][6] = pawnB6;
		board[1][7] = pawnB7;
		
		board[0][0] = rookB0;
		board[0][7] = rookB1;
		
		board[0][1] = knightB0;
		board[0][6] = knightB1;
		
		board[0][2] = bishopB0;
		board[0][5] = bishopB1;
		
		board[0][3] = queenB;
		board[0][4] = kingB;
		
		board[6][0] = pawnW0;
		board[6][1] = pawnW1;
		board[6][2] = pawnW2;
		board[6][3] = pawnW3;
		board[6][4] = pawnW4;
		board[6][5] = pawnW5;
		board[6][6] = pawnW6;
		board[6][7] = pawnW7;
		
		board[7][0] = rookW0;
		board[7][7] = rookW1;
		
		board[7][1] = knightW0;
		board[7][6] = knightW1;
		
		board[7][2] = bishopW0;
		board[7][5] = bishopW1;
		
		board[7][3] = queenW;
		board[7][4] = kingW;
		
	}
	
	/**
	 * 
	 * @return 2D Board array representing the current state of the chess game
	 */
	public Piece[][] getBoard() {
		return board;
	}
	
	
	/**
	 * Returns the piece at a given location
	 * 
	 * @param row index of the row of the Piece
	 * @param col index of the column of the Piece
	 * @return Piece found at the given row and column
	 */
	public Piece getPiece(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * Sets the piece at a given location
	 * 
	 * @param row index of the row to set the Piece in
	 * @param col index of the column to set the Piece in
	 * @param p Piece to be placed at the given row and column
	 */
	public void setPiece(int row, int col, Piece p) {
		board[row][col] = p;
	}
	
	
	/**
	 * Sets the Pawn on which an en passant kill may be carried out
	 * 
	 * @param p Pawn on which an en passant kill may be carried out
	 */
	public void setEnPassant(Pawn p) {
		this.enPassant = p;
	}
	
	
	/**
	 * Returns the Pawn object on which an en passant kill may be carried out
	 * 
	 * @return Pawn on which an en passant kill may be carried out
	 */
	public Pawn getEnPassant() {
		return enPassant;
	}
	
	
	/**
	 * Creates a new object representing an identical state of a chess game
	 * 
	 * @return new Board object with identical copies of all the fields in this object
	 */
	public Board copy() {
		Board b = new Board();
		
		for(int i = 0; i < 8; i++) {
			for(int x = 0; x < 8; x++) {
				if(board[i][x] != null) {
					b.setPiece(i, x, board[i][x].copy());
					if(enPassant != null) {
						b.enPassant = ((Pawn) enPassant.copy());
					}
				}
				else {
					b.setPiece(i, x, null);
				}
			}
		}
		
		return b;
	}
}
