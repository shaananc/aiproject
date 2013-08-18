
public class PartA {

	public static void main(String[] args) {
		int n = 5;
		String boardString = "-----" +
							 "-----" +
							 "-WB--" +
							 "-----" +
							 "-----";
		
		Gameboard gb = new Gameboard(n, boardString);
		System.out.println("Gameboard:\n" + gb.toString());
		
		MoveCounter mc = new MoveCounter(n);
		int numMoves = mc.countMoves(gb, Cell.State.W);
		System.out.println("Number of legal white moves: " + numMoves);
		
		// example given in specs
		n = 6;
		boardString = "BW-W-B" +
					  "WXWBBW" +
					  "XXWBXB" +
					  "BXWWXW" +
					  "XBBBXX" +
					  "BBBXWW";
		gb = new Gameboard(n, boardString);
		mc = new MoveCounter(n);
		int numWPlaceMoves = mc.countPlaceMoves(gb);
		int numWJumpMoves = mc.countJumpMoves(gb, Cell.State.W);
		int numBPlaceMoves = mc.countPlaceMoves(gb);
		int numBJumpMoves = mc.countJumpMoves(gb, Cell.State.B);
		
		System.out.println("numWPlaceMoves = " + numWPlaceMoves);
		System.out.println("numWJumpMoves = " + numWJumpMoves);
		System.out.println("numBPlaceMoves = " + numBPlaceMoves);
		System.out.println("numBJumpMoves = " + numBJumpMoves);

		
		
		return;
	}
}
