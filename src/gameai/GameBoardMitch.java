package gameai;
import java.util.ArrayList;
import java.util.Random;

/* Mitchell Brunton - mbrunton
   Shaanan Cohney   - sncohney
*/

/* Gameboard class represents a snapshot of a game of Jumper
   Actual board is an array of Cells, each of which can be
   in one of four states
*/

public class GameBoardMitch implements Piece, GameBoard {
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
	public GameBoardMitch(GameBoardMitch aGameboard) {
		this.n = aGameboard.n;
		this.board = new int[n*n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i*n + j] = aGameboard.board[i*n + j];
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
	public static GameBoardMitch newInstance(GameBoardMitch aGameboard) {
		return new GameBoardMitch(aGameboard);
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
			// apply single jump to testBoard
			if (testBoard.board[jumped_i * n + jumped_j] != m.P) {
				testBoard.board[jumped_i * n + jumped_j] = DEAD;
			}
			testBoard.board[next_i * n + next_j] = m.P;
			i = next_i;
			j = next_j;
		}
		
		return true;
	}
	
	// apply Move m to this gameboard
	// assume Move is legal
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
	
	public int getWinner() {
		
		int numWhite = 0;
		int numBlack = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				switch (board[i * n + j]) {
				case EMPTY:
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
	
	public int getDensity() {
		int density = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (board[i * n + j] != EMPTY) {
					density++;
				}
			}
		}
		
		return density;
	}
	
	// calculate if the player piece at location (i,j) is jumpable
	public boolean isJumpable(int i, int j, int player) {
		if (board[i*n + j] != player) {
			return false;
		}
		
		boolean hasUpper = i-1 >= 0;
		boolean hasLower = i+1 < n;
		boolean hasLeft = j-1 >= 0;
		boolean hasRight = j+1 < n;
		
		// top-left and bottom-right
		if (hasUpper && hasLeft && hasLower && hasRight) {
			if (board[(i-1)*n + (j-1)] == EMPTY) {
				if (board[(i+1)*n + (j+1)] != player) {
					if (board[(i+1)*n + (j+1)] != DEAD) {
						return true;
					}
				}
			} else if (board[(i-1)*n + (j-1)] != player) {
				if (board[(i-1)*n + (j-1)] != DEAD) {
					if (board[(i+1)*n + (j+1)] == EMPTY) {
						return true;
					}
				}
			}
		}
		
		// top and bottom
		if (hasUpper && hasLower) {
			if (board[(i-1)*n + j] == EMPTY) {
				if (board[(i+1)*n + j] != player) {
					if (board[(i+1)*n + j] != DEAD) {
						return true;
					}
				}
			} else if (board[(i-1)*n + j] != player) {
				if (board[(i-1)*n + j] != DEAD) {
					if (board[(i+1)*n + j] == EMPTY) {
						return true;
					}
				}
			}
		}
		
		// top-right and bottom-left
		if (hasUpper && hasRight && hasLower && hasLeft) {
			if (board[(i-1)*n + (j+1)] == EMPTY) {
				if (board[(i+1)*n + (j-1)] != player) {
					if (board[(i+1)*n + (j-1)] != DEAD) {
						return true;
					}
				}
			} else if (board[(i-1)*n + (j+1)] != player) {
				if (board[(i-1)*n + (j+1)] != DEAD) {
					if (board[(i+1)*n + (j-1)] == EMPTY) {
						return true;
					}
				}
			}
		}
		
		// left and right
		if (hasLeft && hasRight) {
			if (board[i*n + (j-1)] == EMPTY) {
				if (board[i*n + (j+1)] != player) {
					if (board[i*n + (j+1)] != DEAD) {
						return true;
					}
				}
			} else if (board[i*n + (j-1)] != player) {
				if (board[i*n + (j-1)] != DEAD) {
					if (board[i*n + (j+1)] == EMPTY) {
						return true;
					}
				}
			}
		}
		
		// can't be jumped
		return false;
	}
	
	// return a random nxn Gameboard
	public static GameBoardMitch getRandomBoard(int n) {
		StringBuffer buffer = new StringBuffer(n*n);
		Random rand = new Random();
		for (int i = 0; i < n*n; i++) {
			switch (rand.nextInt(4)) {
			case 0:
				buffer.append('-');
				break;
			case 1:
				buffer.append('B');
				break;
			case 2:
				buffer.append('W');
				break;
			case 3:
				buffer.append('X');
				break;
			}
		}
		
		return new GameBoardMitch(n, buffer.toString());
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

    @Override
    public boolean isWhite(int loc) {
        return board[loc] == WHITE;
    }

    @Override
    public boolean isBlack(int loc) {
        return board[loc] == BLACK;
    }

    @Override
    public boolean isEmpty(int loc) {
       return board[loc] == EMPTY;
    }

    @Override
    public boolean isDead(int loc) {
        return board[loc] == DEAD;
    }

    @Override
    public boolean isSameColor(int loc1, int loc2) {
        return board[loc1] == board[loc2];
    }

    @Override
    public boolean isFull(int loc) {
        return board[loc] != EMPTY;
    }


}
