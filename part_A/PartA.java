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
                throw new NumberFormatException();
            }

            while ( (input = br.readLine()) != null) {
                boardStringBuilder.append(input.replace(" ", "").replace("\n", ""));
            }

            String boardString = boardStringBuilder.toString();
            if (boardString.length() != n*n) {
                throw new InvalidBoardStringException();
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
            System.out.println("An IO Exception occurred. Aborting...");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("The first line of input must be a positive integer, n, " +
                                "representing board side length");
            System.exit(1);
        } catch (InvalidBoardStringException e) {
            System.out.println("Supplied board string must contain n rows, with each row " +
                                "containing n cell states. Possible states = {'-', 'B', 'W', 'E'}");
            System.exit(1);
        }
	}
}

