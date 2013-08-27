/* Mitchell Brunton - mbrunton
   Shaanan Cohney   - sncohney
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/* Entry point to Part A of CS "Jumper" project. A file is provided via standard input, which
   consists of an ascii representation of a Jumper gameboard, mid-play. Assuming the board is
   valid, the number of possible place and jump moves available to both white and black players
   is calculated and displayed.
*/

public class PartA {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		if (args.length != 1) {
			System.out.println("usage: java PartA input_file");
			System.exit(1);
		}
		
		Scanner in = new Scanner(new BufferedReader(new FileReader(args[0])));
		int n = 0;
		try {
			n = in.nextInt();
            if (n <= 0) {
                throw new Exception();
            }
		} catch (Exception e) {
			System.out.println("Error: input file must begin with positive integer representing gameboard side length");
			System.exit(1);
		}
		
		StringBuilder boardStringBuilder = new StringBuilder();
		while (in.hasNext()) {
			boardStringBuilder.append(in.next());
		}

		String boardString = boardStringBuilder.toString();
		Gameboard gb = new Gameboard(n, boardString);
		MoveCounter mc = new MoveCounter(n);
		int numWPlaceMoves = mc.countPlaceMoves(gb);
		int numWJumpMoves = mc.countJumpMoves(gb, Cell.State.W);
		int numBPlaceMoves = mc.countPlaceMoves(gb);
		int numBJumpMoves = mc.countJumpMoves(gb, Cell.State.B);
		
		System.out.println("W " + numWPlaceMoves + " " + numWJumpMoves);
		System.out.println("B " + numBPlaceMoves + " " + numBJumpMoves);
		
		return;
	}
	
	// just testing and debugging...
	public static void tests() throws FileNotFoundException {
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
