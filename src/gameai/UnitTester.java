package gameai;

import java.util.ArrayList;

/* scratch-space for unit testing rest of project */

public class UnitTester implements Piece {

	public static void main(String[] args) {
		// testJumpNodes();
		testIsLegalMove();
	}
	
	public static void testIsLegalMove() {
		int n = 6;
		String boardString = "WB----" +
							 "------" +
							 "------" +
							 "------" +
							 "------" +
							 "------";
		
		Mbrunton bot = new Mbrunton();
		bot.init(n, WHITE, boardString);
		
		int[] rowPositions = {0,0};
		int[] colPositions = {0,2};
		Move m1 = new Move(WHITE, false, rowPositions, colPositions);
		if (bot.gb.isLegalMove(m1)) {
			System.out.println("m1 legal");
		} else {
			System.out.println("m1 illegal");
		}
	}
	
	public static void testJumpNodes() {
		int n = 6;
		String boardString = "---W--" +
							 "----B-" +
							 "--WB--" +
							 "------" +
							 "------" +
							 "------";
		
		GameBoardMitch gb = new GameBoardMitch(n, boardString);
		System.out.println("Original GameBoardMitch:");
		System.out.println(gb);
		
		Node node = new Node(gb, WHITE);
		node.getChildJumpNodes();
		
		System.out.println("Child jump nodes:");
		int i = 1;
		for (Node childNode : node.childNodes) {
			System.out.println("child " + i + ":");
			i++;
			System.out.println(childNode);
			System.out.println("\n\n");
		}
	}
}
