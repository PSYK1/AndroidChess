package com.example.chess.chessBoard;

/**
 * Represents a knight
 * 
 * @author James Hadley, Sai Kalagotla
 *
 */
public class Knight extends Piece {
		
	/**
	 * 
	 * @param color The color to which the knight belongs
	 */
	public Knight(String color) {
		this.color = color;
		this.name = "n";
	}

	@Override
	public boolean validFormat(int row1, int col1, int row2, int col2, Board board) {
		// Must be an L of dimensions 1x2 or 2x1
		
		if (Math.abs(col2 - col1) + Math.abs(row2 - row1) != 3) {
			return false;
		}
		
		if (Math.abs(col2 - col1) == 0 || Math.abs(row2 - row1) == 0) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean pathClear(int row1, int col1, int row2, int col2, Board board) {
		// Check if destination is the same color
		if (board.getPiece(row2, col2) != null) {
			if (board.getPiece(row2, col2).getColor().equals(color)) {
				return false;
			}
		}
		
		return true;
	}
	
	public Piece copy() {
		Knight copy = new Knight(color);
		copy.setMoved(moved);
		return copy;
	}
}