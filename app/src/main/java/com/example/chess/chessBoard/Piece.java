package com.example.chess.chessBoard;

/**
 * Abstract method that is inherited by all the different types of pieces. Sets a standard
 * for all pieces to follow.
 * 
 * @author James Hadley
 *
 */
public abstract class Piece {
	
	/**
	 * Keeps track of moved pieces
	 */
	boolean moved;
	/**
	 * Color of the piece
	 */
	String color;
	/**
	 * piece color and type
	 */
	String name;
	
	/**
	 * Returns whether a piece can make a given move, ignoring checks against 
	 * putting your king in check.
	 * 
	 * @param row1 the index of the row at which the piece is currently located
	 * @param col1 the index of the column at which the piece is currently located
	 * @param row2 the index of the row of the destination
	 * @param col2 the index of the column of the destination
	 * @param board the board on which the move is taking place
	 * @param turn the color of the active turn
	 * @return true if the move is possible, false if not
	 */
	public boolean move(int row1, int col1, int row2, int col2, Board board, String turn) {
		
		
		// Check if it's not your turn
		if (!turn.equals(color)) {
			return false;
		}
		
		// Check if the destination is out of bounds bounds
		if ((row2 < 0) || (row2 > 7)) { // Row is out of bounds
			return false;
		} else if ((col2 < 0) || (col2 > 7)) { // Column is out of bounds
			return false;
		}
		
		// Check if the path is trivial
		if (row1 == row2 && col1 == col2) {
			return false;
		}
		
		// Check if the path is not in valid format
		if (!validFormat(row1, col1, row2, col2, board)) {
			return false;
		}
		
		// Check if the path is clear
		if (!pathClear(row1, col1, row2, col2, board)) {
			return false;
		}
		
		// All checks passed, so move is legal
		// BoardState checks if this puts your king in jeopardy
		return true;
	}
	
	
	/**
	 * Returns whether the destination tile is one that can be reached in a single move
	 * by this piece.
	 * 
	 * @param row1 the index of the row at which the piece is currently located
	 * @param col1 the index of the column at which the piece is currently located
	 * @param row2 the index of the row of the destination
	 * @param col2 the index of the column of the destination
	 * @param board the board on which the move is taking place
	 * @return true if the destination tile is reachable, false if not
	 */
	public abstract boolean validFormat(int row1, int col1, int row2, int col2, Board board);
	
	
	/**
	 * Returns whether there are any obstructions in the path, including pieces of
	 * the same colour at the destination or pieces of any colour along the way.
	 * 
	 * @param row1 the index of the row at which the piece is currently located
	 * @param col1 the index of the column at which the piece is currently located
	 * @param row2 the index of the row of the destination
	 * @param col2 the index of the column of the destination
	 * @param board the board on which the move is taking place
	 * @return true if the path is clear, false if there is an obstruction
	 */
	public boolean pathClear(int row1, int col1, int row2, int col2, Board board) {

        // Check if last piece is same color
        if (board.getPiece(row2, col2) != null) {
            if (board.getPiece(row2, col2).getColor().equals(color)) {
                return false;
            }
        }

        // Check intermediate steps to see if there are pieces in the way
        int rowStep = (row2 - row1);
        if (rowStep != 0) rowStep /= Math.abs(row2 - row1);
        int colStep = (col2 - col1);
        if (colStep != 0) colStep /= Math.abs(col2 - col1);

        int row = row1;
        int col = col1;

        int limit = Math.abs(row2-row1);
        if(limit == 0) limit = Math.abs(col2-col1); 

        for (int i = 0; i < limit - 1; i++) {

            row += rowStep;
            col += colStep;

            if (board.getPiece(row, col) != null) {
                return false;
            }
        }

        return true;
    }
	
	/**
	 * Creates and returns a copy of a piece
	 * 
	 * @return Piece object with the same value in all fields as this object
	 */
	public abstract Piece copy();
	
	/**
	 * Returns the name of the piece
	 * 
	 * @return String with the name of the type of piece
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the color of the piece
	 * 
	 * @return String with the color of the piece
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * 
	 * @return String consisting of the color of the piece followed by its designated symbol
	 */
	public String toString() {
		return color + name;
	}
	
	/**
	 * Returns if a piece has moved or not.
	 * 
	 * @return true if the piece has been moved, false otherwise
	 */
	public boolean hasMoved() {
		return moved;
	}
	
	/**
	 * Sets the piece's moved field
	 * 
	 * @param moved true if the piece has been moved, false otherwise
	 */
	public void setMoved(Boolean moved) {
		this.moved = moved;
	}
	
}
