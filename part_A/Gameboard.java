/* Mitchell Brunton - mbrunton
   Shaanan Cohney   - sncohney
*/

import java.util.ArrayList;

/* Gameboard class represents a snapshot of a game of Jumper
   Actual board is an array of Cells, each of which can be
   in one of four states
*/

public class Gameboard {

	private int n;
	public Cell[] board;
	
	// empty board constructor
	public Gameboard(int n) {

		this.n = n;		// create (n x n) board of empty cells
		board = new Cell[n*n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i*n + j] = new Cell();
			}
		}
	}
	
	// copy constructor
	public Gameboard(Gameboard aGameboard) {
		this.n = aGameboard.n;
		this.board = new Cell[n*n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i*n + j] = new Cell(aGameboard.board[i*n + j].state);
			}
		}
	}
	
	// string-to-board constructor
	public Gameboard(int n, String boardString) {
		this(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				switch (boardString.charAt(i*n + j)) {
				case '-':
					break;
				case 'B':
					board[i*n + j].placeBlack();
					break;
				case 'W':
					board[i*n + j].placeWhite();
					break;
				case 'X':
					board[i*n + j].killCell();
					break;
				default:
					System.out.println("Error parsing boardString -- unidentified char");
					System.exit(0);
				}
			}
		}
	}
	
	// creates a copy of current gameboard and returns it
	public static Gameboard newInstance(Gameboard aGameboard) {
		return new Gameboard(aGameboard);
	}
	
	/* returns ArrayList of NeighbourPairs for a given Pos, where a NeighbourPair
	   consists of 2 Pos's radially adjacent to "pos", where a piece situated on
	   "pos" could potentially jump over NeighbourPair.neighbour and onto
	   NeighbourPair.neighboursNeighbour
    */
	public ArrayList<NeighbourPair> getNeighbourPairs(Pos pos) {
		int i = pos.i;
		int j = pos.j;
        boolean hasLeft = j-2 >= 0;
        boolean hasRight = j+2 < n;
        boolean hasUp = i-2 >= 0;
        boolean hasDown = i+2 < n;

		ArrayList<NeighbourPair> neighbourPairs = new ArrayList<NeighbourPair>();

		// top left
		if ( hasLeft && hasUp ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i-1, j-1), new Pos(i-2, j-2)) );
		}

		// left
		if ( hasLeft ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i, j-1), new Pos(i, j-2)) );
		}

		// bottom left
		if ( hasLeft && hasDown ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i+1, j-1), new Pos(i+2, j-2)) );
		}

		// bottom
		if ( hasDown ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i+1, j), new Pos(i+2, j)) );
		}

		// bottom right
		if ( hasRight && hasDown ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i+1, j+1), new Pos(i+2, j+2)) );
		}

		// right
		if ( hasRight ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i, j+1), new Pos(i, j+2)) );
		}

		// top right
		if ( hasRight && hasUp ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i-1, j+1), new Pos(i-2, j+2)) );
		}

		// top
		if ( hasUp ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i-1, j), new Pos(i-2, j)) );
		}

		return neighbourPairs;
	}
	
	// place "player" piece at position "pos" on new Gameboard and return Gameboard
	public Gameboard executeMove(Cell.State player, Pos pos) {
		Gameboard child = newInstance(this);
		
		if (player == Cell.State.B) { 
			child.board[pos.i * n + pos.j].placeBlack();
		} else if (player == Cell.State.W) {
			child.board[pos.i * n + pos.j].placeWhite();			
		}
		
		return child;
	}
	
	// for jump moves - possibly kill cell at position "posKill"
	public Gameboard executeMove(Cell.State player, Pos pos, Pos posKill) {
		// place player piece at pos
		Gameboard child = executeMove(player, pos);
		
		// kill cell at posKill if it's opposite to player (jumped cell)
		Cell posKillCell = child.board[posKill.i * n + posKill.j];
		if (posKillCell.state == Cell.State.B && player == Cell.State.W) {
			posKillCell.killCell();
		}
		
		if (posKillCell.state == Cell.State.W && player == Cell.State.B) {
			posKillCell.killCell();
		}
		
		return child;
	}
	
	public String toString() {
		StringBuffer boardStringBuffer = new StringBuffer( (n+1)*n );	// n+1 for '\n'
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Cell cell = board[i*n + j];
				switch (cell.state) {
				case E:
					boardStringBuffer.append( '-');
					break;
				case B:
					boardStringBuffer.append( 'B');
					break;
				case W:
					boardStringBuffer.append( 'W');
					break;
				case X:
					boardStringBuffer.append( 'X');
					break;
				}
			}
			boardStringBuffer.append('\n');
		}
		
		return boardStringBuffer.toString();
	}
}
