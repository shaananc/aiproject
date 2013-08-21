
public class PartA {

	public static void main(String[] args) {
		int n;
		MoveCounter mc;
		String boardString;
		Gameboard gb;
		int numPlaceMoves, numJumpMoves, numMoves;
		
		// first example
		n = 3;
		mc = new MoveCounter(n);
		boardString = "-B-" +
					  "BB-" +
					  "--W";

		gb = new Gameboard(n, boardString);
		
		// first printout
		numPlaceMoves = mc.countPlaceMoves(gb);
		numJumpMoves = mc.countJumpMoves(gb, Cell.State.W);
		numMoves = mc.countMoves(gb, Cell.State.W);
		System.out.println("numPlaceMoves = " + numPlaceMoves);
		System.out.println("numJumpMoves = " + numJumpMoves);
		System.out.println("numMoves = " + numMoves);
		System.out.println("\n\n");
		
		// second example
		n = 5;
		mc = new MoveCounter(n);
		boardString = 		 "--B--" +
							 "---B-" +
							 "-WB--" +
							 "---B-" +
							 "--B--";
		
		gb = new Gameboard(n, boardString);
		
		// second printout
		numPlaceMoves = mc.countPlaceMoves(gb);
		numJumpMoves = mc.countJumpMoves(gb, Cell.State.W);
		numMoves = mc.countMoves(gb, Cell.State.W);
		System.out.println("numPlaceMoves = " + numPlaceMoves);
		System.out.println("numJumpMoves = " + numJumpMoves);
		System.out.println("numMoves = " + numMoves);
		System.out.println("\n\n");
		
		// example given in specs
		n = 6;
		mc = new MoveCounter(n);
		boardString = "BW-W-B" +
					  "WXWBBW" +
					  "XXWBXB" +
					  "BXWWXW" +
					  "XBBBXX" +
					  "BBBXWW";
		gb = new Gameboard(n, boardString);
		
		// spec printout
		int numWPlaceMoves = mc.countPlaceMoves(gb);
		int numWJumpMoves = mc.countJumpMoves(gb, Cell.State.W);
		int numBPlaceMoves = mc.countPlaceMoves(gb);
		int numBJumpMoves = mc.countJumpMoves(gb, Cell.State.B);
		
		System.out.println("W\t" + numWPlaceMoves + "\t" + numWJumpMoves);
		System.out.println("B\t" + numBPlaceMoves + "\t" + numBJumpMoves);
		System.out.println("\n\n");
		
		return;
	}
}
