import java.util.ArrayList;

/* TODO: use more space-efficient method for storing Cell */
public class Gameboard implements Cloneable {

	private int n;
	public Cell[] board;
	
	public Gameboard(int n) {

		this.n = n;		// create (n x n) board of empty cells
		board = new Cell[n*n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i*n + j] = new Cell();
			}
		}
	}
	
	public Gameboard(int n, String boardString) {
		// TODO
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
	
	public ArrayList<NeighbourPair> getNeighbourPairs(Pos pos) {
		// TODO: consider storing 4 bools iPlus2, jPlus2 etc
		int i = pos.i;
		int j = pos.j;
		ArrayList<NeighbourPair> neighbourPairs = new ArrayList<NeighbourPair>();

		// top left
		if ( (i-2) >= 0 && (j-2) >= 0 ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i-1, j-1), new Pos(i-2, j-2)) );
		}

		// left
		if ( (j-2) >= 0 ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i, j-1), new Pos(i, j-2)) );
		}

		// bottom left
		if ( (i+2) < n && (j-2) >= 0 ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i+1, j-1), new Pos(i+2, j-2)) );
		}

		// bottom
		if ( (i+2) < n ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i+1, j), new Pos(i+2, j)) );
		}

		// bottom right
		if ( (i+2) < n && (j+2) < n ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i+1, j+1), new Pos(i+2, j+2)) );
		}

		// right
		if ( (j+2) < n ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i, j+1), new Pos(i, j+2)) );
		}

		// top right
		if ( (i-2) >= 0 && (j+2) < n) {
			neighbourPairs.add( new NeighbourPair(new Pos(i-1, j+1), new Pos(i-2, j+2)) );
		}

		// top
		if ( (i-2) >= 0 ) {
			neighbourPairs.add( new NeighbourPair(new Pos(i-1, j), new Pos(i-2, j)) );
		}

		return neighbourPairs;
	}
	
	public int getN() {
		return n;
	}
	
	// place piece of state "player" at position "pos" and return new Gameboard
	public Gameboard executeMove(Cell.State player, Pos pos) {
		// TODO: cleanup this code
		try {
			Gameboard child = (Gameboard) this.clone();
			if (player == Cell.State.B) { 
				child.board[pos.i * n + pos.j].placeBlack();
			} else {
				child.board[pos.i * n + pos.j].placeWhite();			
			}
			
			return child;
			
		} catch (CloneNotSupportedException e) {
			System.out.println("Unable to clone Gameboard");
			System.exit(0);
			return null;	// to keep the compiler happy
		}
	}
	
	public Gameboard executeMove(Cell.State player, Pos pos, Pos posKill) {
		try {
			Gameboard child = (Gameboard) this.clone();
			
			if (player == Cell.State.B) { 
				child.board[pos.i * n + pos.j].placeBlack();
			} else {
				child.board[pos.i * n + pos.j].placeWhite();			
			}
			
			Cell posKillCell = child.board[posKill.i * n + posKill.j];
			if (posKillCell.state == Cell.State.B && player == Cell.State.W) {
				posKillCell.killCell();
			}
			
			if (posKillCell.state == Cell.State.W && player == Cell.State.B) {
				posKillCell.killCell();
			}	
			
			return child;
			
		} catch (CloneNotSupportedException e) {
			System.out.println("Unable to clone Gameboard");
			System.exit(0);
			return null;	// to keep the compiler happy
		}
	}
	
	public String toString() {
		/* TODO */
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
