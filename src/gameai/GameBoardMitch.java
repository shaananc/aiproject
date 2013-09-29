package gameai;

import java.util.ArrayList;

/* Mitchell Brunton - mbrunton
   Shaanan Cohney   - sncohney
*/

/* GameBoardMitch class represents a snapshot of a game of Jumper
   Actual board is an array of Cells, each of which can be
   in one of four states
*/

public class GameBoardMitch implements Piece {
	int n;
	int[] board;

	// empty board constructor
	public GameBoardMitch(int n) {

		this.n = n;		// create (n x n) board of empty cells
		board = new int[n*n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i*n + j] = EMPTY;
			}
		}
	}

	// copy constructor
	public GameBoardMitch(GameBoardMitch aGameBoardMitch) {
		this.n = aGameBoardMitch.n;
		this.board = new int[n*n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i*n + j] = aGameBoardMitch.board[i*n + j];
			}
		}
	}

	// string-to-board constructor (expects String with no whitespace)
	public GameBoardMitch(int n, String boardString) {
		this(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				char ch = boardString.charAt(i*n + j);
				switch (ch) {
				case '-':
					break;
				case 'B':
					board[i*n + j] = BLACK;
					break;
				case 'W':
					board[i*n + j] = WHITE;
					break;
				case 'X':
					board[i*n + j] = DEAD;
					break;
				default:
					System.out.println("Error parsing boardString -- unidentified char: " + ch);
					System.exit(0);
				}
			}
		}
	}

	// creates a copy of current gameboard and returns it
	public static GameBoardMitch newInstance(GameBoardMitch aGameBoardMitch) {
		return new GameBoardMitch(aGameBoardMitch);
	}

	public boolean isLegalMove(Move m) {
		if (m.IsPlaceMove) {
			return isLegalPlaceMove(m);
		} else {
			return isLegalJumpMove(m);
		}
	}

	public boolean isLegalPlaceMove(Move m) {
		if (m.RowPositions.length != 1 || m.ColPositions.length != 1) {
			return false;
		}

		int i = m.RowPositions[0];
		int j = m.ColPositions[0];

		if (i < 0 || i >= n || j < 0 || j >= n) {
			return false;
		}

		return board[i*n + j] == EMPTY;
	}

	public boolean isLegalJumpMove(Move m) {

		// create a scrap board to test move on
		GameBoardMitch testBoard = newInstance(this);

		int len = m.RowPositions.length;
		if (m.ColPositions.length != len || len < 2) {
			return false;
		}

		int i = m.RowPositions[0];
		int j = m.ColPositions[0];
		if (testBoard.board[i * n + j] != m.P){
			return false;
		}
		if (i < 0 || i >= n || j < 0 || j >= n) {
			return false;
		}

		// always considering single jump from (i,j) to (next_i,next_j), over (jumped_i,jumped_j)		
		for (int k = 1; k < len; k++) {
			int next_i = m.RowPositions[k];
			int next_j = m.ColPositions[k];
			if (next_i < 0 || next_i >= n || next_j < 0 || next_j >= n) {
				return false;
			}
			// next cell must be 0 or 2 rows/cols away
			if (Math.abs(next_i - i) != 2 && Math.abs(next_i - i) != 0) {
				return false;
			}
			if (Math.abs(next_j - j) != 2 && Math.abs(next_j - j) != 0) {
				return false;
			}
			int jumped_i = i + (next_i - i)/2;
			int jumped_j = j + (next_j - j)/2;
			// jumped cell must have a black/white piece
			if (testBoard.board[jumped_i * n + jumped_j] != BLACK) {
				if (testBoard.board[jumped_i * n + jumped_j] != WHITE) {
					return false;
				}
			}
			// next cell must be empty
			if (testBoard.board[next_i * n + next_j] != EMPTY) {
				return false;
			}
			// apply move to testBoard
			if (testBoard.board[jumped_i * n + jumped_j] != m.P) {
				testBoard.board[jumped_i * n + jumped_j] = DEAD;
			}
			testBoard.board[next_i * n + next_j] = m.P;
			i = next_i;
			j = next_j;
		}

		return true;
	}

	/* apply Move m to this gameboard */
	public void applyMove(Move m) {
		int i = m.RowPositions[0];
		int j = m.ColPositions[0];
		if (m.IsPlaceMove) {
			board[i * n + j] = m.P;
		} else {	
			int len = m.RowPositions.length;
			for (int k = 1; k < len; k++) {

				int next_i = m.RowPositions[k];
				int next_j = m.ColPositions[k];
				int jumped_i = i + (next_i - i)/2;
				int jumped_j = j + (next_j - j)/2;
				if (board[jumped_i * n + jumped_j] != m.P) {
					board[jumped_i * n + jumped_j] = DEAD;
				}
				board[next_i * n + next_j] = m.P;
				i = next_i;
				j = next_j;
			}
		}
	}

	// create copy of current board, apply move and return copy
	public GameBoardMitch applyMoveToChildBoard(Move m) {
		GameBoardMitch childBoard = newInstance(this);
		childBoard.applyMove(m);
		return childBoard;
	}

	public int getWinner() {
		int numWhite = 0;
		int numBlack = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				switch (board[i * n + j]) {
				case EMPTY:
					// game hasn't finished
					return EMPTY;
				case BLACK:
					numBlack++;
					break;
				case WHITE:
					numWhite++;
					break;
				}
			}
		}

		// determine winner (or DEAD for draw)
		if (numBlack > numWhite) {
			return BLACK;
		} else if (numWhite > numBlack) {
			return WHITE;
		} else {
			return DEAD;
		}
	}

	public ArrayList<NeighbourPair> getNeighbourPairs(int i, int j) {
		boolean hasLeft = j-2 >= 0;
        boolean hasRight = j+2 < n;
        boolean hasUp = i-2 >= 0;
        boolean hasDown = i+2 < n;

		ArrayList<NeighbourPair> neighbourPairs = new ArrayList<NeighbourPair>();

		// top left
		if ( hasLeft && hasUp ) {
			neighbourPairs.add( new NeighbourPair(i-1, j-1, i-2, j-2) );
		}

		// left
		if ( hasLeft ) {
			neighbourPairs.add( new NeighbourPair(i, j-1, i, j-2) );
		}

		// bottom left
		if ( hasLeft && hasDown ) {
			neighbourPairs.add( new NeighbourPair(i+1, j-1, i+2, j-2) );
		}

		// bottom
		if ( hasDown ) {
			neighbourPairs.add( new NeighbourPair(i+1, j, i+2, j) );
		}

		// bottom right
		if ( hasRight && hasDown ) {
			neighbourPairs.add( new NeighbourPair(i+1, j+1, i+2, j+2) );
		}

		// right
		if ( hasRight ) {
			neighbourPairs.add( new NeighbourPair(i, j+1, i, j+2) );
		}

		// top right
		if ( hasRight && hasUp ) {
			neighbourPairs.add( new NeighbourPair(i-1, j+1, i-2, j+2) );
		}

		// top
		if ( hasUp ) {
			neighbourPairs.add( new NeighbourPair(i-1, j, i-2, j) );
		}

		return neighbourPairs;
	}

	public String toString() {
		StringBuffer boardStringBuffer = new StringBuffer( (n+1)*n );	// n+1 for '\n'

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int piece = board[i*n + j];
				switch (piece) {
				case EMPTY:
					boardStringBuffer.append( "- ");
					break;
				case BLACK:
					boardStringBuffer.append( "B ");
					break;
				case WHITE:
					boardStringBuffer.append( "W ");
					break;
				case DEAD:
					boardStringBuffer.append( "X ");
					break;
				}
			}
			boardStringBuffer.append('\n');
		}

		return boardStringBuffer.toString();
	}
}