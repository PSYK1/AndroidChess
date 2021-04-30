package com.example.chess.chessBoard;

/**
 * Represents a pawn
 * 
 * @author James Hadley, Sai Kalagotla
 *
 */
public class Pawn extends Piece {
	
	private boolean enPassant;
	
	/**
	 * 
	 * @param color The color to which the pawn belongs
	 */
	public Pawn(String color) {
		this.enPassant = false;
		this.color = color;
		this.name = "p";
	}
	

	@Override
    public boolean validFormat(int row1, int col1, int row2, int col2, Board board) {
        int dist = row2 - row1;

        if (dist == 0) return false;

        if (dist > 0 && color.equals("w")) return false;
        if (dist < 0 && color.equals("b")) return false;

        if (Math.abs(dist) > 2) return false;

        if (moved && Math.abs(dist) == 2) return false;

        int shift = col2 - col1;

        if (Math.abs(shift) > 1) return false;

        if (shift != 0) {
            if (board.getPiece(row2, col2) == null) {
                // Check if en passant
                Piece temp = board.getPiece(row1, col2);
                // Check if it's a pawn
                if (temp == null || !(temp instanceof Pawn)) return false;
                // Check if the pawn did a double stride
                else if (((Pawn)temp).getEnPassant()) return true;
                // No double stride, so move is invalid
                return false;
            } else return true;
        } else if (board.getPiece(row2, col2) != null) return false;

        return true;
    }
	
	
	public Piece copy() {
		Pawn copy = new Pawn(color);
		copy.setMoved(moved);
		copy.setEnPassant(enPassant);
		return copy;
	}
	
	
	/**
	 * 
	 * @return true if an en passant kill may be carried out on this pawn, false otherwise
	 */
	public boolean getEnPassant() {
		return enPassant;
	}
	
	
	/**
	 * 
	 * @param enPassant true if an en passant kill may be carried out on this pawn, false otherwise
	 */
	public void setEnPassant(Boolean enPassant) {
		this.enPassant = enPassant;
	}
	
}