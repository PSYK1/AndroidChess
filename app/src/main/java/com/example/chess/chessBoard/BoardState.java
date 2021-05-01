package com.example.chess.chessBoard;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores and manipulates the state of the board
 * 
 * @author James Hadley, Sai Kalagotla
 */
public class BoardState {
	
	/**
	 * The current board state
	 */
	public Board board = new Board();
	
	/**
	 * Returns the piece a pawn should be promoted to, if the function is passed a string other than q,r,b, or n case does not matter
	 * it will return a Queen piece and the color of the piece is determined by the turn parameter.
	 * 
	 * @param promo the type of piece to promote to
	 * @param turn the color of the piece to promote to
	 * @return the piece to promote to
	 */
	public Piece getPromotion(String promo, String turn) {
		Piece p = new Queen(turn);
		
		if(promo.toLowerCase().equals("q")) {
			p = new Queen(turn);
			p.setMoved(true);
			return p;
		}
		if(promo.toLowerCase().equals("r")) {
			p = new Rook(turn);
			p.setMoved(true);
			return p;
		}
		if(promo.toLowerCase().equals("b")) {
			p = new Bishop(turn);
			p.setMoved(true);
			return p;
		}
		if(promo.toLowerCase().equals("n")) {
			p = new Knight(turn);
			p.setMoved(true);
			return p;
		}
		p.setMoved(true);
		return p;
	}
	
	/**
	 * Takes in input for pos1 and pos2 in columnRow format. Returns a Board with the piece from pos1 moved to pos2 if the move is valid
	 * otherwise returns an IllegalArgumentException
	 * 
	 * @param pos1 the location of the piece needed to be moved in columnRow format
	 * @param pos2 the location the piece needs to be moved to in columnRow format
	 * @param promo if a pawn is to be promoted the piece to be promoted to else null if a pawn is not being promoted
	 * @param turn the color of the current player
	 * @return the board after the changed position of a piece
	 */
	public Board setPosition(String pos1, String pos2, Piece promo, String turn) {
		
		int row1 = 8-Integer.parseInt(pos1.substring(1));
		int col1 = (pos1.toLowerCase().charAt(0))-97;
		
		int row2 = 8-Integer.parseInt(pos2.substring(1));
		int col2 = (pos2.toLowerCase().charAt(0))-97;
		
		Piece p = board.getPiece(row1, col1);
		
		if (p == null) throw new IllegalArgumentException();
		
		if(p.move(row1, col1, row2, col2, board, turn)) {
			Piece temp = board.getPiece(row2, col2);			

			// Check if this puts king in jeopardy
			// Check if castling first, since we need to check for jeopardy multiple times
			if (p instanceof King && Math.abs(col2-col1) > 1) {
				// We must be castling. 
				//First, make sure that we aren't castling through check
				int step = ((col2-col1)/Math.abs(col2-col1));
				for (int i = col1; i != col2+step; i += step) {
					board.setPiece(row1, i, p);
					if (i != col1) board.setPiece(row1, i-step, null);
					if (isCheck(turn)) {
						// Can't castle through check
						// Undo the move
						board.setPiece(row1, col1, p);
						board.setPiece(row2, i, null);
						throw new IllegalArgumentException();
					}
				}
				// If we make it here, there is no check, so we can move the rook too
				int rookCol = 0;
				if (col2 > 3) rookCol = 7; 
				Piece rook = board.getPiece(row2, rookCol);
				rook.setMoved(true);
				board.setPiece(row2, col2 - step, rook);
				board.setPiece(row2, rookCol, null);
				
			} else {
				// Not castling

				board.setPiece(row2, col2, p);
				board.setPiece(row1, col1, null);
				
				// Check if the move puts your king in jeopardy
				if (isCheck(turn)) {
					// That move put your own king in jeopardy, so it's invalid
					// Undo the move
					board.setPiece(row1, col1, p);
					board.setPiece(row2, col2, temp);
					throw new IllegalArgumentException();
				}
			}
				
			p.setMoved(true);
			
			// Check if this is en passant
			
			if (p instanceof Pawn) {
				if ((row2 != row1 && col2 != col1)) {
					
					// get the tile passed by
					Piece passedBy = board.getPiece(row1, col2);
					// Check if it's a pawn
					if (passedBy != null && (passedBy instanceof Pawn)) {
						// kill it if it just did a double stride
						if (!passedBy.getColor().equals(turn) && ((Pawn) passedBy).getEnPassant()) {
							board.setPiece(row1, col2, null);
						}
					}
				}
			}
			
			if (board.getEnPassant() != null) board.getEnPassant().setEnPassant(false);
			board.setEnPassant(null);
			
			// Check if this pawn did a double stride
			if (p instanceof Pawn) {
				if (Math.abs(row2 - row1) == 2) {
					// double stride
					((Pawn) p).setEnPassant(true);
					board.setEnPassant(((Pawn) p));
				}
			}
			
		}
		else {
			throw new IllegalArgumentException();
		}
		
		if((row2 == 7 || row2 == 0) && board.getPiece(row2, col2) instanceof Pawn) {
			board.setPiece(row2, col2, promo);
		}
		
		return board;
	}
	
