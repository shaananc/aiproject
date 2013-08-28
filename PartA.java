/* Mitchell Brunton - mbrunton
   Shaanan Cohney   - sncohney
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/* Entry point to Part A of CS "Jumper" project. A file is provided via standard input, which
   consists of an ascii representation of a Jumper gameboard, mid-play. Assuming the board is
   valid, the number of possible place and jump moves available to both white and black players
   is calculated and displayed.
*/

public class PartA {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input;
            StringBuilder boardStringBuilder = new StringBuilder();

            int n = new Integer(br.readLine());
            if (n <= 0) {
                System.out.println("Input must begin with positive integer n");
                System.exit(1);
            }

            int i = 0;
            while ( (input = br.readLine()) != null) {
                boardStringBuilder.append(input.replace(" ", "").replace("\n", ""));
                i++;
            }
            if (i != n) {
                System.out.println("Must supply n rows of gameboard");
                System.exit(1);
            }

            String boardString = boardStringBuilder.toString();
            if (boardString.length() != n*n) {
                System.out.println("Row of incorrect length supplied");
                System.exit(1);
            }

            Gameboard gb = new Gameboard(n, boardString);
            MoveCounter mc = new MoveCounter(n);

            int numWPlaceMoves = mc.countPlaceMoves(gb);
            int numWJumpMoves = mc.countJumpMoves(gb, Cell.State.W);
            int numBPlaceMoves = mc.countPlaceMoves(gb);
            int numBJumpMoves = mc.countJumpMoves(gb, Cell.State.B);
            
            System.out.println("W " + numWPlaceMoves + " " + numWJumpMoves);
            System.out.println("B " + numBPlaceMoves + " " + numBJumpMoves);
		
        } catch (IOException e) {
            System.out.println("An IO Exception occurred. Input must be of form:\nn\nrow 0\n...\nrow n");
            System.exit(1);
        }
	}
	
	// just testing and debugging...
	public static void tests() {
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
	}
}