	/**
	 * Returns an arraylist that represents all possible moves for the player represented by the color parameter
	 * 
	 * @param color the color of the player for which all moves are being calculated for
	 * @return an arraylist for all moves of a player
	 */
	public ArrayList<Move> allPossibleMoves(String color) {
		ArrayList<Move> moves = new ArrayList<>();
		
		for(int i = 0; i < 8; i++) {
			for(int x = 0; x < 8; x++) {
				if(board.getPiece(i, x) != null && board.getPiece(i, x).getColor().equals(color)) {
					Piece p = board.getPiece(i, x);
					for(int y = 0; y < 8; y++) {
						for(int z = 0; z < 8; z++) {
							if(p.move(i, x, y, z, board, color)) {
								moves.add(new Move(i, x, y, z));
							}
						}
					}
				}
				else {
					continue;
				}
			}
		}
		return moves;
	}
	
	/**
	 * Returns the location of the king with the given color
	 * 
	 * @param color the color of the king that the location is being calculated for
	 * @return the location of the king with the given color
	 */
	public int[] getKingLocation(String color) {
		int[] result = new int[2];
		for(int i = 0; i < 8; i++) {
			for(int x = 0; x < 8; x++) {
				if((board.getPiece(i,x) != null) && board.getPiece(i,x).toString().equals(color+"k")) {
					result[0] = i;
					result[1] = x;
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Takes in a color and returns a boolean value that represents if the king of that specific color is in check.
	 * 
	 * @param color the color of the king that is being checked if it is in check
	 * @return A boolean value that represents if the king with the given color is in check.
	 */
	public boolean isCheck(String color) {
		int[] kingLocation = getKingLocation(color.toLowerCase());
		
		String opColor = (color.toLowerCase().equals("w")) ? "b" : "w";
		
		for(int i = 0; i < 8; i++) {
			for(int x = 0; x < 8; x++) {
				if(board.getPiece(i, x) != null && board.getPiece(i, x).getColor().equals(opColor)) {
					if(board.getPiece(i, x).move(i, x, kingLocation[0], kingLocation[1], board, opColor)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns a boolean value representing if the king of the given color is in check mate
	 * 
	 * @param color the color of the king that is being checked if it is in check mate
	 * @return A boolean value that represents if the king with the given color is in check mate.
	 */
	public boolean isCheckMate(String color) {
		
		if(isCheck(color)) {
			
			ArrayList<Move> moves = allPossibleMoves(color);
			
			BoardState b = new BoardState();
			b.board = board.copy();
			
			for(int x = 0; x < moves.size(); x++) {
				
				int row1 = 8-moves.get(x).getRow1();
				char col1 = (char)(moves.get(x).getCol1()+97);
				int row2 = 8-moves.get(x).getRow2();
				char col2 = (char)(moves.get(x).getCol2()+97);
				
				String loc1 = col1 + "" + row1;
				String loc2 = col2 + "" + row2;
				
				try {
					b.setPosition(loc1, loc2, null, color);
				}
				catch(IllegalArgumentException e) {
					
				}
				if(!(b.isCheck(color))) {
					return false;
				}
				else {
					b.board = board.copy();
				}
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	/**
	 * Prints the state of teh current board
	 */
	public void printBoard() {
		for(int i = 0; i < 8; i++) {
			for(int x = 0; x < 8; x++) {
				if(board.getPiece(i, x) == null) {
					if((i+x)%2 == 0) {
						System.out.print("   ");
					}
					else {
						System.out.print("## ");
					}
				}
				else {
					System.out.print(board.getPiece(i, x) + " ");
				}
				
				if(x == 7) {
					System.out.print(8-i);
				}
			}
			System.out.println("");
		}
		System.out.println(" a  b  c  d  e  f  g  h");
	}

	/**
	 * Initiates a new chess game
	 */
	public void play() {
		String turn = "w";
		
		boolean resign = false;
		boolean draw = false;
		boolean checkmate = false;
		boolean drawOffer = false;
		
		printBoard();
		System.out.println();
		Scanner keyboard = new Scanner(System.in);
		
		
		while(!resign && !draw && !checkmate) {
			System.out.print((turn.equals("w")) ? "White's move: " : "Black's move: ");
			
			String input = keyboard.nextLine();
			
			if(drawOffer == true && input.toLowerCase().equals("draw")) {
				System.out.println("draw");
				break;
			}
			else {
				drawOffer = false;
			}
			
			if(input.toLowerCase().equals("resign")) {
				if(turn.equals("w")) {
					System.out.println("Black wins");
				}
				else {
					System.out.println("White wins");
				}
				break;
			}
			
			String[] positions = input.split(" ");
			
			if(positions.length == 3 && positions[2].equals("draw?")) {
				drawOffer = true;
			}
			
			try {
				if(positions.length == 3) {
					Piece p = getPromotion(positions[2], turn);
					setPosition(positions[0], positions[1], p, turn);
				}
				else {
					Piece p = getPromotion(" ", turn);
					setPosition(positions[0], positions[1], p, turn);
				}
				System.out.println();
				printBoard();
				System.out.println();
				
				if((turn.equals("w") && isCheck("b"))) { 
					if(isCheckMate("b")) {
						System.out.println("Checkmate");
						System.out.println("White wins");
						checkmate = true;
					}
					else {
						System.out.println("Check"); 
					}
				}
				
				if((turn.equals("b") && isCheck("w"))) {
					if(isCheckMate("w")) {
						System.out.println("Checkmate");
						System.out.println("Black wins");
						checkmate = true;
					}
					else {
						System.out.println("Check"); 
					}
				}
				
				turn = (turn.equals("w")) ? "b" : "w";
			}
			catch(IllegalArgumentException e) {
				System.out.println("Illegal Move, try again");
			}
		}

		keyboard.close();
	}
	
	public static void main(String[] args) {
		BoardState board = new BoardState();
		
		board.play();
	}
}
